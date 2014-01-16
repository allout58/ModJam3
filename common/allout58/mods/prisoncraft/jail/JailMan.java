package allout58.mods.prisoncraft.jail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Logger;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.config.Config;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

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

    public boolean TryJailPlayer(EntityPlayer player, ICommandSender jailer, double time)
    {
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.nojail"));
            return false;
        }
        else
        {
            boolean foundOpen = false;
            for (int i = 0; i < ws.getTesList().size(); i++)
            {
                int coord[] = (int[]) ws.getTesList().get(i);
                TileEntity te = jailer.getEntityWorld().getBlockTileEntity(coord[0], coord[1], coord[2]);
                if (te instanceof TileEntityPrisonManager)
                {
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
                                jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.playeralreadyjailed"));
                                return false;
                            }

                        }
                        else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.noplayerfound"));
                    }
                }
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
            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.noopenjails"));
            return false;
        }
    }

    public boolean TryJailPlayer(String playerName, ICommandSender jailer, double time)
    {
        return TryJailPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerName), jailer, time);
    }

    /**
     * playerJailerName must be of a player, not Server or other
     */
    public boolean TryJailPlayer(String playerName, String playerJailerName, double time)
    {
        return TryJailPlayer(playerName, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerJailerName), time);
    }

    public boolean TryUnjailPlayer(EntityPlayer player, ICommandSender jailer)
    {
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.nojails"));
            return false;
        }
        else
        {
            boolean foundOpen = false;
            for (int i = 0; i < ws.getTesList().size(); i++)
            {
                int coord[] = (int[]) ws.getTesList().get(i);
                TileEntity te = jailer.getEntityWorld().getBlockTileEntity(coord[0], coord[1], coord[2]);
                if (te instanceof TileEntityPrisonManager)
                {
                    if (((TileEntityPrisonManager) te).hasJailedPlayer)
                    {
                        if (player != null && ((TileEntityPrisonManager) te).playerName.equalsIgnoreCase(player.username))
                        {
                            if (((TileEntityPrisonManager) te).unjailPlayer()) foundOpen = true;
                            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.playeroffline"));
                        }
                        else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.playeroffline"));
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
            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString()).addKey("string.noplayerfound"));
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
}
