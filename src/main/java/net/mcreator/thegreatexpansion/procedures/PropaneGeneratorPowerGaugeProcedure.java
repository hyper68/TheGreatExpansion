package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class PropaneGeneratorPowerGaugeProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 1) {
			return 0;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 1785) {
			return 1;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 3571) {
			return 2;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 7142) {
			return 3;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 8928) {
			return 4;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 10714) {
			return 5;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 12499) {
			return 6;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 14285) {
			return 7;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 16071) {
			return 8;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 17857) {
			return 9;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 19642) {
			return 10;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 21428) {
			return 11;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < 23214) {
			return 12;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 25000) {
			return 13;
		}
		return 13;
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