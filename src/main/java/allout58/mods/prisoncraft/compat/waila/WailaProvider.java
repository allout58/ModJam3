package allout58.mods.prisoncraft.compat.waila;

import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.blocks.BlockPrisonUnbreakable;
import allout58.mods.prisoncraft.blocks.BlockPrisonUnbreakablePane;
import allout58.mods.prisoncraft.blocks.IPrisonCraftBlock;
import allout58.mods.prisoncraft.tileentities.TileEntityJailView;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonUnbreakable;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by James Hollowell on 4/29/2014.
 */
public class WailaProvider implements IWailaDataProvider
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerStackProvider(new WailaProvider(), IPrisonCraftBlock.class);
        registrar.registerHeadProvider(new WailaProvider(), IPrisonCraftBlock.class);
        registrar.registerBodyProvider(new WailaProvider(), Block.class);
        registrar.registerTailProvider(new WailaProvider(), IPrisonCraftBlock.class);
    }

    @Override public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        if (accessor.getBlock() instanceof BlockPrisonUnbreakable)
        {
            return new ItemStack(BlockList.prisonUnbreak);
        }
        if (accessor.getBlock() instanceof BlockPrisonUnbreakablePane)
        {
            return new ItemStack(BlockList.prisonUnbreakPaneGlass);
        }
        return null;
    }

    @Override public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Override public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        //Prison walls
        if (accessor.getTileEntity() instanceof TileEntityPrisonUnbreakable)
        {
            TileEntityPrisonUnbreakable te = (TileEntityPrisonUnbreakable) accessor.getTileEntity();
            currenttip.add("Acts like: " + te.getFakeBlock().getLocalizedName());
            currenttip.add("Meta like: " + te.getFakeBlockMeta());
        }
        //Jail viewer
        else if (accessor.getTileEntity() instanceof TileEntityJailView)
        {
            TileEntityJailView te = (TileEntityJailView) accessor.getTileEntity();
            currenttip.add("Jail watched: " + te.jailname);
        }
        //Manager
        else if (accessor.getTileEntity() instanceof TileEntityPrisonManager)
        {
            TileEntityPrisonManager te = (TileEntityPrisonManager) accessor.getTileEntity();
            if (te.hasJailedPlayer)
                currenttip.add("Currently jailed player: " + te.playerName);
            else
                currenttip.add("No player currently jailed.");
        }
        //DEBUG CODE :D maybe...
        if (accessor.getPlayer().isSneaking())
        {
            currenttip.add("Name: " + Block.blockRegistry.getNameForObject(accessor.getBlock()));
            currenttip.add("Meta: " + accessor.getMetadata());
        }
        return currenttip;
    }

    @Override public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }
}
