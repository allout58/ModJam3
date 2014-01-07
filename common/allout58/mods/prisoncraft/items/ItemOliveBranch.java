package allout58.mods.prisoncraft.items;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

public class ItemOliveBranch extends ItemEntityTargetTool
{

    public ItemOliveBranch(int id)
    {
        super(id);
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
            ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
            DataOutputStream outputStream = new DataOutputStream(bos);
            try
            {
                outputStream.writeUTF(stack.stackTagCompound.getString("userHit"));
                outputStream.writeUTF(player.username);
                outputStream.writeDouble(-1);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            //Send packet
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = ModConstants.UNJAILPACKETCHANNEL;
            packet.data = bos.toByteArray();
            packet.length = bos.size();
            PacketDispatcher.sendPacketToServer(packet);
            stack.stackTagCompound=null;
        }

        return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if(world.getBlockTileEntity(x, y, z) instanceof TileEntityPrisonManager)
            {
                TileEntityPrisonManager te=(TileEntityPrisonManager)world.getBlockTileEntity(x, y, z);
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
            infoList.add("Use this tool to release the player you are looking at from jail!");
            infoList.add("Can also be used on a prison manager to release its player.");
        }
        else
        {
            infoList.add(CommonProxy.additionalInfoInstructions());
        }
    }
}
