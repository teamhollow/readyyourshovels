package group.rys.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.rys.common.item.ModItemGroup;
import net.minecraft.item.ItemGroup;

public class Reference {
	
	public static final String MOD_ID = "rys";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup();
	
}
