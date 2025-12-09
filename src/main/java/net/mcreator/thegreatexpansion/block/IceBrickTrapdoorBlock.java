package net.mcreator.thegreatexpansion.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.SoundType;

public class IceBrickTrapdoorBlock extends TrapDoorBlock {
	public IceBrickTrapdoorBlock() {
		super(BlockSetType.STONE, BlockBehaviour.Properties.of().sound(SoundType.GLASS).strength(1f, 10f).requiresCorrectToolForDrops());
	}
}