package net.teamhollow.readyyourshovels.entity.ant;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;

import java.util.Optional;
import java.util.function.Predicate;

public interface ResourceGatherer {
    BlockPos getResourcePos();
    void setResourcePos(BlockPos pos);
    boolean hasResourcePos();

    void setHasResource(boolean hasResource);
    boolean hasResource();

    void resetResourcePickupTicks();
    void setTicksUntilCanResourcePickup(int ticksUntilCanResourcePickup);
    int getTicksUntilCanResourcePickup();

    class Utils {
        public static boolean isResource(World world, BlockPos pos) {
            return world.canSetBlock(pos) && world.getBlockState(pos).getBlock().isIn(RYSBlockTags.ANT_RESOURCES);
        }
    }

    class ResourcePickupGoal extends Goal {
        private final AbstractAntEntity ant;
        private final ResourceGatherer gatherer;

        private final Predicate<BlockState> resourcePredicate = (blockState) -> blockState.isIn(RYSBlockTags.ANT_RESOURCES);
        private int resourcePickupTicks = 0;
        private int lastResourcePickupTick = 0;
        private boolean running;
        private Vec3d nextTarget;
        private int ticks = 0;

        public ResourcePickupGoal(AbstractAntEntity ant) {
            super();
            this.ant = ant;
            this.gatherer = (ResourceGatherer) ant;
        }

        @Override
        public boolean canStart() {
            if (gatherer.getTicksUntilCanResourcePickup() > 0) {
                return false;
            } else if (ant.getNavigation().isIdle()) {
                return false;
            } else if (gatherer.hasResource()) {
                return false;
            } else if (ant.world.isRaining()) {
                return false;
            } else if (ant.getRandom().nextFloat() < 0.7F) {
                return false;
            } else {
                Optional<BlockPos> optional = this.getResource();
                if (optional.isPresent()) {
                    gatherer.setResourcePos(optional.get());
                    BlockPos resourcePos = gatherer.getResourcePos();
                    ant.getNavigation().startMovingTo((double) resourcePos.getX() + 0.5D, (double) resourcePos.getY() + 1.0D, (double) resourcePos.getZ() + 0.5D, 1.2000000476837158D);
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean shouldContinue() {
            if (!this.running || gatherer.hasResource() || ant.world.isRaining() || ant.world.isNight()) {
                return false;
            } else if (this.completedPickup()) {
                return ant.getRandom().nextFloat() < 0.2F;
            } else if (ant.age % 20 == 0 && !ResourceGatherer.Utils.isResource(ant.world, gatherer.getResourcePos())) {
                gatherer.setResourcePos(null);
                return false;
            } else {
                return true;
            }
        }

        private boolean completedPickup() {
            return this.resourcePickupTicks > 400;
        }

        @Override
        public void start() {
            this.resourcePickupTicks = 0;
            this.ticks = 0;
            this.lastResourcePickupTick = 0;
            this.running = true;

            World world = this.ant.world;
            BlockPos pos = this.gatherer.getResourcePos();
            BlockState state = world.getBlockState(pos);
            world.breakBlock(pos, false);
            world.setBlockState(pos, state, 0);

            gatherer.resetResourcePickupTicks();
        }

        @Override
        public void tick() {
            this.ticks++;

            World world = this.ant.world;
            BlockPos pos = this.gatherer.getResourcePos();
            BlockState state = world.getBlockState(pos);

            if (this.ticks > 600) {
                gatherer.setResourcePos(null);
            } else {
                if (gatherer.hasResourcePos()) {
                    Vec3d vec3d = Vec3d.ofBottomCenter(gatherer.getResourcePos()).add(0.0D, 1.0D, 0.0D);
                    if (this.nextTarget == null) {
                        this.nextTarget = vec3d;
                    }
                    ant.getLookControl().lookAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());

                    boolean isWithinRangeOfTarget = ant.getPos().distanceTo(this.nextTarget) <= 0.1D;
                    if (!isWithinRangeOfTarget) {
                        if (!ant.isNavigating()) ant.startMovingTo(gatherer.getResourcePos());

                        if (this.ticks > 600) {
                            world.breakBlock(pos, false);
                            world.setBlockState(pos, state, 0);

                            gatherer.setResourcePos(null);
                        } else {
                            this.resourcePickupTicks++;
                            if (ant.getRandom().nextFloat() < 0.05F && this.resourcePickupTicks > this.lastResourcePickupTick + 60) {
                                this.lastResourcePickupTick = this.resourcePickupTicks;
                                world.breakBlock(pos, false);
                                world.setBlockState(pos, state, 0);
                                ant.playSound(SoundEvents.ENTITY_BEE_POLLINATE, 1.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void stop() {
            this.running = false;
            this.ant.getNavigation().stop();
            this.ant.cannotEnterNestTicks = 40;
            this.gatherer.setHasResource(true);

            World world = this.ant.world;
            BlockPos pos = this.gatherer.getResourcePos();
            BlockState state = world.getBlockState(pos);
            world.breakBlock(pos, false);
            world.setBlockState(pos, state, 0);

            this.gatherer.setTicksUntilCanResourcePickup(200);
        }

        private Optional<BlockPos> getResource() {
            return this.findResource(this.resourcePredicate, 5.0D);
        }

        private Optional<BlockPos> findResource(Predicate<BlockState> predicate, double searchDistance) {
            BlockPos blockPos = ant.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int y = 0; (double) y <= searchDistance; y = y > 0 ? -y : 1 - y) {
                for (int distance = 0; (double) distance < searchDistance; ++distance) {
                    for (int x = 0; x <= distance; x = x > 0 ? -x : 1 - x) {
                        for (int z = x < distance && x > -distance ? distance : 0; z <= distance; z = z > 0 ? -z : 1 - z) {
                            mutable.set(blockPos, x, y - 1, z);
                            if (blockPos.isWithinDistance(mutable, searchDistance) & predicate.test(ant.world.getBlockState(mutable))) {
                                return Optional.of(mutable);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
        }

        public boolean isRunning() {
            return this.running;
        }
    }
}
