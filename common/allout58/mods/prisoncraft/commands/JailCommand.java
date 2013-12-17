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
//        return "/jail <playername> [time]";
        return "/jail <playername>";
    }

    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
//        if (astring.length < 1 || astring.length > 2)
        if(astring.length!=1)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
        }
        else
        {
            PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(icommandsender.getEntityWorld());
            if (ws.getTesList().size() == 0)
            {
                icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.nojail"));
            }
            else
            {
                EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[0]);
                boolean foundOpen = false;
                for (int i = 0; i < ws.getTesList().size(); i++)
                {
                    TileEntityPrisonManager te = (TileEntityPrisonManager) ws.getTesList().get(i);
                    if (!te.hasJailedPlayer)
                    {
                        if (player != null)
                        {
                            if (astring.length == 1)
                            {
//                                te.jailPlayer(player, -1);
                                if(te.jailPlayer(player))foundOpen = true;
                                else 
                                {
                                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.playeralreadyjailed"));
                                    return;
                                }
                            }
//                            if (astring.length == 2)
//                            {
//                                te.jailPlayer(player, Double.parseDouble(astring[1]));
//                            }
                            
                        }
                        else icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.noplayerfound"));
                    }
                }
                if (foundOpen)
                {
                    icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addText(icommandsender.getCommandSenderName()).addKey("string.sends").addText(astring[0]).addKey("string.tojail"));
                    MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addText(icommandsender.getCommandSenderName()).addKey("string.sends").addText(astring[0]).addKey("string.tojail"));
                }
                else icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.noopenjails"));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        return JailPermissions.getInstance().playerCanUse(icommandsender);
        // return true;
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
        // return i == 0;
        return true;
    }

}
