package allout58.mods.prisoncraft.fakeworld;

import allout58.mods.prisoncraft.PrisonCraft;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 5/3/2014.
 */
public class FakeWorldProvider
{
    /*private static Map<Integer, FakeWorld> fakeWorlds = new HashMap<Integer, FakeWorld>();

    public static void initFakeWorlds(MinecraftServer server)
    {
        //Currently, only add vanilla biomes
        FakeWorld nether = new FakeWorld(server.worldServerForDimension(-1));
        FakeWorld overworld = new FakeWorld(server.worldServerForDimension(0));
        FakeWorld end = new FakeWorld(server.worldServerForDimension(1));
        fakeWorlds.put(-1, nether);
        fakeWorlds.put(0, overworld);
        fakeWorlds.put(1, end);
    }

    public static FakeWorld getWorldFromDimension(int id)
    {
        FakeWorld ret=fakeWorlds.get(id);
        if(ret==null)
        {
            PrisonCraft.logger.error("Curently only vanilla biomes are supported. Sorry!");
            return null;
        }
        return ret;
    }*/
}
