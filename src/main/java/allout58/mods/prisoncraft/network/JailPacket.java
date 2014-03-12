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

public class JailPacket implements IPacket
{
    private String playerToJail = "";
    private String jailer = "";
    private int jailTime = -1;

    public JailPacket()
    {
    }

    public JailPacket(String playerToJail, String jailer, int jailTime)
    {
        this.playerToJail = playerToJail;
        this.jailer = jailer;
        this.jailTime = jailTime;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        this.playerToJail=NetworkUtils.readString(bytes);
        this.jailer=NetworkUtils.readString(bytes);
        this.jailTime=bytes.readInt();

    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        NetworkUtils.writeString(bytes, this.playerToJail);
        NetworkUtils.writeString(bytes, this.jailer);        
        bytes.writeInt(this.jailTime);
    }

    
    @Override
    public void postProcess()
    {
        if (JailPermissions.getInstance().playerCanUse(jailer, PermissionLevel.Jailer))
        {
            JailMan.getInstance().TryJailPlayer(playerToJail, jailer, "", jailTime);
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
