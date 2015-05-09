package allout58.mods.prisoncraft.jail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.config.Config;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.network.JVSendPersonPacket;
import allout58.mods.prisoncraft.network.NetworkUtils;
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
            PrisonCraft.logger.error("JailMan Record keeper not initialized before use.");
        }
    }

    // Jailing
    public boolean TryJailPlayer(EntityPlayer player, ICommandSender jailer, String jailName, double time)
    {
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.nojail")));
            return false;
        }
        else
        {
            boolean foundOpen = false;
            boolean foundJail = false;
            if (ws.jails.contains(jailName)||jailName=="")
            {
                for (int i = 0; i < ws.getTesList().size(); i++)
                {
                    JailManRef ref = ws.getTesList().get(i);
                    int coord[] = ref.coord;
                    TileEntity te = jailer.getEntityWorld().getTileEntity(coord[0], coord[1], coord[2]);
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
                                         
                                        PrisonCraft.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
                                        PrisonCraft.channels.get(Side.SERVER).writeOutbound(new JVSendPersonPacket(ws.people));
                                        
                                        break;
                                    }
                                    else
                                    {
                                        jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.playeralreadyjailed")));
                                        return false;
                                    }

                                }
                                else
                                {
                                    jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.noplayerfound")));
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            if (!foundJail)
            {
                jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.jailnotfound")));
                return false;
            }
            if (foundOpen)
            {
                if (Config.logJailing)
                {
                    try
                    {
                        this.addEntry(player.getDisplayName(), jailer.getCommandSenderName(), time);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + jailer.getCommandSenderName() + StatCollector.translateToLocal("string.sends") + player.getDisplayName() + StatCollector.translateToLocal("string.tojail")));
                if (!jailer.getCommandSenderName().equalsIgnoreCase(MinecraftServer.getServer().getCommandSenderName()))
                {
                    MinecraftServer.getServer().addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + jailer.getCommandSenderName() + StatCollector.translateToLocal("string.sends") + player.getDisplayName() + StatCollector.translateToLocal("string.tojail")));
                }
                return true;
            }
            else jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.noopenjails")));
            return false;
        }
    }

    public boolean TryJailPlayer(String playerName, ICommandSender jailer, String jailName, double time)
    {
        return TryJailPlayer(MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName), jailer, jailName, time);
    }

    /**
     * playerJailerName must be of a player, not Server or other
     */
    public boolean TryJailPlayer(String playerName, String playerJailerName, String jailName, double time)
    {
        return TryJailPlayer(playerName, MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerJailerName), jailName, time);
    }

    // Unjailing
    public boolean TryUnjailPlayer(EntityPlayer player, ICommandSender jailer)
    {
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.nojails")));
            return false;
        }
        else
        {
            boolean foundOpen = false;
            for (int i = 0; i < ws.getTesList().size(); i++)
            {
                int coord[] = ws.getTesList().get(i).coord;
                TileEntity te = jailer.getEntityWorld().getTileEntity(coord[0], coord[1], coord[2]);
                if (te instanceof TileEntityPrisonManager)
                {
                    if (((TileEntityPrisonManager) te).hasJailedPlayer)
                    {
                        if (player != null && ((TileEntityPrisonManager) te).playerName.equalsIgnoreCase(player.getDisplayName()))
                        {
                            if (((TileEntityPrisonManager) te).unjailPlayer()) foundOpen = true;
                            else jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.playeroffline")));
                        }
                        else jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.playeroffline")));
                    }
                }
            }
            if (foundOpen)
            {
                jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + jailer.getCommandSenderName() + StatCollector.translateToLocal("string.frees") + player.getDisplayName() + StatCollector.translateToLocal("string.fromjail")));
                if (!jailer.getCommandSenderName().equalsIgnoreCase(MinecraftServer.getServer().getCommandSenderName()))
                {
                    MinecraftServer.getServer().addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + jailer.getCommandSenderName() + StatCollector.translateToLocal("string.frees") + player.getDisplayName() + StatCollector.translateToLocal("string.fromjail")));
                }
                return true;
            }
            else jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.noplayerfound")));
            return false;
        }
    }

    public boolean TryUnjailPlayer(String playerName, ICommandSender jailer)
    {
        return TryUnjailPlayer(MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName), jailer);
    }

    /**
     * playerJailerName must be of a player, not Server or other
     */
    public boolean TryUnjailPlayer(String playerName, String jailerName)
    {
        return TryUnjailPlayer(playerName, MinecraftServer.getServer().getConfigurationManager().func_152612_a(jailerName));
    }

    public boolean TrySetReason(String player, ICommandSender jailer, String reason)
    {
        List<JailManRef> refs = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld()).getTesList();
        for (int i = 0; i < refs.size(); i++)
        {
            int[] coord = refs.get(i).coord;
            TileEntity te = jailer.getEntityWorld().getTileEntity(coord[0], coord[1], coord[2]);
            if (te instanceof TileEntityPrisonManager)
            {
                if (((TileEntityPrisonManager) te).hasJailedPlayer)
                {
                    if (((TileEntityPrisonManager) te).playerName.equalsIgnoreCase(player))
                    {
                        ((TileEntityPrisonManager) te).setReason(reason);
                        jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + jailer.getCommandSenderName() + StatCollector.translateToLocal("string.setsreason" + player) + StatCollector.translateToLocal("string.to" + '"' + reason + '"')));
                        if (!jailer.getCommandSenderName().equalsIgnoreCase(MinecraftServer.getServer().getCommandSenderName()))
                        {
                            MinecraftServer.getServer().addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + jailer.getCommandSenderName() + StatCollector.translateToLocal("string.setsreason" + player) + StatCollector.translateToLocal("string.to" + '"' + reason + '"')));
                        }
                        return true;
                    }
                    else
                    {
                        jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.noplayerfound")));
                        return false;
                    }
                }
            }
        }
        jailer.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.jailnotfound")));
        return false;
    }
}
