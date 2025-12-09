package net.mcreator.thegreatexpansion.block;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.LiquidBlock;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModFluids;

public class CrudeOilBlock extends LiquidBlock {
	public CrudeOilBlock() {
		super(TheGreatExpansionModFluids.CRUDE_OIL.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).strength(100f).noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
	}
}