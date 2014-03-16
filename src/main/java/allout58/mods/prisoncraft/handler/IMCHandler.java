package allout58.mods.prisoncraft.handler;

import net.minecraft.block.Block;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.config.ConfigChangableBlocks;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.relauncher.FMLLaunchHandler;

/**IMC Message format:
 * 
 * [
 * Request Blacklisting a block Name
 *  type: String
 *  key: blacklist
 *  value: Block.blockRegistry.getNameForObject(yourBlockObjectHere)
 * ]
 * 
 * @author allout58
 *
 */
public class IMCHandler
{
    public static void HandleIMC(ImmutableList<IMCMessage> messages)
    {
        for (IMCMessage msg : messages)
        {
            //Mods can add their blocks to a black list that
            //players can't add to the convertabl list
            if(msg.key=="blacklist"&&msg.isStringMessage())
            {
                String niceSender=Loader.instance().getIndexedModList().get(msg.getSender()).getName();
                String blBlockName=msg.getStringValue();
                if(ConfigChangableBlocks.getInstance().addBlacklist(blBlockName))
                {
                    PrisonCraft.logger.info("IMCHandler: "+niceSender+" successfully requested that its "+blBlockName+" be blacklisted.");
                }
                else
                {
                    PrisonCraft.logger.error("IMCHandler: "+niceSender+" failed to blacklist block "+blBlockName+".");
                }
            }
        }
    }
}
