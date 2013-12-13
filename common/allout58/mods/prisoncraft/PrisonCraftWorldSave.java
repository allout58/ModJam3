package allout58.mods.prisoncraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class PrisonCraftWorldSave extends WorldSavedData
{
    public static final String key = "prisoncraft";
    
    public static World worldObj;

    public List tesList = new ArrayList();

    public PrisonCraftWorldSave()
    {
        super(key);
    }

    public static PrisonCraftWorldSave forWorld(World world)
    {
        worldObj=world;
        // Retrieves the PrisonCraftWorldSave instance for the given world, creating it
        // if necessary
        MapStorage storage = world.perWorldStorage;
        PrisonCraftWorldSave result = (PrisonCraftWorldSave) storage.loadData(PrisonCraftWorldSave.class, key);
        if (result == null)
        {
            result = new PrisonCraftWorldSave();
            storage.setData(key, result);
        }
        return result;
    }

    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        if (tags.hasKey("NumTEs"))
        {
            int size = tags.getInteger("NumTEs");
            for(int i=0;i<size;i++)
            {
                NBTTagCompound t=tags.getCompoundTag(i+"");
                int x=t.getInteger("x");
                int y=t.getInteger("y");
                int z=t.getInteger("z");
                tesList.add(worldObj.getBlockTileEntity(x, y, z));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        tags.setInteger("NumTEs", tesList.size());
        for (int i = 0; i < tesList.size(); i++)
        {
            NBTTagCompound t = new NBTTagCompound();
            t.setInteger("x", ((TileEntity) tesList.get(i)).xCoord);
            t.setInteger("y", ((TileEntity) tesList.get(i)).yCoord);
            t.setInteger("z", ((TileEntity) tesList.get(i)).zCoord);
            tags.setCompoundTag(i + "", t);// HACKY! :D
        }
    }

}
