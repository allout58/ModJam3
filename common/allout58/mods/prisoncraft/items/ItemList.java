package allout58.mods.prisoncraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import allout58.mods.prisoncraft.config.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemList
{
    public static Item configWand;
    public static Item banhammer;
    public static Item oliveBranch;

    public static void init()
    {
        configWand = new ItemConfigWand(Config.configWand);
        banhammer = new ItemBanHammer(Config.banhammer);
        oliveBranch = new ItemOliveBranch(Config.oliveBranch);

        GameRegistry.registerItem(configWand, "prisonConfigWand");
        GameRegistry.registerItem(banhammer, "banhammer");
        GameRegistry.registerItem(oliveBranch, "olivebranch");
    }

    public static void registerRecipies()
    {
    }
}
