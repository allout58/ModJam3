package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class JailPermissions
{
    private static JailPermissions instance;
    private List canUse=new ArrayList();
    
    public static JailPermissions getInstance()
    {
        if(instance==null)
        {
            instance=new JailPermissions();
        }
        return instance;
    }
    
    public boolean playerCanUse(ICommandSender sender)
    {
        for(int i=0;i<canUse.size();i++)
        {
            if(((String)canUse.get(i)).equalsIgnoreCase(sender.getCommandSenderName()))
            {
                return true;
            }
        }
        return false;
    }
    public boolean playerCanUse(String senderName)
    {
        for(int i=0;i<canUse.size();i++)
        {
            if(((String)canUse.get(i)).equalsIgnoreCase(senderName))
            {
                return true;
            }
        } 
        return false;
    }
    
    public void addUserPlayer(ICommandSender player)
    {
        if(!playerCanUse(player))
        {
            canUse.add(player.getCommandSenderName());
        }
    }
    public void addUserPlayer(String playerName)
    {
        if(!playerCanUse(playerName))
        {
            canUse.add(playerName);
        }
    }
    
    public void removeUserPlayer(ICommandSender player)
    {
        if(playerCanUse(player))
        {
            canUse.remove(player.getCommandSenderName());
        }
    }
    public void removeUserPlayer(String playerName)
    {
        if(playerCanUse(playerName))
        {
            canUse.remove(playerName);
        }
    }
}
