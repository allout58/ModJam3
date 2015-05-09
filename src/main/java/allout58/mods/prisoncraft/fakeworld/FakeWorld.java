package allout58.mods.prisoncraft.fakeworld;

import allout58.mods.prisoncraft.tileentities.TileEntityPrisonUnbreakable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeWorld// extends World implements IBlockAccess
{
    /*
     * TODO: Save FakeCoords and FakeTES to disk
     */

    /*World realWorld;
    List<int[]> fakecoords = new ArrayList<int[]>();
    Map<Integer, TileEntity> fakeTEs = new HashMap<Integer, TileEntity>();

    public FakeWorld(World real)
    {
        super(real.getSaveHandler(), real.getWorldInfo().getWorldName(), real.provider, new WorldSettings(real.getWorldInfo()), new Profiler());
        realWorld = real;
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        if (isFakeBlockLoc(x, y, z))
        {
            return getFakeBlock(x, y, z);
        }
        else
        {
            return realWorld.getBlock(x, y, z);
        }
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z)
    {
        if (isFakeBlockLoc(x, y, z))
        {
            return getFakeTE(x, y, z);
        }
        else
        {
            return realWorld.getTileEntity(x, y, z);
        }
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int i, int i2, int i3, int i4)
    {
        return realWorld.getLightBrightnessForSkyBlocks(i, i2, i3, i4);
    }

    @Override
    public int getBlockMetadata(int x, int y, int z)
    {
        if (isFakeBlockLoc(x, y, z))
        {
            return getFakeMeta(x, y, z);
        }
        else
        {
            return realWorld.getBlockMetadata(x, y, z);
        }
    }

    @Override
    public boolean isAirBlock(int i, int i2, int i3)
    {
        return realWorld.isAirBlock(i, i2, i3);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int i, int i2)
    {
        return realWorld.getBiomeGenForCoords(i, i2);
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        //TODO: Auto-generated method stub
        return null;
    }

    @Override
    public int getHeight()
    {
        return realWorld.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache()
    {
        return realWorld.extendedLevelsInChunkCache();
    }

    @Override
    public Vec3Pool getWorldVec3Pool()
    {
        return realWorld.getWorldVec3Pool();
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int dir)
    {
        return getBlock(x, y, z).isProvidingStrongPower(this, x, y, z, dir);
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection forgeDirection, boolean b)
    {
        if (isFakeBlockLoc(x, y, z))
        {
            // pass this or realworld?
            return getFakeBlock(x, y, z).isSideSolid(this, x, y, z, forgeDirection);
        }
        else
        {
            return realWorld.isSideSolid(x, y, z, forgeDirection, b);
        }
    }

    public void registerFakeBlockLoc(int x, int y, int z)
    {
        if (!isFakeBlockLoc(x, y, z))
        {
            int coord[] = { x, y, z };
            fakecoords.add(coord);
        }
    }

    public void unregisterFakeBlockLoc(int x, int y, int z)
    {
        int fNDX = findFakeNDX(x, y, z);
        if (fNDX != -1)
        {
            fakeTEs.remove(fNDX);
            //FIXME: remove fake coords... can't right now because then coord NDX won't match fakeTE NDX
            //            fakecoords.remove(fNDX);
        }
    }

    public boolean isFakeBlockLoc(int x, int y, int z)
    {
        return findFakeNDX(x, y, z) != -1;
    }*/

    /**
     * WARNING: Must call {@link #registerFakeBlockLoc(int, int, int) registerFakeBlockLoc}
     * before calling this. Not doing so will return immediately.
     *
     * @param x   X coord
     * @param y   Y coord
     * @param z   Z coord
     * @param tag The result of the TE's writeToNBT()
     */
    /*public void registerFakeTileEntity(int x, int y, int z, NBTTagCompound tag)
    {
        int fNdx = findFakeNDX(x, y, z);
        if (fNdx == -1) return;
        TileEntity te = realWorld.getBlock(x, y, z).createTileEntity(realWorld, getBlockMetadata(x, y, z));
        te.readFromNBT(tag);
        te.setWorldObj(this);
        fakeTEs.put(fNdx, te);

    }

    protected int findFakeNDX(int x, int y, int z)
    {
        for (int i = 0; i < fakecoords.size(); i++)
        {
            int coord[] = fakecoords.get(i);
            if (x == coord[0] && y == coord[1] && z == coord[2])
            {
                return i;
            }
        }
        return -1;
    }

    protected Block getFakeBlock(int x, int y, int z)
    {
        TileEntity te = realWorld.getTileEntity(x, y, z);
        Block retBlock = null;
        if (te instanceof TileEntityPrisonUnbreakable)
        {
            retBlock = ((TileEntityPrisonUnbreakable) te).getFakeBlock();
        }
        return retBlock;
    }

    protected int getFakeMeta(int x, int y, int z)
    {
        TileEntity te = realWorld.getTileEntity(x, y, z);
        int meta = 0;
        if (te instanceof TileEntityPrisonUnbreakable)
        {
            meta = ((TileEntityPrisonUnbreakable) te).getFakeBlockMeta();
        }
        return meta;
    }

    protected TileEntity getFakeTE(int x, int y, int z)
    {
        int fNdx = findFakeNDX(x, y, z);
        if (fNdx == -1) return null;
        return fakeTEs.get(fNdx);
    }

    @Override
    public Entity getEntityByID(int var1)
    {
        return realWorld.getEntityByID(var1);
    }*/
}
