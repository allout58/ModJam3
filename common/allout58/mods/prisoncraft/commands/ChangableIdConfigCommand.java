package allout58.mods.prisoncraft.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import allout58.mods.prisoncraft.config.ConfigChangableIDs;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.storage.SaveHandler;

public class ChangableIdConfigCommand implements ICommand
{
    private List aliases;

    public ChangableIdConfigCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("prisoncraftids");
        this.aliases.add("pcids");
    }

    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "prisoncraftids";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/pcids <add|remove> <id>\n/pcids <save|reload>";
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
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()+EnumChatFormatting.ITALIC.toString()).addKey("string.invalidArgument"));
        }
        else
        {
            if (astring.length == 1)
            {
                if (astring[0].equalsIgnoreCase("save"))
                {
                    ConfigChangableIDs.getInstance().save();
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.savedconfig"));
                    if (!icommandsender.getCommandSenderName().equalsIgnoreCase("server"))
                    {
                        MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.savedconfig"));
                    }

                }
                else if (astring[0].equalsIgnoreCase("reload"))
                {
                    SaveHandler saveHandler = (SaveHandler) MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler();
                    String fileName = saveHandler.getWorldDirectory().getAbsolutePath() + "/PCUnbreakableIDs.txt";
                    File f = new File(fileName);
                    ConfigChangableIDs.getInstance().load(f);
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.loadedconfig"));
                    if (!icommandsender.getCommandSenderName().equalsIgnoreCase("server"))
                    {
                        MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.loadedconfig"));
                    }
                }
                else
                {
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()+EnumChatFormatting.ITALIC.toString()).addKey("string.invalidArgument"));
                }
            }
            if (astring.length == 2)
            {
                if (astring[0].equalsIgnoreCase("add"))
                {
                    try
                    {
                        int integer = Integer.parseInt(astring[1]);
                        ConfigChangableIDs.getInstance().addID(integer);
                        icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idadded"));
                        if (!icommandsender.getCommandSenderName().equalsIgnoreCase("server"))
                        {
                            MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idadded"));
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()+EnumChatFormatting.ITALIC.toString()).addKey("string.nan"));
                    }
                }
                if (astring[0].equalsIgnoreCase("remove"))
                {
                    try
                    {
                        int integer = Integer.parseInt(astring[1]);
                        if (ConfigChangableIDs.getInstance().removeID(integer))
                        {
                            icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idremoved"));
                            if (!icommandsender.getCommandSenderName().equalsIgnoreCase("server"))
                            {
                                MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idremoved"));
                            }
                        }
                        else
                        {
                            icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] "+EnumChatFormatting.RED.toString()+EnumChatFormatting.ITALIC.toString()).addKey("string.idremoved.fail"));
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()+EnumChatFormatting.ITALIC.toString()).addKey("string.nan"));
                    }
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        return JailPermissions.getInstance().playerCanUse(icommandsender, PermissionLevel.ConfigurationManager);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        final List<String> MATCHES = new LinkedList<String>();
        final String ARG_LC = astring[0].toLowerCase();
        if ("add".startsWith(ARG_LC)) MATCHES.add("add");
        if ("remove".startsWith(ARG_LC)) MATCHES.add("remove");
        if ("save".startsWith(ARG_LC)) MATCHES.add("save");
        if ("reload".startsWith(ARG_LC)) MATCHES.add("reload");
        return MATCHES;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

}
