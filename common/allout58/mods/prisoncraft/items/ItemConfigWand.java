package allout58.mods.prisoncraft.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.blocks.BlockPrisonManager;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;

public class ItemConfigWand extends Item
{

    public ItemConfigWand(int id)
    {
        super(id);
        setUnlocalizedName("prisonConfigWand");
        setTextureName(TextureConstants.RESOURCE_CONTEXT + ":" + getUnlocalizedName().substring(5));
        setCreativeTab(PrisonCraft.creativeTab);
        setMaxStackSize(1);
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
            // reset
            stack.stackTagCompound = null;
        }
        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if (player.isSneaking())
            {
                // reset
                stack.stackTagCompound = null;
                return true;
            }
            if (world.getBlockId(x, y, z) == BlockList.prisonMan.blockID) return false;
            if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
            if (!stack.stackTagCompound.hasKey("jailCoord1"))
            {
                int coord[] = new int[3];
                coord[0] = x;
                coord[1] = y;
                coord[2] = z;
                stack.stackTagCompound.setIntArray("jailCoord1", coord);
                player.sendChatToPlayer(new ChatMessageComponent().addKey("string.configwand.nextpt"));
            }
            else if (!stack.stackTagCompound.hasKey("jailCoord2"))
            {
                int coord[] = new int[3];
                coord[0] = x;
                coord[1] = y;
                coord[2] = z;
                stack.stackTagCompound.setIntArray("jailCoord2", coord);
                player.sendChatToPlayer(new ChatMessageComponent().addKey("string.configwand.tpin"));
            }
            else if (!stack.stackTagCompound.hasKey("tpIn"))
            {
                int coord[] = new int[3];
                coord[0] = x;
                coord[1] = y+1;
                coord[2] = z;
                stack.stackTagCompound.setIntArray("tpIn", coord);
                player.sendChatToPlayer(new ChatMessageComponent().addKey("string.configwand.tpout"));
            }
            else if (!stack.stackTagCompound.hasKey("tpOut"))
            {
                int coord[] = new int[3];
                coord[0] = x;
                coord[1] = y+1;
                coord[2] = z;
                stack.stackTagCompound.setIntArray("tpOut", coord);
                player.sendChatToPlayer(new ChatMessageComponent().addKey("string.configwand.done"));
            }

            return true;
        }
        else return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /** Allows items to add custom lines of information to the mouseover description. */
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List infoList, boolean par4)
    {
        // TODO See if this can be localized
        infoList.add("Right-click on two blocks to set");
        infoList.add("the bounds of the jail.");
        if (CommonProxy.shouldAddAdditionalInfo())
        {
            if (stack.stackTagCompound != null)
            {
                int coord[]=stack.stackTagCompound.getIntArray("jailCoord1");
                infoList.add(String.format("Block 1 {X: %d, Y: %d, Z: %d}", coord[0], coord[1], coord[2]));
                if (stack.stackTagCompound.hasKey("jailCoord2"))
                {
                    int coord2[]=stack.stackTagCompound.getIntArray("jailCoord2");
                    infoList.add(String.format("Block 2 {X: %d, Y: %d, Z: %d}", coord2[0], coord2[1], coord2[2]));
                }
            }
        }
        else
        {
            infoList.add(CommonProxy.additionalInfoInstructions());
        }
    }
}
