package allout58.mods.prisoncraft.jail;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.config.Config;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.network.PacketHandler;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

public class JailMan
{
    private boolean initialized = false;
    private File logFile;
    private static JailMan instance;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static JailMan getInstance()
    {
        if (instance == null)
        {
            instance = new JailMan();
        }
        return instance;
    }

    // Recording
    public void initializeRecorder(File f)
    {
        logFile = f;
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
                FileWriter w = new FileWriter(logFile);
                w.write("DateTime,Jailed,Jailer,Length\n");
                w.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        initialized = true;
    }

    private void addEntry(String playerName, String jailerName, double time) throws IOException
    {
        if (initialized)
        {
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(this.dateFormat.format(Long.valueOf(new Date().getTime())) + "," + playerName + "," + jailerName + "," + String.valueOf(time) + "\n");
            writer.close();
        }
        else
        {
            PrisonCraft.logger.severe("JailMan Record keeper not initialized before use.");
        }
    }

    // Jailing
    public boolean TryJailPlayer(EntityPlayer player, ICommandSender jailer, String jailName, double time)
    {
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.nojail"));
            return false;
        }
        else
        {
            boolean foundOpen = false;
            boolean foundJail = false;
            if (ws.jails.contains(jailName))
            {
                for (int i = 0; i < ws.getTesList().size(); i++)
                {
                    JailManRef ref = ws.getTesList().get(i);
                    int coord[] = ref.coord;
                    TileEntity te = jailer.getEntityWorld().getBlockTileEntity(coord[0], coord[1], coord[2]);
                    if (te instanceof TileEntityPrisonManager)
                    {
                        if (ref.jailName.equalsIgnoreCase(jailName) || ref.jailName == "")
                        {
                            foundJail = true;
                            if (!((TileEntityPrisonManager) te).hasJailedPlayer)
                            {
                                if (player != null)
                                {
                                    if (((TileEntityPrisonManager) te).jailPlayer(player, time))
                                    { 
                                        foundOpen = true;
                                        break;
                                    }
                                    else
                                    {
                                        jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.playeralreadyjailed"));
                                        return false;
                                    }

                                }
                                else
                                {
                                    jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.noplayerfound"));
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            if (!foundJail)
            {
                jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.jailnotfound"));
                return false;
            }
            if (foundOpen)
            {
                if (Config.logJailing)
                {
                    try
                    {
                        this.addEntry(player.username, jailer.getCommandSenderName(), time);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addText(jailer.getCommandSenderName()).addKey("string.sends").addText(player.username).addKey("string.tojail"));
                if (!jailer.getCommandSenderName().equalsIgnoreCase(MinecraftServer.getServer().getCommandSenderName()))
                {
                    MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addText(jailer.getCommandSenderName()).addKey("string.sends").addText(player.username).addKey("string.tojail"));
                }
                return true;
            }
            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.noopenjails"));
            return false;
        }
    }

    public boolean TryJailPlayer(String playerName, ICommandSender jailer, String jailName, double time)
    {
        return TryJailPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerName), jailer, jailName, time);
    }

    /**
     * playerJailerName must be of a player, not Server or other
     */
    public boolean TryJailPlayer(String playerName, String playerJailerName, String jailName, double time)
    {
        return TryJailPlayer(playerName, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerJailerName), jailName, time);
    }

    // Unjailing
    public boolean TryUnjailPlayer(EntityPlayer player, ICommandSender jailer)
    {
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.nojails"));
            return false;
        }
        else
        {
            boolean foundOpen = false;
            for (int i = 0; i < ws.getTesList().size(); i++)
            {
                int coord[] = ws.getTesList().get(i).coord;
                TileEntity te = jailer.getEntityWorld().getBlockTileEntity(coord[0], coord[1], coord[2]);
                if (te instanceof TileEntityPrisonManager)
                {
                    if (((TileEntityPrisonManager) te).hasJailedPlayer)
                    {
                        if (player != null && ((TileEntityPrisonManager) te).playerName.equalsIgnoreCase(player.username))
                        {
                            if (((TileEntityPrisonManager) te).unjailPlayer()) foundOpen = true;
                            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.playeroffline"));
                        }
                        else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.playeroffline"));
                    }
                }
            }
            if (foundOpen)
            {
                jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addText(jailer.getCommandSenderName()).addKey("string.frees").addText(player.username).addKey("string.fromjail"));
                if (!jailer.getCommandSenderName().equalsIgnoreCase(MinecraftServer.getServer().getCommandSenderName()))
                {
                    MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addText(jailer.getCommandSenderName()).addKey("string.frees").addText(player.username).addKey("string.fromjail"));
                }
                return true;
            }
            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.noplayerfound"));
            return false;
        }
    }

    public boolean TryUnjailPlayer(String playerName, ICommandSender jailer)
    {
        return TryUnjailPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerName), jailer);
    }

    /**
     * playerJailerName must be of a player, not Server or other
     */
    public boolean TryUnjailPlayer(String playerName, String jailerName)
    {
        return TryUnjailPlayer(playerName, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(jailerName));
    }

    public boolean TrySetReason(String player, ICommandSender jailer, String reason)
    {
        List<JailManRef> refs = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld()).getTesList();
        for (int i = 0; i < refs.size(); i++)
        {
            int[] coord = refs.get(i).coord;
            TileEntity te = jailer.getEntityWorld().getBlockTileEntity(coord[0], coord[1], coord[2]);
            if (te instanceof TileEntityPrisonManager)
            {
                if (((TileEntityPrisonManager) te).hasJailedPlayer)
                {
                    if (((TileEntityPrisonManager) te).playerName.equalsIgnoreCase(player))
                    {
                        ((TileEntityPrisonManager) te).setReason(reason);
                        jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addText(jailer.getCommandSenderName()).addKey("string.setsreason").addText(player).addKey("string.to").addText('"'+reason+'"'));
                        if (!jailer.getCommandSenderName().equalsIgnoreCase(MinecraftServer.getServer().getCommandSenderName()))
                        {
                            MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addText(jailer.getCommandSenderName()).addKey("string.setsreason").addText(player).addKey("string.to").addText('"'+reason+'"'));
                        }
                        return true;
                    }
                    else
                    {
                        jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addText(EnumChatFormatting.RED.toString()).addKey("string.noplayerfound"));
                        return false;
                    }
                }
            }
        }
        jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addText(EnumChatFormatting.RED.toString()).addKey("string.jailnotfound"));
        return false;
    }
}
