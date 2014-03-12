package allout58.mods.prisoncraft.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import allout58.mods.prisoncraft.constants.ModConstants;

public class ConfigChangableBlocks
{
    private static ConfigChangableBlocks instance;

    private List<Block> unbreakIDWhitelist = new ArrayList<Block>();
    private File propFile;

    public static ConfigChangableBlocks getInstance()
    {
        if (instance == null)
        {
            instance = new ConfigChangableBlocks();
        }
        return instance;
    }

    public void load(File f)
    {
        unbreakIDWhitelist.clear();
        propFile = f;
        if (!f.exists())
        {
            try
            {
                f.createNewFile();
                for (int i = 0; i < Config.unbreakIDWhitelistDefault.length; i++)
                {
                    unbreakIDWhitelist.add(Block.getBlockFromName(ModConstants.WHITELIST_WALL_BLOCKS[i]));
                }
                save();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            BufferedReader read = new BufferedReader(new FileReader(f));

            String ln;
            while ((ln = read.readLine()) != null)
            {
                try
                {
                    this.addName(ln);
                }
                catch (NumberFormatException e)
                {
                }
            }
            read.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            FileWriter writer = new FileWriter(propFile);

            for (int i = 0; i < unbreakIDWhitelist.size(); i++)
            {
                writer.write(unbreakIDWhitelist.get(i).toString() + "\n");
            }
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String[] getNames()
    {
        String[] ret = new String[unbreakIDWhitelist.size()];
        for (int i = 0; i < unbreakIDWhitelist.size(); i++)
        {
            ret[i] = Block.blockRegistry.getNameForObject(unbreakIDWhitelist.get(i));
        }
        return ret;
    }

    public boolean addName(String name)
    {
        Block b = Block.getBlockFromName(name);
        if (b != null) return unbreakIDWhitelist.add(b);
        return false;
    }

    public boolean addBlock(Block block)
    {
        return unbreakIDWhitelist.add(block);
    }

    public boolean removeName(String name)
    {
        Block b = Block.getBlockFromName(name);
        return removeBlock(b);
    }

    public boolean removeBlock(Block block)
    {
        int loc = unbreakIDWhitelist.indexOf(block);
        if (loc > -1)
        {
            unbreakIDWhitelist.remove(loc);
        }
        return loc > -1;
    }
}
