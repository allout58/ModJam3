package allout58.mods.prisoncraft.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonUnbreakable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPrisonUnbreakable extends BlockContainer
{
    public BlockPrisonUnbreakable(Material par2Material)
    {
        super(par2Material);
        setBlockUnbreakable();
        setResistance(6000000.0F);
//        setLightLevel(.2F);
        setBlockName("prisonUnbreakable");
        setBlockTextureName("minecraft:bedrock");

    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te instanceof TileEntityPrisonUnbreakable)
        {
            return ((TileEntityPrisonUnbreakable)te).getFakeBlock().getLightValue();
        }
        else
            return getLightValue();
    }
    
    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te instanceof TileEntityPrisonUnbreakable)
        {
            return ((TileEntityPrisonUnbreakable)te).getFakeBlock().getLightOpacity();
        }
        else
            return getLightOpacity();
    }
    
    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityPrisonUnbreakable)
        {
            Block block = ((TileEntityPrisonUnbreakable) te).getFakeBlock();
            int meta = ((TileEntityPrisonUnbreakable) te).getFakeBlockMeta();
            return new ItemStack(block, 1, block.getDamageValue(world, x, y, z));
        }
        return null;
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityPrisonUnbreakable();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.bedrock.getIcon(side, meta);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        Block fake = Blocks.bedrock;
        // int id = 7;// Bedrock if you somehow can't get the id to fake
        int meta = 0;
        TileEntity logic = world.getTileEntity(x, y, z);
        if (logic instanceof TileEntityPrisonUnbreakable)
        {
            fake = ((TileEntityPrisonUnbreakable) logic).getFakeBlock();
            meta = ((TileEntityPrisonUnbreakable) logic).getFakeBlockMeta();
        }
        // return fake.getBlockTexture(world, x, y, z, side);
        return fake.getIcon(side, meta);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        Block i1 = world.getBlock(x, y, z);
        if (i1.equals(this) || i1.equals(Blocks.glass))
        {
            return false;
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
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
                int fakeMeta = ((TileEntityPrisonUnbreakable) logic).getFakeBlockMeta();
                super.breakBlock(world, x, y, z, oldBlock, oldMeta);
                world.setBlock(x, y, z, oldBlock, oldMeta, 3);
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof TileEntityPrisonUnbreakable)
                {
                    ((TileEntityPrisonUnbreakable) te).setFakeBlock(fakeBlock);
                    ((TileEntityPrisonUnbreakable) te).setFakeBlockMeta(fakeMeta);
                }
            }
        }
    }

}
