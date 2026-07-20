package horiuchi.floweringleaves;

import net.minecraft.core.block.Blocks;

import java.util.Map;

public class LeavesFlowerUtil {
	public static final int MASK_FLOWER = 0b11100000;
	public static final int SHIFT_FLOWER = 5;

	public enum LeafFlower {
		NONE,
		YELLOW,
		RED,
		PINK,
		PURPLE,
		LIGHT_BLUE,
		ORANGE,
		UNUSED
	}

	public static final Map<Integer, LeafFlower> ITEM_TO_FLOWER = Map.of(
		Blocks.FLOWER_YELLOW.id(), LeafFlower.YELLOW,
		Blocks.FLOWER_RED.id(), LeafFlower.RED,
		Blocks.FLOWER_PINK.id(), LeafFlower.PINK,
		Blocks.FLOWER_PURPLE.id(), LeafFlower.PURPLE,
		Blocks.FLOWER_LIGHT_BLUE.id(), LeafFlower.LIGHT_BLUE,
		Blocks.FLOWER_ORANGE.id(), LeafFlower.ORANGE
	);

	public static final Map<LeafFlower, Integer> FLOWER_TO_ITEM = Map.of(
		LeafFlower.YELLOW, Blocks.FLOWER_YELLOW.id(),
		LeafFlower.RED, Blocks.FLOWER_RED.id(),
		LeafFlower.PINK, Blocks.FLOWER_PINK.id(),
		LeafFlower.PURPLE, Blocks.FLOWER_PURPLE.id(),
		LeafFlower.LIGHT_BLUE, Blocks.FLOWER_LIGHT_BLUE.id(),
		LeafFlower.ORANGE, Blocks.FLOWER_ORANGE.id()
	);

	public static LeafFlower getLeavesFlower(int meta) {
		return LeafFlower.values()[(meta & MASK_FLOWER) >> SHIFT_FLOWER];
	}

	public static int setLeavesFlower(int meta, LeafFlower flower) {
		meta &= ~MASK_FLOWER;
		meta |= (flower.ordinal() << SHIFT_FLOWER) & MASK_FLOWER;
		return meta;
	}
}
