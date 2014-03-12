package allout58.mods.prisoncraft;

import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy
{

    public void registerRenderers()
    {
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
