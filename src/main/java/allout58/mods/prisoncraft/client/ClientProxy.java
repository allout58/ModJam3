package allout58.mods.prisoncraft.client;

import java.util.Iterator;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.client.render.JailViewHUDRenderer;
import allout58.mods.prisoncraft.tileentities.TileEntityJailView;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityJailView.class,new JailViewHUDRenderer());
    }
     
    // NEI Compat
//    public void doNEICheck(int id)
//    {
//        if (Minecraft.getMinecraft().thePlayer != null)
//        {
//            Iterator modsIT = Loader.instance().getModList().iterator();
//            ModContainer modc;
//            while (modsIT.hasNext())
//            {
//                modc = (ModContainer) modsIT.next();
//                if ("Not Enough Items".equals(modc.getName().trim()))
//                {
//                    codechicken.nei.api.API.hideItem(id);
//                    return;
//                }
//            }
//        }
//    }
}
