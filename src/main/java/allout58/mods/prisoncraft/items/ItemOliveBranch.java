package allout58.mods.prisoncraft.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.network.UnjailPacket;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOliveBranch extends ItemEntityTargetTool
{

    public ItemOliveBranch()
    {
        super();
        setUnlocalizedName("olivebranch");
        setTextureName(TextureConstants.RESOURCE_CONTEXT + ":" + getUnlocalizedName().substring(5));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        super.onItemRightClick(stack, world, player);

        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("userHit"))
        {
            // Build packet
            PrisonCraft.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
            PrisonCraft.channels.get(Side.CLIENT).writeOutbound(new UnjailPacket(stack.stackTagCompound.getString("userHit"), player.getDisplayName()));
        }

        return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if(world.getTileEntity(x, y, z) instanceof TileEntityPrisonManager)
            {
                TileEntityPrisonManager te=(TileEntityPrisonManager)world.getTileEntity(x, y, z);
                if (te.hasJailedPlayer)
                {
                    JailMan.getInstance().TryUnjailPlayer(te.playerName, player);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /** Allows items to add custom lines of information to the mouseover description. */
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List infoList, boolean par4)
    {
        // TODO See if this can be localized
        if (CommonProxy.shouldAddAdditionalInfo())
        {
            infoList.add("Use this tool to release the player");
            infoList.add(" you are looking at from jail!");
            infoList.add("Can also be used on a prison manager");
            infoList.add(" to release its player.");
        }
        else
        {
            infoList.add(CommonProxy.additionalInfoInstructions());
        }
    }
}
