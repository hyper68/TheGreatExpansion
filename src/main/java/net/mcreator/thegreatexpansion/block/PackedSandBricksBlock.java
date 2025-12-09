package net.mcreator.thegreatexpansion.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class PackedSandBricksBlock extends Block {
	public PackedSandBricksBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SAND).strength(3f, 10f).requiresCorrectToolForDrops().speedFactor(1.2f));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}