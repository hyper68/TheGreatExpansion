package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class PropaneGeneratorOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double Energy = 0;
		if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), null)) {
			Energy = extractEnergySimulate(world, BlockPos.containing(x, y, z), 500, null);
			Energy = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) Energy, Direction.DOWN);
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), null);
				if (_entityStorage != null)
					_entityStorage.extractEnergy((int) Energy, false);
			}
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y - 1, z), Direction.DOWN);
				if (_entityStorage != null)
					_entityStorage.receiveEnergy((int) Energy, false);
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x - 1, y, z), null)) {
			Energy = extractEnergySimulate(world, BlockPos.containing(x, y, z), 500, null);
			Energy = receiveEnergySimulate(world, BlockPos.containing(x - 1, y, z), (int) Energy, Direction.WEST);
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), null);
				if (_entityStorage != null)
					_entityStorage.extractEnergy((int) Energy, false);
			}
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x - 1, y, z), Direction.WEST);
				if (_entityStorage != null)
					_entityStorage.receiveEnergy((int) Energy, false);
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x + 1, y, z), null)) {
			Energy = extractEnergySimulate(world, BlockPos.containing(x, y, z), 500, null);
			Energy = receiveEnergySimulate(world, BlockPos.containing(x + 1, y, z), (int) Energy, Direction.EAST);
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), null);
				if (_entityStorage != null)
					_entityStorage.extractEnergy((int) Energy, false);
			}
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x + 1, y, z), Direction.EAST);
				if (_entityStorage != null)
					_entityStorage.receiveEnergy((int) Energy, false);
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y, z - 1), null)) {
			Energy = extractEnergySimulate(world, BlockPos.containing(x, y, z), 500, null);
			Energy = receiveEnergySimulate(world, BlockPos.containing(x, y, z - 1), (int) Energy, Direction.SOUTH);
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), null);
				if (_entityStorage != null)
					_entityStorage.extractEnergy((int) Energy, false);
			}
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z - 1), Direction.SOUTH);
				if (_entityStorage != null)
					_entityStorage.receiveEnergy((int) Energy, false);
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y, z + 1), null)) {
			Energy = extractEnergySimulate(world, BlockPos.containing(x, y, z), 500, null);
			Energy = receiveEnergySimulate(world, BlockPos.containing(x, y, z + 1), (int) Energy, Direction.NORTH);
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), null);
				if (_entityStorage != null)
					_entityStorage.extractEnergy((int) Energy, false);
			}
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z + 1), Direction.NORTH);
				if (_entityStorage != null)
					_entityStorage.receiveEnergy((int) Energy, false);
			}
		}
	}

	private static boolean canReceiveEnergy(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.canReceive();
		}
		return false;
	}

	private static int extractEnergySimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.extractEnergy(amount, true);
		}
		return 0;
	}

	private static int receiveEnergySimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.receiveEnergy(amount, true);
		}
		return 0;
	}
}