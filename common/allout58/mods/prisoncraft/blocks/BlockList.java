package allout58.mods.prisoncraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class BlockList
{
    public static Block prisonMan;
    public static Block prisonUnbreak;
    
    public static void init()
    {
        prisonMan=new BlockPrisonMaintainer();
        
        GameRegistry.registerBlock(prisonMan);
        GameRegistry.registerBlock(prisonUnbreak);
    }
}
