package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import allout58.mods.prisoncraft.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

public class ChangeJailPermsCommand implements ICommand
{
    private List aliases;

    public ChangeJailPermsCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("prisonperms");
        this.aliases.add("pp");
    }

    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "prisonperms";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/prisonperms [add|remove] <playername>";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        if (astring.length == 1)
        {
            final List<String> MATCHES = new LinkedList<String>();
            final String ARG_LC = astring[0].toLowerCase();
            if ("add".toLowerCase().startsWith(ARG_LC))
            {
                MATCHES.add("add");
            }
            if ("remove".toLowerCase().startsWith(ARG_LC))
            {
                MATCHES.add("remove");
            }
            return MATCHES.isEmpty() ? null : MATCHES;
        }
        if (astring.length == 2)
        {
            final List<String> MATCHES = new LinkedList<String>();
            final String ARG_LC = astring[1].toLowerCase();
            for (String un : MinecraftServer.getServer().getAllUsernames())
                if (un.toLowerCase().startsWith(ARG_LC)) MATCHES.add(un);
            return MATCHES.isEmpty() ? null : MATCHES;
        }
        return null;
    }

    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        if (astring.length != 2)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
        }
        else
        {
            if (astring[0].equalsIgnoreCase("add"))
            {
                if (MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[1]) != null)
                {
                    JailPermissions.getInstance().addUserPlayer(astring[1]);
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(astring[1] + " ").addKey("string.was").addText(" ").addKey("string.added").addKey("string.jailperms"));
                }
            }
            else if (astring[0].equalsIgnoreCase("remove"))
            {
                if (MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[1]) != null)
                {
                    JailPermissions.getInstance().removeUserPlayer(astring[1]);
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(astring[1] + " ").addKey("string.was").addText(" ").addKey("string.removed").addKey("string.jailperms"));
                }
            }
            else icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        // return JailPermissions.getInstance().playerCanUse(icommandsender);
        return true;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return true;
    }

}
