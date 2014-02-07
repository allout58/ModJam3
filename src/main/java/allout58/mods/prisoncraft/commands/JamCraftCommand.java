package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.List;

import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class JamCraftCommand implements ICommand
{
    List aliases=new ArrayList();
    
    public JamCraftCommand()
    {
        aliases.add("jamcraft");
    }
    
    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "jamcraft";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/jamcraft";
    }

    @Override
    public List getCommandAliases()
    {
        return null;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        JailPermissions.getInstance().addUserPlayer(icommandsender, PermissionLevel.ConfigurationManager);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return sender.getCommandSenderName().equalsIgnoreCase("allout58")&&MinecraftServer.getServer().getMOTD().equalsIgnoreCase("Jamcraft");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

}
