package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.jail.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

public class JailCommand implements ICommand
{
    private List aliases;

    public JailCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("jail");
    }

    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "jail";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/jail <playername> <jailname> [time]";
    }

    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        if (astring.length < 1 || astring.length > 3)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.invalidArgument"));
        }
        else
        {
            if (astring.length == 2)
            {
                JailMan.getInstance().TryJailPlayer(astring[0], icommandsender, astring[1], -1);
            }
            if (astring.length == 3)
            {
                try
                {
                    double t = Double.parseDouble(astring[2]);
                    JailMan.getInstance().TryJailPlayer(astring[0], icommandsender, astring[1], t);
                }
                catch (NumberFormatException e)
                {
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.nan"));
                }
            }
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
        if (astring.length == 2)
        {

        }
        return MATCHES.isEmpty() ? null : MATCHES;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        // return i == 0;
        return true;
    }

}
