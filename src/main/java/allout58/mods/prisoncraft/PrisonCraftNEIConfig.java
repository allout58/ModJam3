package allout58.mods.prisoncraft;

import java.lang.reflect.Method;

import allout58.mods.prisoncraft.config.Config;
import codechicken.nei.api.IConfigureNEI;

import com.google.common.base.Throwables;

public class PrisonCraftNEIConfig implements IConfigureNEI
{

    @Override
    public void loadConfig()
    {
        if (Config.prisonUnbreak > 0)
        {
            codechicken.nei.api.API.hideItem(Config.prisonUnbreak);
//            try
//            {
//                // I have no idea how to link with NEI API
//                Class<?> cls = Class.forName("codechicken.nei.api.API");
//                Method hide = cls.getMethod("hideItem", int.class);
//                hide.invoke(null, Config.prisonUnbreak);
//            }
//            catch (Exception e)
//            {
//                Throwables.propagate(e);
//            }
        }
    }

    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return "PrisonCraft";
    }

    @Override
    public String getVersion()
    {
        return "0.0.1";
    }

}
