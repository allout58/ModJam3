package allout58.mods.prisoncraft.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import allout58.libs.LayeredTextureBlock.block.BlockLayeredTexture;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.items.ItemList;
import allout58.mods.prisoncraft.tileentities.TileEntityJailView;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockJailView extends BlockLayeredTexture implements ITileEntityProvider
{
    public IIcon top, bottom, side, side_nolink;

    public BlockJailView(Material par2Material)
    {
        super(par2Material);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setBlockName("jailView");
        setBlockTextureName(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_side");
        setCreativeTab(PrisonCraft.creativeTab);
        setBlockBounds(0, 0, 0, 1, (float) .5, 1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 1) return this.top;
        if (side == 0) return this.bottom;
        return this.side;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if (renderPass == 0)
        {
            if (side == ForgeDirection.DOWN.ordinal()) return this.bottom;
            if (side == ForgeDirection.UP.ordinal()) return this.top;
            return this.side;
        }
        else if (renderPass == 1)
        {
            if (meta == 0)
            {
                if (side == ForgeDirection.UP.ordinal())
                {
                    return this.side_nolink;
                }
                else return this.blank;
            }
            else
            {
                return this.blank;
            }
        }
        else
        {
            return null;
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
        super.registerBlockIcons(ir);
        this.side = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_side");
        this.side_nolink = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_side_nolink");
        this.bottom = this.top = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_top_bottom");

    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        super.onBlockActivated(world, x, y, z, entityPlayer, par6, par7, par8, par9);
        if (entityPlayer.isSneaking()) return false;
        if (!world.isRemote)
        {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityJailView)
            {
                if (entityPlayer.inventory.getCurrentItem() != null)
                {
                    if (entityPlayer.inventory.getCurrentItem().isItemEqual(new ItemStack(ItemList.jailLink)))
                    {
                        if (entityPlayer.inventory.getCurrentItem().hasTagCompound())
                        {
                            if (entityPlayer.inventory.getCurrentItem().stackTagCompound.hasKey("jailName"))
                            {
                                if (((TileEntityJailView) te).setJailName(entityPlayer.inventory.getCurrentItem().stackTagCompound.getString("jailName")))
                                {
                                    entityPlayer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.blockprisonmanager.jail.success")));
                                }
                                else
                                {
                                    entityPlayer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.blockprisonmanager.jail.failoverwrite")));
                                }
                            }
                        }
                        return true;
                    }
                }
                else
                {
                    entityPlayer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.linkedjailname") + ((TileEntityJailView) te).jailname));
                }
            }
        }
        return false;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityJailView();
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
