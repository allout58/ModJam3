package allout58.mods.prisoncraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import allout58.mods.prisoncraft.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemList
{
    public static Item configWand;
    
    public static void init()
    {
        configWand=new ItemConfigWand(Config.configWand);
        
        GameRegistry.registerItem(configWand, "prisonConfigWand");
    }
    
    public static void registerRecipies()
    {
        GameRegistry.addShapelessRecipe(new ItemStack(configWand), "W", new ItemStack(configWand));
    }
}
