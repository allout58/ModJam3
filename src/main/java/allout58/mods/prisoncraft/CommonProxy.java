package allout58.mods.prisoncraft;

import java.io.File;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.Configuration;

import org.lwjgl.input.Keyboard;

import allout58.mods.prisoncraft.config.ConfigServer;
import allout58.mods.prisoncraft.constants.ModConstants;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy
{

    public void registerRenderers()
    {
    }

    public void loadConfig(FMLPreInitializationEvent e)
    {
        ConfigServer.init(new Configuration(new File(e.getModConfigurationDirectory(),ModConstants.MODID+"-server.cfg")));
    }

    // From MachineMuse's PowerSuits mod
    public static boolean shouldAddAdditionalInfo()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                return true;
            }
        }
        return false;
    }

    public static Object additionalInfoInstructions()
    {
        return EnumChatFormatting.ITALIC.toString() + "Press " + EnumChatFormatting.AQUA.toString() + EnumChatFormatting.ITALIC.toString() + "<SHIFT>" + EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC.toString() + " for more information.";
    }

}
