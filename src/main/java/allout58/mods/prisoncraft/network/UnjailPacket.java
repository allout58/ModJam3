package allout58.mods.prisoncraft.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;
import io.netty.buffer.ByteBuf;

public class UnjailPacket implements IPacket
{
    private String playerToJail = "";
    private String jailer = "";

    public UnjailPacket()
    {
    }

    public UnjailPacket(String playerToJail, String jailer)
    {
        this.playerToJail = playerToJail;
        this.jailer = jailer;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        this.playerToJail = NetworkUtils.readString(bytes);
        this.jailer = NetworkUtils.readString(bytes);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        NetworkUtils.writeString(bytes, this.playerToJail);
        NetworkUtils.writeString(bytes, this.jailer);
    }

    
    @Override
    public void postProcess()
    {
        if (JailPermissions.getInstance().playerCanUse(jailer, PermissionLevel.Jailer))
        {
            JailMan.getInstance().TryUnjailPlayer(playerToJail, jailer);
        }
        else
        {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(jailer);
            if (player != null)
            {
                player.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidperms.tool")));
            }
        }
    }
}
