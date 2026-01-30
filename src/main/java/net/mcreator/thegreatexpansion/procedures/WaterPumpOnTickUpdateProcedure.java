package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class WaterPumpOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= 800 && (world.getBlockState(BlockPos.containing(x, -1, z))).getBlock() == Blocks.COBBLESTONE && getFluidTankCapacity(world, BlockPos.containing(x, y, z), 0, null) < 12000) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.boat.paddle_water")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.boat.paddle_water")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), null);
				if (_entityStorage != null)
					_entityStorage.extractEnergy(800, false);
			}
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y, z), null);
				if (_fluidHandler != null)
					_fluidHandler.fill(new FluidStack(Fluids.WATER, 500), IFluidHandler.FluidAction.EXECUTE);
			}
		}
		if (fillTankSimulate(world, BlockPos.containing(x, y, -1), 250, null, Fluids.WATER) <= getFluidTankCapacity(world, BlockPos.containing(x, y, -1), 1, null)) {
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y, -1), null);
				if (_fluidHandler != null)
					_fluidHandler.fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);
			}
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y, z), null);
				if (_fluidHandler != null)
					_fluidHandler.drain(250, IFluidHandler.FluidAction.EXECUTE);
			}
		}
		if (fillTankSimulate(world, BlockPos.containing(x, y, 1), 250, null, Fluids.WATER) <= getFluidTankCapacity(world, BlockPos.containing(x, y, 1), 1, null)) {
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y, 1), null);
				if (_fluidHandler != null)
					_fluidHandler.fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);
			}
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y, z), null);
				if (_fluidHandler != null)
					_fluidHandler.drain(250, IFluidHandler.FluidAction.EXECUTE);
			}
		}
		if (fillTankSimulate(world, BlockPos.containing(-1, y, z), 250, null, Fluids.WATER) <= getFluidTankCapacity(world, BlockPos.containing(-1, y, z), 1, null)) {
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(-1, y, z), null);
				if (_fluidHandler != null)
					_fluidHandler.fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);
			}
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y, z), null);
				if (_fluidHandler != null)
					_fluidHandler.drain(250, IFluidHandler.FluidAction.EXECUTE);
			}
		}
		if (fillTankSimulate(world, BlockPos.containing(1, y, z), 250, null, Fluids.WATER) <= getFluidTankCapacity(world, BlockPos.containing(1, y, z), 1, null)) {
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(1, y, z), null);
				if (_fluidHandler != null)
					_fluidHandler.fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);
			}
			if (world instanceof ILevelExtension _ext) {
				IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y, z), null);
				if (_fluidHandler != null)
					_fluidHandler.drain(250, IFluidHandler.FluidAction.EXECUTE);
			}
		}
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getEnergyStored();
		}
		return 0;
	}

	private static int getFluidTankCapacity(LevelAccessor level, BlockPos pos, int tank, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IFluidHandler fluidHandler = levelExtension.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction);
			if (fluidHandler != null)
				return fluidHandler.getTankCapacity(tank);
		}
		return 0;
	}

	private static int fillTankSimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction, Fluid fluid) {
		if (level instanceof ILevelExtension levelExtension) {
			IFluidHandler fluidHandler = levelExtension.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction);
			if (fluidHandler != null)
				return fluidHandler.fill(new FluidStack(fluid, amount), IFluidHandler.FluidAction.SIMULATE);
		}
		return 0;
	}
}