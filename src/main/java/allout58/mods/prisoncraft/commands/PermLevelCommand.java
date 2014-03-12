package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class PermLevelCommand implements ICommand
{
    private List aliases;

    public PermLevelCommand()
    {
        aliases = new ArrayList();
        aliases.add("pcpermlevel");
    }

    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "prisoncraftpermlevel";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/pcpermlevel [user]";
    }

    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        if (astring.length > 1)
        {
            icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString()  + StatCollector.translateToLocal("string.invalidArgument")));
        }
        else
        {
            PermissionLevel pl=PermissionLevel.Default;
            if (astring.length == 0)
            {
                pl = JailPermissions.getInstance().getPlayerPermissionLevel(icommandsender);
            }
            if (astring.length == 1)
            {
                pl = JailPermissions.getInstance().getPlayerPermissionLevel(astring[0]);
            }
            icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.permlevel") +pl.toString()));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        final List<String> MATCHES = new LinkedList<String>();
        final String ARG_LC = astring[0].toLowerCase();
        for (String un : MinecraftServer.getServer().getAllUsernames())
            if (un.toLowerCase().startsWith(ARG_LC)) MATCHES.add(un);
        return MATCHES.isEmpty() ? null : MATCHES;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return true;
    }

}
