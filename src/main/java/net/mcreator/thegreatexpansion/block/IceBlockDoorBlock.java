package net.mcreator.thegreatexpansion.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.DoorBlock;

public class IceBlockDoorBlock extends DoorBlock {
	public IceBlockDoorBlock() {
		super(BlockSetType.STONE, BlockBehaviour.Properties.of().sound(SoundType.GLASS).strength(1f, 10f).requiresCorrectToolForDrops());
	}
}