package allout58.mods.prisoncraft.blocks;

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
    public static Block prisonUnbreakPaneStained;
    public static Block prisonJailView;

    public static void init()
    {
        prisonMan = new BlockPrisonManager(Material.rock);
        prisonUnbreak = new BlockPrisonUnbreakable(Material.rock);
        prisonUnbreakGlass = new BlockPrisonUnbreakableGlass(Material.glass);
        prisonUnbreakPaneGlass = new BlockPrisonUnbreakablePane("glass", "glass_pane_top", Material.rock);
        prisonUnbreakPaneIron = new BlockPrisonUnbreakablePane("iron_bars", "iron_bars", Material.rock);
        prisonUnbreakPaneStained = new BlockPrisonUnbreakablePaneStained();
        prisonJailView = new BlockJailView(Material.iron);

        GameRegistry.registerBlock(prisonMan, "prisonMan");
        GameRegistry.registerBlock(prisonUnbreak, "prisonUnbreak");
        GameRegistry.registerBlock(prisonUnbreakGlass, "prisonUnbreakGlass");
        GameRegistry.registerBlock(prisonUnbreakPaneGlass, "prisonUnbreakPaneGlass");
        GameRegistry.registerBlock(prisonUnbreakPaneIron, "prisonUnbreakPaneIron");
        GameRegistry.registerBlock(prisonJailView, "prisonJailView");
        GameRegistry.registerBlock(prisonUnbreakPaneStained, "prisonUnbreakPaneStained");
    }
}
