package horiuchi.floweringleaves.mixin;

import com.google.common.collect.ImmutableMap;
import horiuchi.floweringleaves.LeavesFlowerUtil;
import net.minecraft.client.render.block.model.BlockModelLeaves;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(BlockModelLeaves.class)
public class BlockModelLeavesMixin<T extends BlockLogic> extends BlockModelStandard<T> {
	private static final Map<LeavesFlowerUtil.LeafFlower, IconCoordinate> FLOWER_TO_TEXTURE = ImmutableMap.of(
		LeavesFlowerUtil.LeafFlower.YELLOW, TextureRegistry.getTexture("floweringleaves:block/leaves/yellow_flowering_overlay"),
		LeavesFlowerUtil.LeafFlower.RED, TextureRegistry.getTexture("floweringleaves:block/leaves/red_flowering_overlay"),
		LeavesFlowerUtil.LeafFlower.PINK, TextureRegistry.getTexture("floweringleaves:block/leaves/pink_flowering_overlay"),
		LeavesFlowerUtil.LeafFlower.PURPLE, TextureRegistry.getTexture("floweringleaves:block/leaves/purple_flowering_overlay"),
		LeavesFlowerUtil.LeafFlower.LIGHT_BLUE, TextureRegistry.getTexture("floweringleaves:block/leaves/lightblue_flowering_overlay"),
		LeavesFlowerUtil.LeafFlower.ORANGE, TextureRegistry.getTexture("floweringleaves:block/leaves/orange_flowering_overlay")
	);

	public BlockModelLeavesMixin(Block<T> block) {
		super(block);
	}

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		super.render(tessellator, x, y, z);

		if (renderBlocks.blockAccess.getBlockId(x, y, z) == Blocks.LEAVES_CACAO.id())
			return true;

		LeavesFlowerUtil.LeafFlower flower = LeavesFlowerUtil.getLeavesFlower(renderBlocks.blockAccess.getBlockMetadata(x, y, z));

		if (flower != LeavesFlowerUtil.LeafFlower.NONE)
		{
			renderBlocks.overrideBlockTexture = FLOWER_TO_TEXTURE.get(flower);
			this.renderStandardBlock(tessellator, this.block.getBoundsRaw(), x, y, z, 1.0F, 1.0F, 1.0F);
		}

		renderBlocks.overrideBlockTexture = null;
		return true;
	}
}
