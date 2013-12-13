package allout58.mods.prisoncraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import allout58.mods.prisoncraft.Config;
import net.minecraft.item.Item;

public class ItemList
{
    public static Item configWand;
    
    public static void init()
    {
        configWand=new ItemConfigWand(Config.configWand);
        
        GameRegistry.registerItem(configWand, "prisonConfigWand");
    }
}
