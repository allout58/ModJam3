package allout58.mods.prisoncraft.blocks;

import allout58.mods.prisoncraft.Config;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockList
{
    public static Block prisonMan;
    public static Block prisonUnbreak;
    
    public static void init()
    {
        prisonMan=new BlockPrisonMaintainer(Config.prisonManager, Material.rock);
        prisonUnbreak=new BlockPrisonUnbreakable(Config.prisonUnbreak, Material.rock);
        
        GameRegistry.registerBlock(prisonMan,"prisonMan");
        GameRegistry.registerBlock(prisonUnbreak,"prisonUnbreak");
    }
}
