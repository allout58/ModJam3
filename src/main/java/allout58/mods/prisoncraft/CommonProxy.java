package allout58.mods.prisoncraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import allout58.mods.prisoncraft.client.gui.GuiBanHammer;
import allout58.mods.prisoncraft.constants.GuiIDs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler
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
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == GuiIDs.BANHAMMER_GUI)
        {
//            AssemblerLogic logic = (AssemblerLogic) world.getTileEntity(x, y, z);
//            return new ContainerRocketAssembler(player.inventory, logic);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == GuiIDs.BANHAMMER_GUI)
        {
            ItemStack is=player.getCurrentEquippedItem();
            return new GuiBanHammer(is);
        }
        return null;
    }

}
