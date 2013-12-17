package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import allout58.mods.prisoncraft.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.constants.ModConstants;
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
        return "/prisonperms <add|remove|reload|save> <playername>";
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
            if("reload".toLowerCase().startsWith(ARG_LC))
            {
                MATCHES.add("reload");
            }
            if("save".toLowerCase().startsWith(ARG_LC))
            {
                MATCHES.add("save");
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
        if (astring.length < 1 || astring.length > 2)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
        }
        else
        {
            ICommandSender server=MinecraftServer.getServer();
            if (astring[0].equalsIgnoreCase("add"))
            {
                if (astring.length != 2)
                {
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
                }
                EntityPlayer player=MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[1]);
                if (player != null)
                {
                    if (JailPermissions.getInstance().addUserPlayer(astring[1]))
                    {
                        icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addText(astring[1] + " ").addKey("string.was").addText(" ").addKey("string.added").addText(" ").addKey("string.jailperms"));
                        server.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addText(astring[1] + " ").addKey("string.was").addText(" ").addKey("string.added").addText(" ").addKey("string.jailperms"));
                        player.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addKey("string.youare").addKey("string.added").addText(" ").addKey("string.jailperms"));
                    }
                    else
                    {
                        icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addText(astring[1] + " ").addKey("string.alreadyperms").addText(" ").addKey("string.added").addText(" ").addKey("string.jailperms"));
                    }
                }
            }
            else if (astring[0].equalsIgnoreCase("remove"))
            {
                if (astring.length != 2)
                {
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
                }
                EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[1]);
                if (player != null)
                {
                    if (JailPermissions.getInstance().removeUserPlayer(astring[1]))
                    {
                        icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addText(astring[1] + " ").addKey("string.was").addText(" ").addKey("string.removed").addText(" ").addKey("string.jailperms"));
                        server.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addText(astring[1] + " ").addKey("string.was").addText(" ").addKey("string.removed").addText(" ").addKey("string.jailperms"));
                        player.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addKey("string.youare").addKey("string.removed").addText(" ").addKey("string.jailperms"));
                    }
                    else
                    {
                        icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addText(astring[1] + " ").addKey("string.alreadyperms").addText(" ").addKey("string.removed").addText(" ").addKey("string.jailperms"));

                    }
                }
            }
            else if(astring[0].equalsIgnoreCase("reload"))
            {
                JailPermissions.getInstance().clear();
                JailPermissions.getInstance().load();
                icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addKey("string.reloadperms"));
                server.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addKey("string.reloadperms"));
            }
            else if(astring[0].equalsIgnoreCase("save"))
            {
                JailPermissions.getInstance().save();
                icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addKey("string.saveperms"));
                server.sendChatToPlayer(new ChatMessageComponent().addText("["+ModConstants.NAME+"]").addKey("string.saveperms"));
            }
            else icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        //In my perms list and OP or server
        return JailPermissions.getInstance().playerCanUse(icommandsender)&&(MinecraftServer.getServer().getConfigurationManager().getOps().contains(icommandsender.getCommandSenderName().toLowerCase()) || icommandsender.getCommandSenderName().equalsIgnoreCase("Server"));
        // return true;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return true;
    }

}
