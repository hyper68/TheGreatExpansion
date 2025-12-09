package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class InductionFurnaceGaugetextureEnergyProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 1) {
			return 0;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 43999) {
			return 1;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 87999) {
			return 2;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 175999) {
			return 3;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 219999) {
			return 4;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 263999) {
			return 5;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 307999) {
			return 6;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 351999) {
			return 7;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 16071) {
			return 8;
		}
		return 8;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getEnergyStored();
		}
		return 0;
	}
}