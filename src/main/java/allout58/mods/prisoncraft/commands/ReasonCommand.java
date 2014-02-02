package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

public class ReasonCommand implements ICommand
{

    private List aliases;
    
    public ReasonCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("reason");
    }
    
    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "reason";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/reason <playername> <reason>";
    }

    @Override
    public List getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        if(astring.length<2)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.invalidArgument"));
        }
        else
        {
            String reason="";
            for(int i=1;i<astring.length;i++)
            {
                reason+=astring[i]+" ";
            }
            JailMan.getInstance().TrySetReason(astring[0], icommandsender, reason);
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        return JailPermissions.getInstance().playerCanUse(icommandsender, PermissionLevel.Jailer);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        final List<String> MATCHES = new LinkedList<String>();
        final String ARG_LC = astring[astring.length - 1].toLowerCase();
        if (astring.length == 1)
        {
            for (String un : MinecraftServer.getServer().getAllUsernames())
                if (un.toLowerCase().startsWith(ARG_LC)) MATCHES.add(un);
        }
        return MATCHES.isEmpty() ? null : MATCHES;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

}
