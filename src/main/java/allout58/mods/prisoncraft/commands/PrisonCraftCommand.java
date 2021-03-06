package allout58.mods.prisoncraft.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.config.ConfigChangableIDs;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.items.ItemList;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.storage.SaveHandler;

public class PrisonCraftCommand implements ICommand
{

    private List aliases;

    public PrisonCraftCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("prisoncraft");
        this.aliases.add("pc");
    }

    @Override
    public int compareTo(Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "prisoncraft";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        String use = "/prisoncraft configure <cell|jail> [jailname]\n";
        use += "/prisoncraft doneconfig\n";
        use += "/prisoncraft ids <add|remove> <id>\n";
        use += "/prisoncraft ids <save|reload>";
        return use;
    }

    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        if (astring.length < 1)
        {
            icommandsender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.invalidArgument"));
        }
        else
        {
            if (astring[0].equalsIgnoreCase("ids"))
            {
                processIds(icommandsender, astring);
            }
            if (astring[0].equalsIgnoreCase("configure"))
            {
                processConfigure(icommandsender, astring);
            }
            if (astring[0].equalsIgnoreCase("doneconfig"))
            {
                processDoneConfig(icommandsender, astring);
            }
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        return JailPermissions.getInstance().playerCanUse(icommandsender, PermissionLevel.ConfigurationManager);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        final List<String> MATCHES = new LinkedList<String>();
        // always the last
        final String ARG_LC = astring[astring.length - 1].toLowerCase();
        if (astring.length == 1)
        {
            if ("ids".startsWith(ARG_LC)) MATCHES.add("ids");
            if ("configure".startsWith(ARG_LC)) MATCHES.add("configure");
            if ("doneconfig".startsWith(ARG_LC)) MATCHES.add("doneconfig");
        }
        if (astring.length == 2)
        {
            if (astring[0].equalsIgnoreCase("ids"))
            {
                if ("add".startsWith(ARG_LC)) MATCHES.add("add");
                if ("remove".startsWith(ARG_LC)) MATCHES.add("remove");
                if ("save".startsWith(ARG_LC)) MATCHES.add("save");
                if ("reload".startsWith(ARG_LC)) MATCHES.add("reload");
            }
            if (astring[0].equalsIgnoreCase("configure"))
            {
                if ("cell".startsWith(ARG_LC)) MATCHES.add("cell");
                if ("jail".startsWith(ARG_LC)) MATCHES.add("jail");
            }
        }
        return MATCHES.isEmpty() ? null : MATCHES;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

    private void processIds(ICommandSender sender, String[] astring)
    {
        if (astring.length < 2 || astring.length > 3)
        {
            sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.invalidArgument"));
        }
        else
        {
            if (astring.length == 2)
            {
                if (astring[1].equalsIgnoreCase("save"))
                {
                    ConfigChangableIDs.getInstance().save();
                    sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.savedconfig"));
                    if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                    {
                        MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.savedconfig"));
                    }

                }
                else if (astring[1].equalsIgnoreCase("reload"))
                {
                    SaveHandler saveHandler = (SaveHandler) MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler();
                    String fileName = saveHandler.getWorldDirectory().getAbsolutePath() + "/PCUnbreakableIDs.txt";
                    File f = new File(fileName);
                    ConfigChangableIDs.getInstance().load(f);
                    sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.loadedconfig"));
                    if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                    {
                        MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.loadedconfig"));
                    }
                }
                else
                {
                    sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.invalidArgument"));
                }
            }
            if (astring.length == 3)
            {
                if (astring[1].equalsIgnoreCase("add"))
                {
                    try
                    {
                        int integer = Integer.parseInt(astring[2]);
                        ConfigChangableIDs.getInstance().addID(integer);
                        sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idadded"));
                        if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                        {
                            MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idadded"));
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.nan"));
                    }
                }
                if (astring[1].equalsIgnoreCase("remove"))
                {
                    try
                    {
                        int integer = Integer.parseInt(astring[2]);
                        if (ConfigChangableIDs.getInstance().removeID(integer))
                        {
                            sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idremoved"));
                            if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                            {
                                MinecraftServer.getServer().sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] ").addKey("string.idremoved"));
                            }
                        }
                        else
                        {
                            sender.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.idremoved.fail"));
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.nan"));
                    }
                }
            }
        }
    }

    private void processConfigure(ICommandSender sender, String[] astring)
    {
        if (!(sender instanceof EntityPlayer))
        {
            sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.mustbeplayer"));
            return;
        }
        if (astring.length < 2 || astring.length > 3)
        {
            sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.invalidArgument"));
        }
        else
        {
            if (astring[1].equalsIgnoreCase("cell"))
            {
                EntityPlayer player = (EntityPlayer) sender;
                player.dropPlayerItem(new ItemStack(ItemList.configWand)).delayBeforeCanPickup = 0;
                player.dropPlayerItem(new ItemStack(BlockList.prisonMan)).delayBeforeCanPickup = 0;
                if (player instanceof EntityPlayerMP)
                {
                    ((EntityPlayerMP) player).setGameType(EnumGameType.CREATIVE);
                }
                else
                {
                    PrisonCraft.logger.severe("Gamemode not set. Player obj not of type EntityPlayerMP in configuring command.");
                }
            }
            if (astring[1].equalsIgnoreCase("jail"))
            {
                if (astring.length != 3)
                {
                    sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.invalidArgument"));
                }
                else
                {
                    EntityPlayer player = (EntityPlayer) sender;
                    ItemStack is = new ItemStack(ItemList.jailLink);
                    is.stackTagCompound = new NBTTagCompound();
                    is.stackTagCompound.setString("jailName", astring[2]);
                    player.dropPlayerItem(is).delayBeforeCanPickup = 0;
                }
            }
        }
    }

    private void processDoneConfig(ICommandSender sender, String[] astring)
    {
        if (!(sender instanceof EntityPlayer))
        {
            sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.mustbeplayer"));
            return;
        }
        if (astring.length != 1)
        {
            sender.sendChatToPlayer(new ChatMessageComponent().addText(EnumChatFormatting.RED.toString()).addKey("string.invalidArgument"));
        }
        else
        {
            if (sender instanceof EntityPlayerMP)
            {
                ((EntityPlayerMP) sender).setGameType(EnumGameType.SURVIVAL);
            }
            else
            {
                PrisonCraft.logger.severe("Gamemode not set. Player obj not of type EntityPlayerMP in doneconfiguring command.");
            }
        }
    }

}
