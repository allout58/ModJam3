package allout58.mods.prisoncraft.jail;

import java.util.ArrayList;
import java.util.List;

import allout58.mods.prisoncraft.tileentities.TileEntityJailView;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class PrisonCraftWorldSave extends WorldSavedData
{
    public static final String key = "prisoncraft";

    public static World worldObj;

    private List<JailManRef> tesList = new ArrayList<JailManRef>();
    public List<String> jails = new ArrayList<String>();
    public List<JailedPersonData> people = new ArrayList<JailedPersonData>();

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

    public void addJailName(String name)
    {
        if (!jails.contains(name))
        {
            jails.add(name);
        }
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
        people.clear();
//        JailedPersonData jT = new JailedPersonData();
//        jT.coord = new int[] { 0, 0, 0 };
//        jT.name = "abc1";
//        jT.reason = ":P";
//        jT.time = 200;
//        jT.jail = "MAIN";
//        people.add(jT);
//
//        JailedPersonData j2 = new JailedPersonData();
//        j2.coord = new int[] { 0, 0, 0 };
//        j2.name = "infiniteMiner";
//        j2.reason = "Because forever";
//        j2.time = -1;
//        j2.jail = "M1";
//        people.add(j2);

        if (tags.hasKey("NumTEs"))
        {
            int size = tags.getInteger("NumTEs");
            for (int i = 0; i < size; i++)
            {
                NBTTagCompound t = tags.getCompoundTag(i + "");
                JailManRef ref = new JailManRef();
                ref.coord = t.getIntArray("coord");
                TileEntity te = worldObj.getBlockTileEntity(ref.coord[0], ref.coord[1], ref.coord[2]);
                if (te instanceof TileEntityPrisonManager)
                {
                    TileEntityPrisonManager tp = (TileEntityPrisonManager) te;
                    JailedPersonData j = new JailedPersonData();
                    j.coord = ref.coord;
                    j.name = tp.playerName;
                    j.reason = tp.reason;
                    j.time = tp.secsLeftJailTime;
                    j.jail = tp.jailname;
                    if (j.name != "")
                    {
                        people.add(j);
                    }
                }
                if (t.hasKey("jailname"))
                {
                    ref.jailName = t.getString("jailname");
                    addJailName(ref.jailName);
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
