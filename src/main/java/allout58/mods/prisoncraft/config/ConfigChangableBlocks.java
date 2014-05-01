package allout58.mods.prisoncraft.config;

import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.constants.ModConstants;
import net.minecraft.block.Block;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigChangableBlocks
{
    private static ConfigChangableBlocks instance;

    private List<Block> unbreakIDWhitelist = new ArrayList<Block>();
    private List<Block> blockBlacklist = new ArrayList<Block>();
    private File propFile;

    public static ConfigChangableBlocks getInstance()
    {
        if (instance == null)
        {
            instance = new ConfigChangableBlocks();
        }
        return instance;
    }

    /* Load/save functions */
    
    public void load(File f)
    {
        //Blacklist all PC blocks
        addBlacklist(BlockList.prisonUnbreakPaneIron);
        addBlacklist(BlockList.prisonUnbreakPaneGlass);
        addBlacklist(BlockList.prisonUnbreakGlass);
        addBlacklist(BlockList.prisonUnbreak);
        addBlacklist(BlockList.prisonMan);
        addBlacklist(BlockList.prisonJailView);
        
        unbreakIDWhitelist.clear();
        propFile = f;
        if (!f.exists())
        {
            try
            {
                f.createNewFile();
                for (int i = 0; i < Config.unbreakIDWhitelistDefault.length; i++)
                {
                    addWhitelist(ModConstants.getWHITELIST_WALL_BLOCKS()[i]);
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
                    this.addWhitelist(ln);
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
                writer.write(Block.blockRegistry.getNameForObject(unbreakIDWhitelist.get(i)) + "\n");
            }
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*Currently whitelisted blocks*/
    
    public String[] getNames()
    {
        String[] ret = new String[unbreakIDWhitelist.size()];
        for (int i = 0; i < unbreakIDWhitelist.size(); i++)
        {
            ret[i] = Block.blockRegistry.getNameForObject(unbreakIDWhitelist.get(i));
        }
        return ret;
    }

    /* Whitelist functions */
    
    public boolean addWhitelist(String name)
    {
        Block b = Block.getBlockFromName(name);
        if (b != null) return addWhitelist(b);
        return false;
    }

    public boolean addWhitelist(Block block)
    {
        if (blockBlacklist.contains(block)) return false;
        else return unbreakIDWhitelist.add(block);
    }

    public boolean removeWhiteList(String name)
    {
        Block b = Block.getBlockFromName(name);
        return removeWhitelist(b);
    }

    public boolean removeWhitelist(Block block)
    {
        int loc = unbreakIDWhitelist.indexOf(block);
        if (loc > -1)
        {
            unbreakIDWhitelist.remove(loc);
        }
        return loc > -1;
    }

    public boolean isValidName(String name)
    {
        for (Block n : unbreakIDWhitelist)
        {
            if (name.equals(n))
            {
                return true;
            }
        }
        return false;
    }

    /*Blacklist functions*/
    
    public boolean addBlacklist(String name)
    {
        Block b = Block.getBlockFromName(name);
        if (b != null) return addBlacklist(b);
        return false;
    }

    public boolean addBlacklist(Block block)
    {
        return blockBlacklist.add(block);
    }
    
    public String[] getBlackList()
    {
        String[] ret = new String[blockBlacklist.size()];
        for (int i = 0; i < blockBlacklist.size(); i++)
        {
            ret[i] = Block.blockRegistry.getNameForObject(blockBlacklist.get(i));
        }
        return ret;
    }

}
