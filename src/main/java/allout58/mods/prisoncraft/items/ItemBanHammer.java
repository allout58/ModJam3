package allout58.mods.prisoncraft.items;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.GuiIDs;
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
    public boolean getShareTag()
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (player.isSneaking())
        {
            player.openGui(PrisonCraft.instance, GuiIDs.BANHAMMER_GUI, world, (int)player.posX, (int)player.posY, (int)player.posZ);
        }
        else
        {
            super.onItemRightClick(stack, world, player);

            if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("userHit"))
            {
                String name="";
                double time=-1;
                
                if(stack.getTagCompound().hasKey("jailname"))
                {
                    name=stack.getTagCompound().getString("jailname");
                }
                if(stack.getTagCompound().hasKey("time"))
                {
                    time=stack.getTagCompound().getDouble("time");
                }
                
                // Build packet
                PrisonCraft.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
                PrisonCraft.channels.get(Side.CLIENT).writeOutbound(new JailPacket(stack.stackTagCompound.getString("userHit"), player.getDisplayName(), name, time));

                stack.stackTagCompound.removeTag("userHit");
            }
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
            
            if(stack.hasTagCompound())
            {
                if(stack.getTagCompound().hasKey("jailname"))
                {
                    infoList.add("Jail name: "+stack.getTagCompound().getString("jailname"));
                }
                if(stack.getTagCompound().hasKey("time"))
                {
                    infoList.add("Time: "+stack.getTagCompound().getDouble("time"));
                }
            }
        }
        else
        {
            infoList.add(CommonProxy.additionalInfoInstructions());
        }
    }
}
