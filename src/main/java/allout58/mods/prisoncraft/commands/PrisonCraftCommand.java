package allout58.mods.prisoncraft.commands;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.config.ConfigChangableBlocks;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.items.ItemList;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.storage.SaveHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        String use = "/prisoncraft configure <cell|jail> [jailname]  ";
        use += "/prisoncraft doneconfig  ";
        use += "/prisoncraft whitelist <add|remove> <id>  ";
        use += "/prisoncraft whitelist <save|reload>";
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
            icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidArgument")));
        }
        else
        {
            if (astring[0].equalsIgnoreCase("whitelist"))
            {
                processIds(icommandsender, astring);
            }
            else if (astring[0].equalsIgnoreCase("configure"))
            {
                processConfigure(icommandsender, astring);
            }
            else if (astring[0].equalsIgnoreCase("doneconfig"))
            {
                processDoneConfig(icommandsender, astring);
            }
            else if (astring[0].equalsIgnoreCase("help"))
                icommandsender.addChatMessage(new ChatComponentText(getCommandUsage(icommandsender)));

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
            if ("whitelist".startsWith(ARG_LC)) MATCHES.add("whitelist");
            if ("configure".startsWith(ARG_LC)) MATCHES.add("configure");
            if ("doneconfig".startsWith(ARG_LC)) MATCHES.add("doneconfig");
        }
        if (astring.length == 2)
        {
            if (astring[0].equalsIgnoreCase("whitelist"))
            {
                if ("list".startsWith(ARG_LC)) MATCHES.add("list");
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
        if (astring.length == 3)
        {
            if (astring[1].equalsIgnoreCase("remove"))
            {
                for (String s : ConfigChangableBlocks.getInstance().getNames())
                {
                    if (s.startsWith(ARG_LC)) MATCHES.add(s);
                    if (s.contains(":"))
                    {
                        String sDomless = s.substring(s.indexOf(":") + 1);
                        if (sDomless.startsWith(ARG_LC)) MATCHES.add(s);
                    }
                }
            }
            if (astring[1].equalsIgnoreCase("add"))
            {
                for (Object o : Block.blockRegistry.getKeys())
                {
                    if (o instanceof String)
                    {
                        //if block already in whitelist, don't show
                        String s = (String) o;
                        List<String> sList = new ArrayList<String>();
                        for (String t : ConfigChangableBlocks.getInstance().getNames())
                        {
                            sList.add(t);
                        }
                        if (sList.contains(s)) continue;

                        //If block in blacklist, don't show
                        //reuse sList
                        sList.clear();
                        for (String t : ConfigChangableBlocks.getInstance().getBlackList())
                        {
                            sList.add(t);
                        }
                        if (sList.contains(s)) continue;

                        //starts with what's there, add it
                        if (s.startsWith(ARG_LC)) MATCHES.add(s);

                        //add the ability to ignore the domain
                        if (s.contains(":"))
                        {
                            String sDomless = s.substring(s.indexOf(":") + 1);
                            if (sDomless.startsWith(ARG_LC)) MATCHES.add(s);
                        }
                    }
                }
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
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidArgument")));
        }
        else
        {
            if (astring.length == 2)
            {
                if (astring[1].equalsIgnoreCase("save"))
                {
                    ConfigChangableBlocks.getInstance().save();
                    sender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.savedconfig")));
                    if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                    {
                        MinecraftServer.getServer().addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.savedconfig")));
                    }

                }
                else if (astring[1].equalsIgnoreCase("reload"))
                {
                    SaveHandler saveHandler = (SaveHandler) MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler();
                    String fileName = saveHandler.getWorldDirectory().getAbsolutePath() + "/PCUnbreakableIDs.txt";
                    File f = new File(fileName);
                    ConfigChangableBlocks.getInstance().load(f);
                    sender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.loadedconfig")));
                    if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                    {
                        MinecraftServer.getServer().addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.loadedconfig")));
                    }
                }
                else if (astring[1].equalsIgnoreCase("list"))
                {
                    List<String> sL = new ArrayList<String>();
                    for (String e : ConfigChangableBlocks.getInstance().getNames())
                    {
                        if (e.contains("minecraft:"))
                        {
                            e = e.replace("minecraft:", "");
                        }
                        sL.add(e);
                    }
                    sender.addChatMessage(new ChatComponentText(StringUtils.join(sL, ", ")));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidArgument")));
                }
            }
            if (astring.length == 3)
            {
                if (astring[1].equalsIgnoreCase("add"))
                {
                    try
                    {
                        if (ConfigChangableBlocks.getInstance().addWhitelist(astring[2]))
                        {
                            sender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.idadded")));
                            if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                            {
                                MinecraftServer.getServer().addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.idadded")));
                            }
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidArgument")));
                    }
                }
                else if (astring[1].equalsIgnoreCase("remove"))
                {
                    try
                    {
                        if (ConfigChangableBlocks.getInstance().removeWhiteList(astring[2]))
                        {
                            sender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.idremoved")));
                            if (!sender.getCommandSenderName().equalsIgnoreCase("server"))
                            {
                                MinecraftServer.getServer().addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + StatCollector.translateToLocal("string.idremoved")));
                            }
                        }
                        else
                        {
                            sender.addChatMessage(new ChatComponentText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.idremoved.fail")));
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.nan")));
                    }
                }
            }
        }
    }

    private void processConfigure(ICommandSender sender, String[] astring)
    {
        if (!(sender instanceof EntityPlayer))
        {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.mustbeplayer")));
            return;
        }
        if (astring.length < 2 || astring.length > 3)
        {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidArgument")));
        }
        else
        {
            if (astring[1].equalsIgnoreCase("cell"))
            {
                EntityPlayer player = (EntityPlayer) sender;
                player.dropItem(ItemList.configWand, 1).delayBeforeCanPickup = 0;
                player.dropItem(Item.getItemFromBlock(BlockList.prisonMan), 1).delayBeforeCanPickup = 0;
                if (player instanceof EntityPlayerMP)
                {
                    ((EntityPlayerMP) player).setGameType(GameType.CREATIVE);
                }
                else
                {
                    PrisonCraft.logger.error("Gamemode not set. Player obj not of type EntityPlayerMP in configuring command.");
                }
            }
            if (astring[1].equalsIgnoreCase("jail"))
            {
                if (astring.length != 3)
                {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidArgument")));
                }
                else
                {
                    EntityPlayer player = (EntityPlayer) sender;
                    ItemStack is = new ItemStack(ItemList.jailLink);
                    is.stackTagCompound = new NBTTagCompound();
                    is.stackTagCompound.setString("jailName", astring[2]);
                    player.entityDropItem(is, 0).delayBeforeCanPickup = 0;
                }
            }
        }
    }

    private void processDoneConfig(ICommandSender sender, String[] astring)
    {
        if (!(sender instanceof EntityPlayer))
        {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.mustbeplayer")));
            return;
        }
        if (astring.length != 1)
        {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED.toString() + StatCollector.translateToLocal("string.invalidArgument")));
        }
        else
        {
            if (sender instanceof EntityPlayerMP)
            {
                ((EntityPlayerMP) sender).setGameType(GameType.SURVIVAL);
            }
            else
            {
                PrisonCraft.logger.error("Gamemode not set. Player obj not of type EntityPlayerMP in doneconfiguring command.");
            }
        }
    }

}
