package allout58.mods.prisoncraft.blocks;

import allout58.libs.LayeredTextureBlock.block.BlockLayeredTexture;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.items.ItemList;
import allout58.mods.prisoncraft.jail.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPrisonManager extends BlockLayeredTexture
        implements ITileEntityProvider, IPrisonCraftBlock
{
    public IIcon top, bottom, side, side_nolink, side_uninit;

    public BlockPrisonManager(Material par2Material)
    {
        super(par2Material);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setBlockName("prisonManager");
        setBlockTextureName(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_side");
        setCreativeTab(PrisonCraft.creativeTab);
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
            if (side == ForgeDirection.DOWN.ordinal() || side == ForgeDirection.UP.ordinal())
                return this.blank;
            if (meta == 0)
            {
                return this.side_uninit;
            }
            if (meta == 1)
            {
                return this.side_nolink;
            }
            else
            {
                return this.blank;
                // return null;
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
        this.side_uninit = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_side_uninit");
        this.side_nolink = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_side_nolink");
        this.bottom = this.top = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_top_bottom");

    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityPrisonManager();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntity logic = world.getTileEntity(x, y, z);
        if (logic instanceof TileEntityPrisonManager)
        {
            PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(world);
            for (int i = 0; i < ws.getTesList().size(); i++)
            {
                int coord[] = ws.getTesList().get(i).coord;
                if (coord[0] == logic.xCoord && coord[1] == logic.yCoord && coord[2] == logic.zCoord)
                {
                    PrisonCraftWorldSave.forWorld(world).getTesList().remove(i);
                }
            }
            ((TileEntityPrisonManager) logic).revertBlocks();
            ((TileEntityPrisonManager) logic).unjailPlayer();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        super.onBlockActivated(world, x, y, z, entityPlayer, par6, par7, par8, par9);
        if (!world.isRemote)
        {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityPrisonManager)
            {
                if (entityPlayer.inventory.getCurrentItem() != null)
                {
                    if (entityPlayer.inventory.getCurrentItem().isItemEqual(new ItemStack(ItemList.configWand)))
                    {
                        if (entityPlayer.inventory.getCurrentItem().stackTagCompound != null)
                        {
                            NBTTagCompound tags = entityPlayer.inventory.getCurrentItem().stackTagCompound;
                            if (tags.hasKey("jailCoord1") && tags.hasKey("jailCoord2") && tags.hasKey("tpIn") && tags.hasKey("tpOut"))
                            {
                                if (((TileEntityPrisonManager) te).changeBlocks(entityPlayer.inventory.getCurrentItem().stackTagCompound))
                                {
                                    entityPlayer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.blockprisonmanager.cell.success")));
                                }
                                else
                                {
                                    entityPlayer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.blockprisonmanager.cell.failoverwrite")));
                                }
                            }
                            else
                            {
                                entityPlayer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.blockprisonmanager.cell.fail")));
                            }
                            return true;
                        }
                    }
                    if (entityPlayer.inventory.getCurrentItem().isItemEqual(new ItemStack(ItemList.jailLink)))
                    {
                        if (entityPlayer.inventory.getCurrentItem().hasTagCompound())
                        {
                            if (entityPlayer.inventory.getCurrentItem().stackTagCompound.hasKey("jailName"))
                            {
                                if (((TileEntityPrisonManager) te).setJailName(entityPlayer.inventory.getCurrentItem().stackTagCompound.getString("jailName")))
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
            }
        }
        if (entityPlayer.isSneaking()) return false;
        else
        {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityPrisonManager && !te.getWorldObj().isRemote)
            {
                String msg = StatCollector.translateToLocal("string.playerInJail");
                if (((TileEntityPrisonManager) te).hasJailedPlayer)
                    msg += ((TileEntityPrisonManager) te).playerName;
                else msg += StatCollector.translateToLocal("string.noOne");
                entityPlayer.addChatMessage(new ChatComponentText(msg));

                if (((TileEntityPrisonManager) te).hasJailedPlayer && ((TileEntityPrisonManager) te).reason != null && !((TileEntityPrisonManager) te).reason.isEmpty())
                {
                    entityPlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("string.reason" + ((TileEntityPrisonManager) te).reason)));
                }
            }
            return true;
        }
    }
}
