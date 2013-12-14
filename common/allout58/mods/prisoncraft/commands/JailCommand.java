package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.List;

import allout58.mods.prisoncraft.PrisonCraftWorldSave;
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
        if (astring.length == 0)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
        }
        else
        {
            PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(icommandsender.getEntityWorld());
            if (ws.getTesList().size() == 0)
            {
                icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("No prison found in world"));
            }
            else
            {
                boolean foundOpen = false;
                for (int i = 0; i < ws.getTesList().size(); i++)
                {
                    TileEntityPrisonManager te = (TileEntityPrisonManager) ws.getTesList().get(i);
                    if (!te.hasJailedPlayer)
                    {
                        EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[0]);
                        if (player != null)
                        {
                            te.jailPlayer(player);
                            foundOpen = true;
                        }
                    }
                }
                if (foundOpen) icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(icommandsender.getCommandSenderName() + " sends " + astring[0] + " to jail..."));
                else icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("No open jails. Build more!"));
            }
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
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        // return i == 0;
        return true;
    }

}
