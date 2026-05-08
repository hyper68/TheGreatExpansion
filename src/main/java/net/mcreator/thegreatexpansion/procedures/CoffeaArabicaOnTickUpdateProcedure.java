package net.mcreator.thegreatexpansion.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModBlocks;

public class CoffeaArabicaOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (Math.random() < 0.1) {
			world.setBlock(BlockPos.containing(x, y, z), TheGreatExpansionModBlocks.COFFEA_ARABICA_GERMINATED.get().defaultBlockState(), 3);
		}
	}
}