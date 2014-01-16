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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.storage.SaveHandler;

public class PrisonCraftCommand implements ICommand
{

    private List aliases;

    public PrisonCraftCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("prisoncraft");
        this.aliases.add("pc");
    }

    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "prisoncraft";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        String use = "/prisoncraft configure\n";
        use += "/prisoncraft doneconfig";
        use += "/prisoncraft ids <add|remove> <id>\n";
        use += "/prisoncraft ids <save|reload>";
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
        // TODO Auto-generated method stub

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
        // always the last
        final String ARG_LC = astring[astring.length - 1].toLowerCase();
        if (astring.length == 1)
        {
            if ("ids".startsWith(ARG_LC)) MATCHES.add("ids");
            if ("configure".startsWith(ARG_LC)) MATCHES.add("ids");
        }
        if (astring.length == 2)
        {
            if (astring[0] == "ids")
            {
                if ("add".startsWith(ARG_LC)) MATCHES.add("add");
                if ("remove".startsWith(ARG_LC)) MATCHES.add("remove");
                if ("save".startsWith(ARG_LC)) MATCHES.add("save");
                if ("reload".startsWith(ARG_LC)) MATCHES.add("reload");
            }
        }
        return MATCHES;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

    private void processIds(ICommandSender sender, String[] astring)
    {
        if (astring.length < 1 || astring.length > 2)
        {
            sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.invalidArgument"));
        }
        else
        {
            if (astring.length == 1)
            {
                if (astring[0].equalsIgnoreCase("save"))
                {
                    ConfigChangableIDs.getInstance().save();
                    sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.savedconfig"));
                    if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
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
                    sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.loadedconfig"));
                    if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                    {
                        MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.loadedconfig"));
                    }
                }
                else
                {
                    sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.invalidArgument"));
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
                        sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idadded"));
                        if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                        {
                            MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idadded"));
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.nan"));
                    }
                }
                if (astring[0].equalsIgnoreCase("remove"))
                {
                    try
                    {
                        int integer = Integer.parseInt(astring[1]);
                        if (ConfigChangableIDs.getInstance().removeID(integer))
                        {
                            sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idremoved"));
                            if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                            {
                                MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idremoved"));
                            }
                        }
                        else
                        {
                            sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.idremoved.fail"));
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.nan"));
                    }
                }
            }
        }
    }

    private void processConfigure(ICommandSender sender, String[] astring)
    {
        if (!(sender instanceof EntityPlayer))
        {
            sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.mustbeplayer"));
            return;
        }
    }

    private void processDoneConfig(ICommandSender sender, String[] astring)
    {
        if (!(sender instanceof EntityPlayer))
        {
            sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.mustbeplayer"));
            return;
        }
    }

}
