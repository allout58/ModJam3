package allout58.mods.prisoncraft.tileentities;

import java.util.Vector;

import cpw.mods.fml.common.network.PacketDispatcher;

import allout58.mods.prisoncraft.blocks.BlockList;
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
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonManager extends TileEntity implements IInventory
{
    private ItemStack[] playerInventory;
    public static final int INVENTORY_SIZE = 40;

    public static final int START_MAIN = 0;
    public static final int START_HOTBAR = 32; // ??
    public static final int START_ARMOR = 36; // ??

    public boolean hasJailedPlayer = false;

    public int tpCoordIn[] = new int[3];
    public int tpCoordOut[] = new int[3];

    private EntityPlayer jailedPlayer;
    public String playerName;

    private Boolean isDirty = false;

    public TileEntityPrisonManager()
    {
        playerInventory = new ItemStack[INVENTORY_SIZE];
    }

    public void changeBlocks(NBTTagCompound locs)
    {
        tpCoordIn = locs.getIntArray("tpIn");
        tpCoordOut = locs.getIntArray("tpOut");
        isDirty = true;
        // give xyz names
        int x1 = locs.getInteger("x1");
        int y1 = locs.getInteger("y1");
        int z1 = locs.getInteger("z1");
        int x2 = locs.getInteger("x2");
        int y2 = locs.getInteger("y2");
        int z2 = locs.getInteger("z2");
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
                            ((TileEntityPrisonUnbreakable) te).setBlockID(id);
                        }
                    }
                }
            }
        }
    }

    private Boolean isValidID(int id)
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

    public void jailPlayer(EntityPlayer player)
    {
        isDirty = true;
        jailedPlayer = player;
        playerName = player.username;
        hasJailedPlayer = true;
        player.mountEntity(null);
        player.setPositionAndUpdate(tpCoordIn[0] + .5, tpCoordIn[1], tpCoordIn[2] + .5);
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
        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 300, false));
    }

    public void unjailPlayer()
    {
        isDirty = true;

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
        jailedPlayer.removePotionEffect(Potion.moveSlowdown.id);
        jailedPlayer.removePotionEffect(Potion.jump.id);
        jailedPlayer.setPositionAndUpdate(tpCoordOut[0]+.5, tpCoordOut[1], tpCoordOut[2]+.5);
        jailedPlayer.inventory.onInventoryChanged();
        hasJailedPlayer = false;
        jailedPlayer = null;
        playerName = "";

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
            jailedPlayer.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 300, false));
        }
    }

    /* NBT */

    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        super.readFromNBT(tags);
        hasJailedPlayer = tags.getBoolean("HasJailedPlayer");
        tpCoordIn = tags.getIntArray("tpCoordIn");
        tpCoordOut = tags.getIntArray("tpCoordOut");
        jailedPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(tags.getString("PlayerUsername"));
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
        if (hasJailedPlayer)
        {
            tags.setString("PlayerUsername", jailedPlayer.username);
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
