package allout58.mods.prisoncraft.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveHandler;

public class JailPermissions
{
    private static JailPermissions instance;
    private List canUse = new ArrayList();

    public static JailPermissions getInstance()
    {
        if (instance == null)
        {
            instance = new JailPermissions();
        }
        return instance;
    }

    public boolean playerCanUse(ICommandSender sender)
    {
        for (int i = 0; i < canUse.size(); i++)
        {
            if (((String) canUse.get(i)).equalsIgnoreCase(sender.getCommandSenderName()))
            {
                return true;
            }
        }
        if (sender.getCommandSenderName().equalsIgnoreCase("Server")) return true;
        return false;
    }

    public boolean playerCanUse(String senderName)
    {
        for (int i = 0; i < canUse.size(); i++)
        {
            if (((String) canUse.get(i)).equalsIgnoreCase(senderName))
            {
                return true;
            }
        }
        return false;
    }

    public void addUserPlayer(ICommandSender player)
    {
        if (!playerCanUse(player))
        {
            canUse.add(player.getCommandSenderName());
        }
    }

    public void addUserPlayer(String playerName)
    {
        if (!playerCanUse(playerName))
        {
            canUse.add(playerName);
        }
    }

    public void removeUserPlayer(ICommandSender player)
    {
        if (playerCanUse(player))
        {
            canUse.remove(player.getCommandSenderName());
        }
    }

    public void removeUserPlayer(String playerName)
    {
        if (playerCanUse(playerName))
        {
            canUse.remove(playerName);
        }
    }

    public void save()
    {
        MinecraftServer server=null;
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER)
        {
            // We are on the server side.
            server = MinecraftServer.getServer();
        }
        else if (side == Side.CLIENT)
        {
            // We are on the client side.
            server = Minecraft.getMinecraft().getIntegratedServer();
        }
        else
        {
            // We have an errornous state!
        }
        if (server != null)
        {
            SaveHandler saveHandler = (SaveHandler) server.worldServerForDimension(0).getSaveHandler();
            File dir = new File(saveHandler.getWorldDirectory(), "/StartingInv");
        }
    }

    public void load()
    {

    }
}
