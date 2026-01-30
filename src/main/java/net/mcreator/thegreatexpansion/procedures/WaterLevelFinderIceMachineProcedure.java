package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class WaterLevelFinderIceMachineProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) == 0) {
			return 0;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 1000) {
			return 1;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 2000) {
			return 2;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 3000) {
			return 3;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 4000) {
			return 4;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 5000) {
			return 5;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 6000) {
			return 6;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 7000) {
			return 7;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 8000) {
			return 8;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 9000) {
			return 9;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 10000) {
			return 10;
		}
		if (getFluidTankLevel(world, BlockPos.containing(x, y, z), 0, null) >= 11000) {
			return 11;
		}
		return 0;
	}

	private static int getFluidTankLevel(LevelAccessor level, BlockPos pos, int tank, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IFluidHandler fluidHandler = levelExtension.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction);
			if (fluidHandler != null)
				return fluidHandler.getFluidInTank(tank).getAmount();
		}
		return 0;
	}
}