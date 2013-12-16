package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class JailPermissions
{
    public static JailPermissions instance;
    private List canUse=new ArrayList();
    
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
    
    public void addUserPlayer(EntityPlayer player)
    {
        if(!playerCanUse(player))
        {
            canUse.add(player.username);
        }
    }
}
