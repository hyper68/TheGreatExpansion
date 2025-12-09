package net.mcreator.thegreatexpansion.block;

import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FenceGateBlock;

public class IceBrickFencegateBlock extends FenceGateBlock {
	public IceBrickFencegateBlock() {
		super(WoodType.OAK, BlockBehaviour.Properties.of().sound(SoundType.GLASS).strength(1f, 10f).requiresCorrectToolForDrops().forceSolidOn());
	}
}