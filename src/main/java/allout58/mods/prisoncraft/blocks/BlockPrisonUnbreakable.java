package allout58.mods.prisoncraft.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonUnbreakable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPrisonUnbreakable extends BlockContainer
{
    public BlockPrisonUnbreakable(int par1, Material par2Material)
    {
        super(par1, par2Material);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setLightValue(.2F);
        setUnlocalizedName("prisonUnbreakable");
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityPrisonUnbreakable();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return Block.bedrock.getIcon(side, meta);
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
        int id = 7;// Bedrock if you somehow can't get the id to fake
        int meta = 0;
        TileEntity logic = world.getBlockTileEntity(x, y, z);
        if (logic instanceof TileEntityPrisonUnbreakable)
        {
            id = ((TileEntityPrisonUnbreakable) logic).getFakeBlockID();
            meta = ((TileEntityPrisonUnbreakable) logic).getFakeBlockMeta();
        }
        Block fake = Block.blocksList[id];
        // return fake.getBlockTexture(world, x, y, z, side);
        return fake.getIcon(id, meta);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        int i1 = world.getBlockId(x, y, z);
        if (i1 == this.blockID || i1 == Block.glass.blockID)
        {
            return false;
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int oldID, int oldMeta)
    {
        TileEntity logic = world.getBlockTileEntity(x, y, z);
        if (logic instanceof TileEntityPrisonUnbreakable)
        {
            if (((TileEntityPrisonUnbreakable) logic).canDestroy())
            {
                super.breakBlock(world, x, y, z, oldID, oldMeta);
            }
            else
            {
                int fakeID = ((TileEntityPrisonUnbreakable) logic).getFakeBlockID();
                int fakeMeta = ((TileEntityPrisonUnbreakable) logic).getFakeBlockMeta();
                super.breakBlock(world, x, y, z, oldID, oldMeta);
                world.setBlock(x, y, z, oldID, oldMeta, 3);
                TileEntity te = world.getBlockTileEntity(x, y, z);
                if (te instanceof TileEntityPrisonUnbreakable)
                {
                    ((TileEntityPrisonUnbreakable) te).setFakeBlockID(fakeID);
                    ((TileEntityPrisonUnbreakable) te).setFakeBlockMeta(fakeMeta);
                }
            }
        }
    }

}
