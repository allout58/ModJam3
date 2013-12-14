package allout58.mods.prisoncraft.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.PacketDispatcher;

import allout58.mods.prisoncraft.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

public class UnJailCommand implements ICommand
{
private List aliases;
    
    public UnJailCommand()
    {
        this.aliases=new ArrayList();
        this.aliases.add("unjail");
    }
    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "unjail";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "unjail <playername>";
    }

    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        if(astring.length==0)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addKey("string.invalidArgument"));
        }
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(icommandsender.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("No prison found in world"));
        }
        else
        {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[0]);
            boolean foundOpen = false;
            for (int i = 0; i < ws.getTesList().size(); i++)
            {
                TileEntityPrisonManager te = (TileEntityPrisonManager) ws.getTesList().get(i);
                if (te.hasJailedPlayer)
                {
                    if (player != null&&te.playerName.equalsIgnoreCase(astring[0]))
                    {
                        te.unjailPlayer();
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
//                        DataOutputStream outputStream = new DataOutputStream(bos);
//                        try {
//                                outputStream.writeUTF("unjail");
//                                //outputStream.writeUTF(player.username);
//                                outputStream.writeInt(te.xCoord);
//                                outputStream.writeInt(te.yCoord);
//                                outputStream.writeInt(te.zCoord);
//                        } catch (Exception ex) {
//                                ex.printStackTrace();
//                        }
//
//                        Packet250CustomPayload packet = new Packet250CustomPayload();
//                        packet.channel = ModConstants.PACKETCHANNEL;
//                        packet.data = bos.toByteArray();
//                        packet.length = bos.size();
//                        PacketDispatcher.sendPacketToAllAround(te.xCoord, te.yCoord, te.zCoord, 20, player.dimension, packet);
                        foundOpen=true;
                    }
                }
            }
            if (foundOpen) icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(icommandsender.getCommandSenderName() + " frees " + astring[0] + " to jail..."));
            else icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("No player with that name found"));
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
                if(un.toLowerCase().startsWith(ARG_LC))
                        MATCHES.add(un);
        return MATCHES.isEmpty() ? null : MATCHES;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return i==0;
    }
}
