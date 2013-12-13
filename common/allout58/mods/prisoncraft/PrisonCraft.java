package allout58.mods.prisoncraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "prisoncraft", name = "Prison Craft", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class PrisonCraft
{
    @Instance("PrisonCraft")
    public static PrisonCraft instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    }
}
