package horiuchi.floweringleaves.mixin;

import horiuchi.floweringleaves.LeavesFlowerUtil;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLogicLeavesBase.class)
public abstract class BlockLogicLeavesMixin extends BlockLogic {
	public BlockLogicLeavesMixin(Block<?> block, Material material) {
		super(block, material);
	}

	@Inject(method = "getBreakResult", at = @At(value = "RETURN"), cancellable = true)
	public void getBreakResultMixin(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity, CallbackInfoReturnable<ItemStack[]> cir)
	{
		LeavesFlowerUtil.LeafFlower currentFlower = LeavesFlowerUtil.getLeavesFlower(meta);
		if(currentFlower == LeavesFlowerUtil.LeafFlower.NONE)
			return;

		ItemStack[] originalReturn = cir.getReturnValue();
		ItemStack flowerBlock = new ItemStack(LeavesFlowerUtil.FLOWER_TO_ITEM.get(currentFlower), 1, 0);

		if (originalReturn == null)
		{
			cir.setReturnValue(new ItemStack[]{flowerBlock});
		}
		else
		{
			ItemStack[] newReturn = new ItemStack[cir.getReturnValue().length + 1];
			newReturn[cir.getReturnValue().length] = flowerBlock;
			System.arraycopy(cir.getReturnValue(), 0, newReturn, 0, cir.getReturnValue().length);
			cir.setReturnValue(newReturn);
		}
	}

	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit)
	{
		ItemStack heldItem = player.getHeldItem();
		LeavesFlowerUtil.LeafFlower currentFlower = LeavesFlowerUtil.getLeavesFlower(world.getBlockData(tilePos));

		if (heldItem == null) // If the leaves block has a flower, drop it
		{
			if(currentFlower == LeavesFlowerUtil.LeafFlower.NONE)
				return false;

			world.setBlockTypeDataNotify(tilePos, world.getBlockType(tilePos), LeavesFlowerUtil.setLeavesFlower(world.getBlockData(tilePos), LeavesFlowerUtil.LeafFlower.NONE));
			world.playSoundAtEntity(player, player, "item.pickup", 1.0F, 1.0F);
			if (!world.isClientSide)
			{
				world.dropItem(tilePos, new ItemStack(LeavesFlowerUtil.FLOWER_TO_ITEM.get(currentFlower), 1, 0));
			}
		}
		else // Attempt to consume the flower and set metadata
		{
			LeavesFlowerUtil.LeafFlower newFlower = LeavesFlowerUtil.ITEM_TO_FLOWER.getOrDefault(heldItem.itemID, LeavesFlowerUtil.LeafFlower.NONE);

			// If we don't have a valid flower in hand, or we already have this flower on the leaves, cancel
			if (newFlower == LeavesFlowerUtil.LeafFlower.NONE || newFlower == currentFlower)
				return false;

			// If the leaves block has a flower, drop it
			if (!world.isClientSide && currentFlower != LeavesFlowerUtil.LeafFlower.NONE)
			{
				world.dropItem(tilePos, new ItemStack(LeavesFlowerUtil.FLOWER_TO_ITEM.get(currentFlower), 1, 0));
			}

			// Consume and set metadata
			heldItem.consumeItem(player);
			world.playBlockSoundEffect(player, (float)tilePos.x() + 0.5F, (float)tilePos.y() + 0.5F, (float)tilePos.z() + 0.5F, this.block, EnumBlockSoundEffectType.PLACE);
			int meta = world.getBlockData(tilePos);
			meta = LeavesFlowerUtil.setLeavesFlower(meta, newFlower);
			meta = BlockLogicLeavesBase.setDecaying(meta, false);
			meta = BlockLogicLeavesBase.setPermanent(meta, true);
			world.setBlockTypeDataNotify(tilePos, world.getBlockType(tilePos), meta);
		}
		return true;
	}
}

