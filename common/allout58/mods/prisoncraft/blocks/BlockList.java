package allout58.mods.prisoncraft.blocks;

import allout58.mods.prisoncraft.config.Config;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockList
{
    public static Block prisonMan;
    public static Block prisonUnbreak;
    public static Block prisonUnbreakGlass;
    public static Block prisonUnbreakPaneGlass;
    public static Block prisonUnbreakPaneIron;

    public static void init()
    {
        prisonMan = new BlockPrisonManager(Config.prisonManager, Material.rock);
        prisonUnbreak = new BlockPrisonUnbreakable(Config.prisonUnbreak, Material.rock);
        prisonUnbreakGlass = new BlockPrisonUnbreakableGlass(Config.prisonUnbreakGlass, Material.glass);
        prisonUnbreakPaneGlass = new BlockPrisonUnbreakablePane(Config.prisonUnbreakPaneGlass, "glass", "glass_pane_top", Material.rock);
        prisonUnbreakPaneIron = new BlockPrisonUnbreakablePane(Config.prisonUnbreakPaneIron, "iron_bars", "iron_bars", Material.rock);

        GameRegistry.registerBlock(prisonMan, "prisonMan");
        GameRegistry.registerBlock(prisonUnbreak, "prisonUnbreak");
        GameRegistry.registerBlock(prisonUnbreakGlass, "prisonUnbreakGlass");
        GameRegistry.registerBlock(prisonUnbreakPaneGlass, "prisonUnbreakPaneGlass");
        GameRegistry.registerBlock(prisonUnbreakPaneIron, "prisonUnbreakPanIron");
    }
}
