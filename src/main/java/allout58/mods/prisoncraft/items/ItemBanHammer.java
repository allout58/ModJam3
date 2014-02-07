package allout58.mods.prisoncraft.items;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBanHammer extends ItemEntityTargetTool
{
    public ItemBanHammer(int id)
    {
        super(id);
        setUnlocalizedName("banhammer");
        setTextureName(TextureConstants.RESOURCE_CONTEXT + ":" + getUnlocalizedName().substring(5));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        super.onItemRightClick(stack, world, player);

        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("userHit"))
        {
            // System.out.printf("%s: User-> %s%s", world.isRemote ?
            // "Client" : "Server",
            // stack.stackTagCompound.getString("userHit"),
            // System.lineSeparator());
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

            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = ModConstants.JAIL_PACKET_CHANNEL;
            packet.data = bos.toByteArray();
            packet.length = bos.size();
            PacketDispatcher.sendPacketToServer(packet);
            stack.stackTagCompound = null;
        }

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /** Allows items to add custom lines of information to the mouseover description. */
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List infoList, boolean par4)
    {
        // TODO See if this can be localized
        if (CommonProxy.shouldAddAdditionalInfo())
        {
            infoList.add("Use this tool to send the player you are looking at to jail!");
        }
        else
        {
            infoList.add(CommonProxy.additionalInfoInstructions());
        }
    }
}
