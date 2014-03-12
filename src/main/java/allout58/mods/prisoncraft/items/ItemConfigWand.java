package allout58.mods.prisoncraft.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.blocks.BlockPrisonManager;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemConfigWand extends Item
{
    private IIcon main;
    private IIcon lock;

    public ItemConfigWand()
    {
        super();
        setUnlocalizedName("prisonConfigWand");
//        setCreativeTab(PrisonCraft.creativeTab);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir)
    {
        main = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + getUnlocalizedName().substring(5));
        lock = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + getUnlocalizedName().substring(5) + ".lock");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass)
    {
        // If locked
        // if (stack.getItemDamage() == 1)
        // {
        // if (renderPass != 1) return lock;
        // else return main;
        // }
        // Else, unlocked
        // else
        // {
        return main;
        // }
    }

    @Override
    public boolean getShareTag()
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            if (player.isSneaking())
            {
                // clear all points
                stack.stackTagCompound = null;
                // int dam=stack.getItemDamage();
                // if(dam==0)dam=1;
                // else if(dam==1)dam=0;
                // stack.setItemDamage(dam);
            }
        }
        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if (stack.stackTagCompound == null)
            {
                stack.stackTagCompound = new NBTTagCompound();
            }
            if (stack.getItemDamage() == 0)
            {
                if (world.getBlock(x, y, z) == BlockList.prisonMan) return false;

                if (!stack.stackTagCompound.hasKey("jailCoord1"))
                {
                    stack.stackTagCompound.setInteger("jailDim", player.dimension);
                    int coord[] = new int[3];
                    coord[0] = x;
                    coord[1] = y;
                    coord[2] = z;
                    stack.stackTagCompound.setIntArray("jailCoord1", coord);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("string.configwand.nextpt")));
                }
                else if (!stack.stackTagCompound.hasKey("jailCoord2"))
                {
                    int coord[] = new int[3];
                    coord[0] = x;
                    coord[1] = y;
                    coord[2] = z;
                    stack.stackTagCompound.setIntArray("jailCoord2", coord);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("string.configwand.tpin")));
                }
                else if (!stack.stackTagCompound.hasKey("tpIn"))
                {
                    int coord[] = new int[3];
                    coord[0] = x;
                    coord[1] = y + 1;
                    coord[2] = z;
                    stack.stackTagCompound.setIntArray("tpIn", coord);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("string.configwand.tpout")));
                }
                else if (!stack.stackTagCompound.hasKey("tpOut"))
                {
                    int coord[] = new int[3];
                    coord[0] = x;
                    coord[1] = y + 1;
                    coord[2] = z;
                    stack.stackTagCompound.setIntArray("tpOut", coord);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("string.configwand.done")));
                }
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /** Allows items to add custom lines of information to the mouseover description. */
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List infoList, boolean par4)
    {
        // TODO See if this can be localized
        if (CommonProxy.shouldAddAdditionalInfo())
        {
            // infoList.add((stack.getItemDamage() == 1) ? "Locked" :
            // "Unlocked");
            if (stack.stackTagCompound != null)
            {
                if (stack.stackTagCompound.hasKey("jailCoord1"))
                {
                    int coord[] = stack.stackTagCompound.getIntArray("jailCoord1");
                    infoList.add(String.format("Corner 1 {X: %d, Y: %d, Z: %d}", coord[0], coord[1], coord[2]));
                }
                if (stack.stackTagCompound.hasKey("jailCoord2"))
                {
                    int coord2[] = stack.stackTagCompound.getIntArray("jailCoord2");
                    infoList.add(String.format("Corner 2 {X: %d, Y: %d, Z: %d}", coord2[0], coord2[1], coord2[2]));
                }
                if (stack.stackTagCompound.hasKey("tpIn"))
                {
                    int coord2[] = stack.stackTagCompound.getIntArray("tpIn");
                    infoList.add(String.format("TP In {X: %d, Y: %d, Z: %d}", coord2[0], coord2[1], coord2[2]));
                }
                if (stack.stackTagCompound.hasKey("tpOut"))
                {
                    int coord2[] = stack.stackTagCompound.getIntArray("tpOut");
                    infoList.add(String.format("TP Out {X: %d, Y: %d, Z: %d}", coord2[0], coord2[1], coord2[2]));
                }
                infoList.add("");
                infoList.add("Shift right-click to clear all points stored.");
            }
            else
            {
                infoList.add("Start configuration by right-clicking");
                infoList.add("on one corner of the jail cell.");
            }
        }
        else
        {
            infoList.add(CommonProxy.additionalInfoInstructions());
        }
    }
}
