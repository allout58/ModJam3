package allout58.mods.prisoncraft.constants;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ModConstants
{
    public static final String MODID = "prisoncraft";
    public static final String NAME = "PrisonCraft";
//    public static final String PACKETCHANNEL = "PrisonCraft";
    public static final String JAIL_PACKET_CHANNEL = "PCJail";
    public static final String UNJAIL_PACKET_CHANNEL = "PCUnjail";
    public static final String JV_CLIENT_TO_SERVER_PACKET_CHANNEL = "JVCtoS";
    public static final String JV_SERVER_TO_CLIENT_PACKET_CHANNEL = "JVStoC";

    public static final int ITEM_ID_DIFF = 256;

    public static final String[] getWHITELIST_WALL_BLOCKS()
    {
        String[] wht={ getName(Blocks.stone), getName(Blocks.cobblestone), getName(Blocks.bedrock), getName(Blocks.glass), getName(Blocks.sandstone), getName(Blocks.gold_block), getName(Blocks.iron_block), getName(Blocks.brick_block), getName(Blocks.mossy_cobblestone), getName(Blocks.obsidian), getName(Blocks.diamond_block), getName(Blocks.stonebrick), getName(Blocks.iron_bars), getName(Blocks.glass_pane), getName(Blocks.nether_brick), getName(Blocks.emerald_block) };
        return wht;
    }
    
    private static final String getName(Block b)
    {
        return Block.blockRegistry.getNameForObject(b);
    }
}
