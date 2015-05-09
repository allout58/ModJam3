package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.jail.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

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
        return "/prisonperms <add|remove> <playername> <permlevel>\n/prisonperms <reload|save>";
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
            if ("reload".toLowerCase().startsWith(ARG_LC))
            {
                MATCHES.add("reload");
            }
            if ("save".toLowerCase().startsWith(ARG_LC))
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
        if (astring.length == 3)
        {
            final List<String> MATCHES = new LinkedList<String>();
            final String ARG_LC = astring[2].toLowerCase();
            for (PermissionLevel name : PermissionLevel.values())
            {
                if (name == PermissionLevel.FinalWord) continue;// Cannot grant
                                                                // final word.
                if (name == PermissionLevel.Default) continue;// Shouldn't grant
                                                              // default
                if (name.toString().toLowerCase().startsWith(ARG_LC))
                {
                    MATCHES.add(name.toString());
                }
            }
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
    	IChatComponent invalidArg=new ChatComponentTranslation("string.invalidArgument");
    	invalidArg.getChatStyle().setColor(EnumChatFormatting.RED);
        if (astring.length < 1 || astring.length > 3)
        {
            icommandsender.addChatMessage(invalidArg);
        }
        else
        {
            ICommandSender server = MinecraftServer.getServer();
            if (astring[0].equalsIgnoreCase("add"))
            {
                if (astring.length != 3)
                {
                    icommandsender.addChatMessage(invalidArg);
                }
                EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(astring[1]); //getPlayerForUsername
                if (player != null)
                {
                    PermissionLevel pl = PermissionLevel.valueOf(astring[2]);
                    // Cannot assign FinalWord from commands
                    if (pl != PermissionLevel.FinalWord)
                    {
                        if (pl.getValue() <= JailPermissions.getInstance().getPlayerPermissionLevel(icommandsender).getValue())
                        {
                            if (JailPermissions.getInstance().addUserPlayer(astring[1], pl))
                            {
                                icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] "+astring[1] + " "+ StatCollector.translateToLocal("string.was")+" "+StatCollector.translateToLocal("string.add")+" "+StatCollector.translateToLocal("string.jailperms")));
                            	if (!icommandsender.getCommandSenderName().equalsIgnoreCase(server.getCommandSenderName()))
                                {
                                    server.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + astring[1] + " " + StatCollector.translateToLocal("string.was") + " " + StatCollector.translateToLocal("string.added") + " " + StatCollector.translateToLocal("string.jailperms")));
                                }
                                player.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.youare") + StatCollector.translateToLocal("string.added") + " " + StatCollector.translateToLocal("string.jailperms")));
                            }
                            else
                            {
                                icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() +astring[1] + " " + StatCollector.translateToLocal("string.alreadyperms") + " " + StatCollector.translateToLocal("string.added") + " " + StatCollector.translateToLocal("string.jailperms")));
                            }
                        }
                        else
                        {
                            icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] "  + EnumChatFormatting.RED.toString()  + StatCollector.translateToLocal("string.invalidperms")));
                        }
                    }
                }
            }
            else if (astring[0].equalsIgnoreCase("remove"))
            {
                if (astring.length != 2)
                {
                    icommandsender.addChatMessage(invalidArg);
                }
                EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(astring[1]); //getPlayerForUsername
                if (player != null)
                {
                    if (JailPermissions.getInstance().getPlayerPermissionLevel(player).getValue() < JailPermissions.getInstance().getPlayerPermissionLevel(icommandsender).getValue())
                    {
                        if (JailPermissions.getInstance().removeUserPlayer(astring[1]))
                        {
                            icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + astring[1] + " " + StatCollector.translateToLocal("string.was") + " " + StatCollector.translateToLocal("string.removed") + " " + StatCollector.translateToLocal("string.jailperms")));
                            if (!icommandsender.getCommandSenderName().equalsIgnoreCase(server.getCommandSenderName()))
                            {
                                server.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + astring[1] + " " + StatCollector.translateToLocal("string.was") + " " + StatCollector.translateToLocal("string.removed") + " " + StatCollector.translateToLocal("string.jailperms")));
                            }
                            player.addChatComponentMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.youare") + StatCollector.translateToLocal("string.removed") + " " + StatCollector.translateToLocal("string.jailperms")));
                        }
                        else
                        {
                            icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " +EnumChatFormatting.RED.toString() +astring[1] + " " + StatCollector.translateToLocal("string.alreadyperms")+ " " + StatCollector.translateToLocal("string.removed") + " " + StatCollector.translateToLocal("string.jailperms")));
                        }
                    }
                    else
                    {
                        icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidperms")));
                    }
                }
            }
            else if (astring[0].equalsIgnoreCase("reload"))
            {
                JailPermissions.getInstance().clear();
                JailPermissions.getInstance().load();
                icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.reloadperms")));
                if (!icommandsender.getCommandSenderName().equalsIgnoreCase(server.getCommandSenderName()))
                {
                    server.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.reloadperms")));
                }
            }
            else if (astring[0].equalsIgnoreCase("save"))
            {
                JailPermissions.getInstance().save();
                icommandsender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.saveperms")));
                if (!icommandsender.getCommandSenderName().equalsIgnoreCase(server.getCommandSenderName()))
                {
                    server.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.saveperms")));
                }
            }
            else icommandsender.addChatMessage(invalidArg);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        return JailPermissions.getInstance().playerCanUse(icommandsender, PermissionLevel.PermissionGiver);
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return true;
    }

}
