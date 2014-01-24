package allout58.mods.prisoncraft.client;

import java.util.Iterator;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import allout58.mods.prisoncraft.CommonProxy;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {

    }
    
    @Override
    public void loadConfig(FMLPreInitializationEvent e)
    {
    }
    

    // NEI Compat
    public void doNEICheck(int id)
    {
        if (Minecraft.getMinecraft().thePlayer != null)
        {
            Iterator modsIT = Loader.instance().getModList().iterator();
            ModContainer modc;
            while (modsIT.hasNext())
            {
                modc = (ModContainer) modsIT.next();
                if ("Not Enough Items".equals(modc.getName().trim()))
                {
                    codechicken.nei.api.API.hideItem(id);
                    return;
                }
            }
        }
    }
}
