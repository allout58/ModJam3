package allout58.mods.prisoncraft.tileentities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cpw.mods.fml.common.network.PacketDispatcher;

import allout58.mods.prisoncraft.Config;
import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.commands.JailCommand;
import allout58.mods.prisoncraft.commands.JailPermissions;
import allout58.mods.prisoncraft.constants.ModConstants;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet130UpdateSign;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.EnumGameType;

public class TileEntityPrisonManager extends TileEntity implements IInventory
{
    private ItemStack[] playerInventory;
    public static final int INVENTORY_SIZE = 40;

    public static final int START_MAIN = 0;
    public static final int START_HOTBAR = 32; // ??
    public static final int START_ARMOR = 36; // ??

    public boolean hasJailedPlayer = false;

    public int jailCoord1[] = new int[3];
    public int jailCoord2[] = new int[3];
    public int tpCoordIn[] = new int[3];
    public int tpCoordOut[] = new int[3];
    public String playerName;

    private List signs = new ArrayList();

    private EnumGameType jailedPlayerGM;
    private EntityPlayer jailedPlayer;
    private boolean jailedPlayerPrevJailPerms;

    private int secsLeftJailTime;

    private boolean isDirty = false;

    public TileEntityPrisonManager()
    {
        playerInventory = new ItemStack[INVENTORY_SIZE];
    }

    public boolean changeBlocks(NBTTagCompound locs)
    {
        if (!isInitialized())
        {
            tpCoordIn = locs.getIntArray("tpIn");
            tpCoordOut = locs.getIntArray("tpOut");
            jailCoord1 = locs.getIntArray("jailCoord1");
            jailCoord2 = locs.getIntArray("jailCoord2");
            // give xyz names
            int x1 = jailCoord1[0];
            int y1 = jailCoord1[1];
            int z1 = jailCoord1[2];
            int x2 = jailCoord2[0];
            int y2 = jailCoord2[1];
            int z2 = jailCoord2[2];
            // force ..1 to be lower than ..2
            if (x1 > x2)
            {
                x1 += x2;
                x2 = x1 - x2;
                x1 -= x2;
            }
            if (y1 > y2)
            {
                y1 += y2;
                y2 = y1 - y2;
                y1 -= y2;
            }
            if (z1 > z2)
            {
                z1 += z2;
                z2 = z1 - z2;
                z1 -= z2;
            }
            // loop through each block
            for (int i = x1; i <= x2; i++)
            {
                for (int j = y1; j <= y2; j++)
                {
                    for (int k = z1; k <= z2; k++)
                    {
                        int id = worldObj.getBlockId(i, j, k);
                        if (isValidID(id))
                        {
                            worldObj.setBlock(i, j, k, BlockList.prisonUnbreak.blockID, 0, 3);
                            TileEntity te = worldObj.getBlockTileEntity(i, j, k);
                            if (te instanceof TileEntityPrisonUnbreakable)
                            {
                                ((TileEntityPrisonUnbreakable) te).setFakeBlockID(id);
                            }
                        }
                        TileEntity te = worldObj.getBlockTileEntity(i, j, k);
                        if (te instanceof TileEntitySign)
                        {
                            int coord[] = new int[3];
                            coord[0] = ((TileEntitySign) te).xCoord;
                            coord[1] = ((TileEntitySign) te).yCoord;
                            coord[2] = ((TileEntitySign) te).zCoord;
                            signs.add(coord);
                        }
                    }
                }
            }
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
            isDirty = true;
            return true;
        }
        else
        {
            isDirty = true;
            return false;
        }
    }

    public void revertBlocks()
    {
        isDirty = true;
        // give xyz names
        int x1 = jailCoord1[0];
        int y1 = jailCoord1[1];
        int z1 = jailCoord1[2];
        int x2 = jailCoord2[0];
        int y2 = jailCoord2[1];
        int z2 = jailCoord2[2];
        // force ..1 to be lower than ..2
        if (x1 > x2)
        {
            x1 += x2;
            x2 = x1 - x2;
            x1 -= x2;
        }
        if (y1 > y2)
        {
            y1 += y2;
            y2 = y1 - y2;
            y1 -= y2;
        }
        if (z1 > z2)
        {
            z1 += z2;
            z2 = z1 - z2;
            z1 -= z2;
        }
        // loop through each block
        for (int i = x1; i <= x2; i++)
        {
            for (int j = y1; j <= y2; j++)
            {
                for (int k = z1; k <= z2; k++)
                {
                    TileEntity te = worldObj.getBlockTileEntity(i, j, k);
                    if (te instanceof TileEntityPrisonUnbreakable)
                    {
                        ((TileEntityPrisonUnbreakable) te).revert();
                    }
                }
            }
        }
    }

    private boolean isValidID(int id)
    {
        for (int i = 0; i < ModConstants.WHITELIST_WALL_IDS.length; i++)
        {
            if (id == ModConstants.WHITELIST_WALL_IDS[i])
            {
                return true;
            }
        }
        return false;
    }

