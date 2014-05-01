package allout58.mods.prisoncraft.blocks;

import allout58.mods.prisoncraft.tileentities.TileEntityPrisonUnbreakable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by James Hollowell on 4/30/2014.
 */
public class BlockPrisonUnbreakablePaneStained extends BlockStainedGlassPane
        implements ITileEntityProvider, IPrisonCraftBlock
{
    public BlockPrisonUnbreakablePaneStained()
    {
        super();
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setBlockName("prisonUnbreakablePaneStained");
        setBlockTextureName("glass");
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityPrisonUnbreakable)
        {
            Block block = ((TileEntityPrisonUnbreakable) te).getFakeBlock();
            int meta = ((TileEntityPrisonUnbreakable) te).getFakeBlockMeta();
            return new ItemStack(block, 1, meta);
        }
        return null;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityPrisonUnbreakable)
        {
            return ((TileEntityPrisonUnbreakable) te).getFakeBlock().getLightValue();
        }
        else
            return getLightValue();
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityPrisonUnbreakable)
        {
            return ((TileEntityPrisonUnbreakable) te).getFakeBlock().getLightOpacity();
        }
        else
            return getLightOpacity();
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean canPaneConnectTo(IBlockAccess access, int x, int y, int z, ForgeDirection dir)
    {
        TileEntity te = access.getTileEntity(x, y, z);
        Block block = null;
        if (te instanceof TileEntityPrisonUnbreakable)
        {
            block = ((TileEntityPrisonUnbreakable) te).getFakeBlock();
        }
        return super.canPaneConnectTo(access, x, y, z, dir) || access.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == block;
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta)
    {
        TileEntity logic = world.getTileEntity(x, y, z);
        if (logic instanceof TileEntityPrisonUnbreakable)
        {
            if (((TileEntityPrisonUnbreakable) logic).canDestroy())
            {
                super.breakBlock(world, x, y, z, oldBlock, oldMeta);
            }
            else
            {
                Block fakeBlock = ((TileEntityPrisonUnbreakable) logic).getFakeBlock();
                super.breakBlock(world, x, y, z, oldBlock, oldMeta);
                world.setBlock(x, y, z, oldBlock, oldMeta, 3);
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof TileEntityPrisonUnbreakable)
                {
                    ((TileEntityPrisonUnbreakable) te).setFakeBlock(fakeBlock);
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityPrisonUnbreakable();
    }
}
