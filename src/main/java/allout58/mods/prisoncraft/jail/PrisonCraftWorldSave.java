package allout58.mods.prisoncraft.jail;

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

    private List<JailManRef> tesList = new ArrayList<JailManRef>(); // int[]
                                                                    // only!

    public PrisonCraftWorldSave()
    {
        super(key);
    }

    public PrisonCraftWorldSave(String keyIn)
    {
        super(PrisonCraftWorldSave.key);
    }

    public List<JailManRef> getTesList()
    {
        this.markDirty();
        return tesList;
    }

    public static PrisonCraftWorldSave forWorld(World world)
    {
        worldObj = world;
        // Retrieves the PrisonCraftWorldSave instance for the given world,
        // creating it if necessary
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
            for (int i = 0; i < size; i++)
            {
                NBTTagCompound t = tags.getCompoundTag(i + "");
                JailManRef ref = new JailManRef();
                ref.coord = t.getIntArray("coord");
                if (t.hasKey("jailname"))
                {
                    ref.jailName = t.getString("jailname");
                    tesList.add(ref);
                }
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
            t.setString("jailname", tesList.get(i).jailName);
            t.setIntArray("coord", tesList.get(i).coord);
            tags.setCompoundTag(i + "", t);// HACKY! :D
        }
    }

}
