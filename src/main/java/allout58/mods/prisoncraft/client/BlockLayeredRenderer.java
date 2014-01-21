package allout58.mods.prisoncraft.client;

import allout58.mods.prisoncraft.blocks.BlockLayeredTexture;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockLayeredRenderer implements ISimpleBlockRenderingHandler
{

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        // unused
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (block instanceof BlockLayeredTexture)
        {
            if (ClientProxy.renderPass == 0)
            {
                renderer.renderStandardBlock(block, x, y, z);
            }
            else
            {
                int l = block.colorMultiplier(world, x, y, z);
                float f = (float)(l >> 16 & 255) / 255.0F;
                float f1 = (float)(l >> 8 & 255) / 255.0F;
                float f2 = (float)(l & 255) / 255.0F;

                if (EntityRenderer.anaglyphEnable)
                {
                    float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                    float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                    float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                    f = f3;
                    f1 = f4;
                    f2 = f5;
                }

//                return Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[block.blockID] == 0 ? (renderer.partialRenderBounds ? this.renderStandardBlockWithAmbientOcclusionPartial(block, x, y, z, f, f1, f2) : this.renderStandardBlockWithAmbientOcclusion(block, x, y, z, f, f1, f2)) : this.renderStandardBlockWithColorMultiplier(block, x, y, z, f, f1, f2);
            }
        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.BlockMultiLayerRenderer;
    }

}
