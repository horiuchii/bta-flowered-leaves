package horiuchi.floweringleaves.mixin;

import com.google.common.collect.ImmutableMap;
import horiuchi.floweringleaves.LeavesFlowerUtil;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.block.model.generic.BlockModelGenericLeaves;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.useless.dragonfly.models.block.StaticBlockModel;

import java.util.Map;

@Mixin(BlockModelGenericLeaves.class)
public class BlockModelGenericLeavesMixin<T extends BlockLogic> extends BlockModelGeneric<T> {
	@Unique
	private static final Map<LeavesFlowerUtil.LeafFlower, StaticBlockModel> FLOWER_TO_TEXTURE = ImmutableMap.of(
		LeavesFlowerUtil.LeafFlower.YELLOW, BlockModelDispatcher.loadDataModel("floweringleaves:block/leaves/yellow_flowering_overlay").asModel(),
		LeavesFlowerUtil.LeafFlower.RED, BlockModelDispatcher.loadDataModel("floweringleaves:block/leaves/red_flowering_overlay").asModel(),
		LeavesFlowerUtil.LeafFlower.PINK, BlockModelDispatcher.loadDataModel("floweringleaves:block/leaves/pink_flowering_overlay").asModel(),
		LeavesFlowerUtil.LeafFlower.PURPLE, BlockModelDispatcher.loadDataModel("floweringleaves:block/leaves/purple_flowering_overlay").asModel(),
		LeavesFlowerUtil.LeafFlower.LIGHT_BLUE, BlockModelDispatcher.loadDataModel("floweringleaves:block/leaves/lightblue_flowering_overlay").asModel(),
		LeavesFlowerUtil.LeafFlower.ORANGE, BlockModelDispatcher.loadDataModel("floweringleaves:block/leaves/orange_flowering_overlay").asModel()
	);

	public BlockModelGenericLeavesMixin(@NotNull Block<T> block, @NotNull StaticBlockModel staticModel) {
		super(block, staticModel);
	}

	public boolean renderAttached(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
		boolean didRender = super.renderAttached(tessellator, worldSource, tilePos, cullFaces, overrideTexture);

		if (worldSource.getBlockType(tilePos) == Blocks.LEAVES_CACAO || worldSource.getBlockType(tilePos) == Blocks.LEAVES_CHERRY_FLOWERING)
			return true;

		LeavesFlowerUtil.LeafFlower flower = LeavesFlowerUtil.getLeavesFlower(worldSource.getBlockData(tilePos));
		if (flower != LeavesFlowerUtil.LeafFlower.NONE)
		{
			didRender |= FLOWER_TO_TEXTURE.get(flower).renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, (double)0.0F, (double)0.0F, (double)0.0F, false, cullFaces, overrideTexture);
		}

		return didRender;
	}
}
