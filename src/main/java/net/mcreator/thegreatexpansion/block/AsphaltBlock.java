package net.mcreator.thegreatexpansion.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class AsphaltBlock extends Block {
	public AsphaltBlock() {
		super(BlockBehaviour.Properties.of().strength(1.1f, 10f).requiresCorrectToolForDrops().speedFactor(1.3f));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}