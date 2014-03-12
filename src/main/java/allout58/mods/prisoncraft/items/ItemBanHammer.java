package allout58.mods.prisoncraft.items;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.network.JailPacket;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBanHammer extends ItemEntityTargetTool
{
    public ItemBanHammer()
    {
        super();
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
            
            PrisonCraft.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
            PrisonCraft.channels.get(Side.CLIENT).writeOutbound(new JailPacket(stack.stackTagCompound.getString("userHit"), player.getDisplayName(), -1));
            
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
