package allout58.mods.prisoncraft.jail;

import allout58.mods.prisoncraft.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;

public class JailMan
{
    public static boolean TryJailPlayer(EntityPlayer player, ICommandSender jailer, double time)
    {
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.nojail"));
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
                                jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.playeralreadyjailed"));
                                return false;
                            }

                        }
                        else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.noplayerfound"));
                    }
                }
            }
            if (foundOpen)
            {
                jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addText(jailer.getCommandSenderName()).addKey("string.sends").addText(player.username).addKey("string.tojail"));
                if (!jailer.getCommandSenderName().equalsIgnoreCase(MinecraftServer.getServer().getCommandSenderName()))
                {
                    MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addText(jailer.getCommandSenderName()).addKey("string.sends").addText(player.username).addKey("string.tojail"));
                }
                return true;
            }
            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.noopenjails"));
            return false;
        }
    }

    public static boolean TryJailPlayer(String playerName, ICommandSender jailer, double time)
    {
        return JailMan.TryJailPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerName), jailer, time);
    }

    public static boolean TryUnjailPlayer(EntityPlayer player, ICommandSender jailer)
    {
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(jailer.getEntityWorld());
        if (ws.getTesList().size() == 0)
        {
            jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.nojails"));
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
                            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.playeroffline"));
                        }
                        else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.playeroffline"));
                    }
                }
            }
            if (foundOpen)
            {
                jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addText(jailer.getCommandSenderName()).addKey("string.frees").addText(player.username).addKey("string.fromjail"));
                if (!jailer.getCommandSenderName().equalsIgnoreCase(MinecraftServer.getServer().getCommandSenderName()))
                {
                    MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addText(jailer.getCommandSenderName()).addKey("string.frees").addText(player.username).addKey("string.fromjail"));
                }
                return true;
            }
            else jailer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.noplayerfound"));
            return false;
        }
    }

    public static boolean TryUnailPlayer(String playerName, ICommandSender jailer)
    {
        return JailMan.TryUnjailPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerName), jailer);
    }
}
