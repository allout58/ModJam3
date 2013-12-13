package allout58.mods.prisoncraft.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
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
        icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(icommandsender.getCommandSenderName()+" frees "+astring[0]+" from jail..."));
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
        return i==0;
    }
}
