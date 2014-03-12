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
    public static Item jailLink;

    public static void init()
    {
        configWand = new ItemConfigWand();
        banhammer = new ItemBanHammer();
        oliveBranch = new ItemOliveBranch();
        jailLink = new ItemJailLink();

        GameRegistry.registerItem(configWand, "prisonConfigWand");
        GameRegistry.registerItem(banhammer, "banhammer");
        GameRegistry.registerItem(oliveBranch, "olivebranch");
        GameRegistry.registerItem(jailLink, "jailLink");
    }

    public static void registerRecipies()
    {
    }
}
