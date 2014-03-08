package allout58.mods.prisoncraft.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.constants.TextureConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemJailLink extends Item
{

    public ItemJailLink(int id)
    {
        super(id - ModConstants.ITEM_ID_DIFF);
        setUnlocalizedName("jaillink");
        setMaxStackSize(1);
        setTextureName(TextureConstants.RESOURCE_CONTEXT + ":" + getUnlocalizedName().substring(5));
//        setCreativeTab(PrisonCraft.creativeTab);
    }

    @Override
    public boolean getShareTag()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /** Allows items to add custom lines of information to the mouseover description. */
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List infoList, boolean par4)
    {
        // TODO See if this can be localized
        if (CommonProxy.shouldAddAdditionalInfo())
        {
            if (stack.hasTagCompound()&&stack.stackTagCompound.hasKey("jailName"))
            {
                    infoList.add("Jail name: " + stack.stackTagCompound.getString("jailName"));
            }
            else
            {
                infoList.add(EnumChatFormatting.RED.toString()+EnumChatFormatting.ITALIC.toString()+"!!!NOT SPAWNED IN CORRECTLY!!!");
                infoList.add("Use the /pc config jail <jailname>");
                infoList.add("command to properly spawn the tool.");
            }
            infoList.add("");
            infoList.add("Used to link cells together to");
            infoList.add("form a jail.");
        }
        else
        {
            infoList.add(CommonProxy.additionalInfoInstructions());
        }
    }
}
