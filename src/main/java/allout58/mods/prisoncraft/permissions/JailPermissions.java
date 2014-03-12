package allout58.mods.prisoncraft.permissions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveHandler;
import allout58.mods.prisoncraft.PrisonCraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class JailPermissions
{
    private static JailPermissions instance;
    private List canUse = new ArrayList();

    public static JailPermissions getInstance()
    {
        if (instance == null)
        {
            instance = new JailPermissions();
        }
        return instance;
    }

    public boolean playerCanUse(ICommandSender sender, PermissionLevel level)
    {
        return playerCanUse(sender.getCommandSenderName(), level);
    }

    public boolean playerCanUse(String senderName, PermissionLevel level)
    {
        PlayerPermsision pp = getPermsFromName(senderName);
        if (pp != null)
        {
            // Player perm level >= required perm
            if (pp.Level.getValue() >= level.getValue())
            {
                return true;
            }
        }
        return false;
    }

    public boolean addUserPlayer(ICommandSender player, PermissionLevel level)
    {
        return addUserPlayer(player.getCommandSenderName(), level);
    }

    public boolean addUserPlayer(String playerName, PermissionLevel level)
    {
        if (!playerCanUse(playerName, PermissionLevel.Default))
        {
            PlayerPermsision perm = new PlayerPermsision();
            perm.UserName = playerName;
            perm.Level = level;
            canUse.add(perm);
            this.save();
            return true;
        }
        return false;
    }

    public boolean removeUserPlayer(ICommandSender player)
    {
        return removeUserPlayer(player.getCommandSenderName());
    }

    public boolean removeUserPlayer(String playerName)
    {
        PlayerPermsision pp = getPermsFromName(playerName);
        if (pp != null)
        {
            canUse.remove(pp);
            this.save();
            return true;
        }
        return false;
    }

    public PermissionLevel getPlayerPermissionLevel(ICommandSender player)
    {
        return getPlayerPermissionLevel(player.getCommandSenderName());
    }

    public PermissionLevel getPlayerPermissionLevel(String playerName)
    {
        PlayerPermsision pp = getPermsFromName(playerName);
        if (pp != null) return pp.Level;
        return PermissionLevel.Default;
    }

    public void save()
    {
        MinecraftServer server = null;
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER)
        {
            // We are on the server side.
            server = MinecraftServer.getServer();
        }
        else if (side == Side.CLIENT)
        {
            // We are on the client side.
        }
        else
        {
            // We have an errornous state!
        }
        if (server != null)
        {
            SaveHandler saveHandler = (SaveHandler) server.worldServerForDimension(0).getSaveHandler();
            String fileName = saveHandler.getWorldDirectory().getAbsolutePath() + "/PrisonCraftPerms.txt";
            File f = new File(fileName);
            if (!f.exists())
            {
                try
                {
                    f.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            FileWriter output = null;
            try
            {
                output = new FileWriter(fileName);
                BufferedWriter writer = new BufferedWriter(output);
                for (int i = 0; i < canUse.size(); i++)
                {
                    if (canUse.get(i) instanceof PlayerPermsision)
                    {
                        PlayerPermsision pp = (PlayerPermsision) canUse.get(i);
                        // Ignore server and rcon when writing to list
                        if (pp.UserName.equalsIgnoreCase("server") || pp.UserName.equalsIgnoreCase("rcon")) continue;
                        writer.write(pp.UserName + "-" + pp.Level.getValue() + "\n");
                    }
                }
                writer.close();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                if (output != null)
                {
                    try
                    {
                        output.close();
                    }
                    catch (IOException e)
                    {
                        // Ignore issues during closing
                    }
                }
            }

        }
    }

    public void load()
    {
        MinecraftServer server = null;
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER)
        {
            // We are on the server side.
            server = MinecraftServer.getServer();
        }
        else if (side == Side.CLIENT)
        {
            // We are on the client side.
        }
        else
        {
            // We have an errornous state!
        }
        if (server != null)
        {
            SaveHandler saveHandler = (SaveHandler) server.worldServerForDimension(0).getSaveHandler();
            String fileName = saveHandler.getWorldDirectory().getAbsolutePath() + "/PrisonCraftPerms.txt";
            File f = new File(fileName);
            if (!f.exists())
            {
                try
                {
                    f.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            FileReader file = null;
            try
            {
                file = new FileReader(fileName);
                BufferedReader reader = new BufferedReader(file);
                String line = "";
                int num = 0;
                while ((line = reader.readLine()) != null)
                {
                    num++;
                    int dashIndex = line.indexOf("-");
                    if (dashIndex > 0)
                    {
                        // Reassemble the player perms
                        String name = line.substring(0, dashIndex);
                        int pLev = Integer.parseInt(line.substring(dashIndex + 1));
                        try
                        {
                            PermissionLevel pl = PermissionLevel.fromInt(pLev);
                            addUserPlayer(name, pl);
                        }
                        catch (IndexOutOfBoundsException e)
                        {
                            PrisonCraft.logger.error("Could not parse line " + num + " in perms file: Invalid perm level");
                        }
                    }
                    else
                    {
                        PrisonCraft.logger.error("Could not parse line " + num + " in perms file: No '-'");
                    }
                }
                reader.close();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                if (file != null)
                {
                    try
                    {
                        file.close();
                    }
                    catch (IOException e)
                    {
                        // Ignore issues during closing
                    }
                }
            }
        }
        PrisonCraft.logger.info("Adding server and rcon with highest permissions");
        addUserPlayer("server", PermissionLevel.FinalWord);
        addUserPlayer("rcon", PermissionLevel.FinalWord);
    }

    public void clear()
    {
        canUse.clear();
    }

    // Private functions
    private PlayerPermsision getPermsFromName(String username)
    {
        for (int i = 0; i < canUse.size(); i++)
        {
            if (canUse.get(i) instanceof PlayerPermsision)
            {
                PlayerPermsision pp = (PlayerPermsision) canUse.get(i);
                if (pp.UserName.equalsIgnoreCase(username))
                {
                    return pp;
                }
            }
        }
        return null;
    }
}