    public boolean isInitialized()
    {
        return !(jailCoord1[0] == 0 && jailCoord1[1] == 0 && jailCoord1[2] == 0);

    }

    public void jailPlayer(EntityPlayer player, double time)
    {
        isDirty = true;
        jailedPlayer = player;
        playerName = player.username;
        secsLeftJailTime = (int) (time * 60);
        if (Config.changeGameMode)
        {
            if (player instanceof EntityPlayerMP)
            {
                jailedPlayerGM = ((EntityPlayerMP) player).theItemInWorldManager.getGameType();
                ((EntityPlayerMP) player).setGameType(EnumGameType.ADVENTURE);
            }
            else
            {
                System.out.println("Gamemode not set. Player obj not of type EntityPlayerMP.");
            }
        }
        hasJailedPlayer = true;
        player.mountEntity(null);
        player.setPositionAndUpdate(tpCoordIn[0] + .5, tpCoordIn[1], tpCoordIn[2] + .5);
        if (Config.takeInventory)
        {
            // Take their inventory
            for (int i = START_MAIN; i < START_HOTBAR; i++)
            {
                playerInventory[i] = player.inventory.mainInventory[i];
            }
            for (int i = START_HOTBAR; i < START_ARMOR; i++)
            {
                playerInventory[i] = player.inventory.mainInventory[i];
            }
            for (int i = START_ARMOR; i < INVENTORY_SIZE; i++)
            {
                playerInventory[i] = player.inventory.armorInventory[i - START_ARMOR];
            }
            player.inventory.clearInventory(-1, -1);
        }
        if (Config.noMovement)
        {
            player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60, 300, false));
        }
        if (Config.removeJailPerms)
        {
            jailedPlayerPrevJailPerms = JailPermissions.getInstance().playerCanUse(player);
            JailPermissions.getInstance().removeUserPlayer(player);
        }
        player.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.jailed"));
        for (int i = 0; i < signs.size(); i++)
        {
            int coord[] = (int[]) signs.get(i);
            TileEntity te = worldObj.getBlockTileEntity(coord[0], coord[1], coord[2]);
            if (te instanceof TileEntitySign)
            {
                ((TileEntitySign) te).signText[0] = playerName;
                PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 100, this.worldObj.provider.dimensionId, new Packet130UpdateSign(te.xCoord, te.yCoord, te.zCoord, ((TileEntitySign) te).signText));
            }
        }
    }

    public boolean unjailPlayer()
    {
        if (jailedPlayer != null && hasJailedPlayer)
        {
            isDirty = true;

            if (Config.takeInventory)
            {
                // give their inventory
                for (int i = START_MAIN; i < START_HOTBAR; i++)
                {
                    jailedPlayer.inventory.mainInventory[i] = playerInventory[i];
                }
                for (int i = START_HOTBAR; i < START_ARMOR; i++)
                {
                    jailedPlayer.inventory.mainInventory[i] = playerInventory[i];
                }
                for (int i = START_ARMOR; i < INVENTORY_SIZE; i++)
                {
                    jailedPlayer.inventory.armorInventory[i - START_ARMOR] = playerInventory[i];
                }
                jailedPlayer.inventory.onInventoryChanged();
            }
            if (Config.noMovement)
            {
                jailedPlayer.removePotionEffect(Potion.moveSlowdown.id);
            }
            jailedPlayer.setPositionAndUpdate(tpCoordOut[0] + .5, tpCoordOut[1], tpCoordOut[2] + .5);
            if (Config.changeGameMode)
            {
                if (jailedPlayer instanceof EntityPlayerMP)
                {
                    ((EntityPlayerMP) jailedPlayer).setGameType(jailedPlayerGM);
                }
                else
                {
                    System.out.println("Game mode could not be reverted. Jailed Player obj in not of type EntityPlayerMP.");
                }
            }
            if (Config.removeJailPerms)
            {
                if (jailedPlayerPrevJailPerms)
                {
                    JailPermissions.getInstance().addUserPlayer(jailedPlayer);
                }
            }
            jailedPlayer.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "]").addKey("string.unjailed"));
            for (int i = 0; i < signs.size(); i++)
            {
                int coord[] = (int[]) signs.get(i);
                TileEntity te = worldObj.getBlockTileEntity(coord[0], coord[1], coord[2]);
                if (te instanceof TileEntitySign)
                {
                    ((TileEntitySign) te).signText[0] = "";
                    ((TileEntitySign) te).signText[2] = "";
                    PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 100, this.worldObj.provider.dimensionId, new Packet130UpdateSign(te.xCoord, te.yCoord, te.zCoord, ((TileEntitySign) te).signText));
                }
            }
            hasJailedPlayer = false;
            jailedPlayer = null;
            playerName = "";
            return true;
        }
        else return false;
    }

    @Override
    public void updateEntity()
    {
        if (isDirty)
        {
            isDirty = false;
            worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
        }
        if (hasJailedPlayer)
        {
            if (jailedPlayer != null)
            {
                if (Config.noMovement && worldObj.getTotalWorldTime() % 50 == 0)
                {
                    jailedPlayer.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60, 300, false));
                }
                if (Config.noJumping && worldObj.getTotalWorldTime() % 20 == 0)
                {
                    jailedPlayer.setPositionAndUpdate(jailedPlayer.posX, tpCoordIn[1], jailedPlayer.posZ);
                }
                if (secsLeftJailTime == -1)
                {
                    this.unjailPlayer();
                }
                if (worldObj.getTotalWorldTime() % 20 == 0 && secsLeftJailTime > -1)
                {
                    secsLeftJailTime--;
                    for (int i = 0; i < signs.size(); i++)
                    {
                        int coord[] = (int[]) signs.get(i);
                        TileEntity te = worldObj.getBlockTileEntity(coord[0], coord[1], coord[2]);
                        if (te instanceof TileEntitySign)
                        {
                            ((TileEntitySign) te).signText[2] = String.valueOf(secsLeftJailTime);
                            PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 100, this.worldObj.provider.dimensionId, new Packet130UpdateSign(te.xCoord, te.yCoord, te.zCoord, ((TileEntitySign) te).signText));
                        }
                    }
                }
            }
            else
            {
                if (worldObj.getTotalWorldTime() % 60 == 0)
                {
                    jailedPlayer = findPlayerFromName(playerName);
                }
            }
        }
    }

    /* Utility */

    private EntityPlayer findPlayerFromName(String uname)
    {
        if (worldObj != null)
        {
            ArrayList players = (ArrayList) worldObj.playerEntities;
            for (int i = 0; i < players.size(); i++)
            {
                if (((EntityPlayer) players.get(i)).username.equalsIgnoreCase(uname))
                {
                    return ((EntityPlayer) players.get(i));
                }
            }
        }
        return null;
    }

    /* NBT */

    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        super.readFromNBT(tags);
        hasJailedPlayer = tags.getBoolean("HasJailedPlayer");
        jailedPlayerPrevJailPerms = tags.getBoolean("JailPlayerPreviousPerms");
        tpCoordIn = tags.getIntArray("tpCoordIn");
        tpCoordOut = tags.getIntArray("tpCoordOut");
        jailCoord1 = tags.getIntArray("jailCoord1");
        jailCoord2 = tags.getIntArray("jailCoord2");
        secsLeftJailTime = tags.getInteger("secLeftJailTime");
        NBTTagCompound signTags = tags.getCompoundTag("SignTags");
        int numSize = tags.getInteger("numSigns");
        for (int i = 0; i < numSize; i++)
        {
            signs.add(signTags.getIntArray("Sign" + i));
        }
        if (tags.hasKey("gameMode"))
        {
            jailedPlayerGM = EnumGameType.getByID(tags.getInteger("gameMode"));
        }
        playerName = tags.getString("PlayerUsername");
        jailedPlayer = findPlayerFromName(tags.getString("PlayerUsername"));
        NBTTagList tagList = tags.getTagList("Items");
        playerInventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            NBTTagCompound tagCompound = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < playerInventory.length)
            {
                playerInventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        tags.setBoolean("HasJailedPlayer", hasJailedPlayer);
        tags.setIntArray("tpCoordIn", tpCoordIn);
        tags.setIntArray("tpCoordOut", tpCoordOut);
        tags.setIntArray("jailCoord1", jailCoord1);
        tags.setIntArray("jailCoord2", jailCoord2);
        tags.setBoolean("JailPlayerPreviousPerms", jailedPlayerPrevJailPerms);
        tags.setInteger("numSigns", signs.size());
        tags.setInteger("secLeftJailTime", secsLeftJailTime);
        NBTTagCompound signTags = new NBTTagCompound();
        for (int i = 0; i < signs.size(); i++)
        {
            signTags.setIntArray("Sign" + i, (int[]) signs.get(i));
        }
        tags.setCompoundTag("SignTags", signTags);
        if (jailedPlayerGM != null)
        {
            tags.setInteger("gameMode", jailedPlayerGM.getID());
        }
        if (hasJailedPlayer)
        {
            tags.setString("PlayerUsername", playerName);
        }
        // Write the ItemStacks in the inventory to NBT
        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < playerInventory.length; ++currentIndex)
        {
            if (playerInventory[currentIndex] != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                playerInventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tags.setTag("Items", tagList);

    }

    /* Packets */
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
    {
        readFromNBT(packet.data);
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    /* Inventory */
    @Override
    public int getSizeInventory()
    {
        return playerInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return playerInventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
            }
            else
            {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            setInventorySlotContents(slot, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        playerInventory[slot] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
        {
            itemStack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName()
    {
        // TODO Possibly add custom names
        return "container.PlayerHeldInventory";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        // TODO This needs to change w/ custom names
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        // TODO Actually set this up to only let in appropriate parts
        return true;
    }
}
