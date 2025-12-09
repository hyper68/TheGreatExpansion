package net.mcreator.thegreatexpansion.fluid;

import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.LiquidBlock;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModItems;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModFluids;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModFluidTypes;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModBlocks;

public abstract class CrudeOilFluid extends BaseFlowingFluid {
	public static final BaseFlowingFluid.Properties PROPERTIES = new BaseFlowingFluid.Properties(() -> TheGreatExpansionModFluidTypes.CRUDE_OIL_TYPE.get(), () -> TheGreatExpansionModFluids.CRUDE_OIL.get(),
			() -> TheGreatExpansionModFluids.FLOWING_CRUDE_OIL.get()).explosionResistance(100f).bucket(() -> TheGreatExpansionModItems.CRUDE_OIL_BUCKET.get()).block(() -> (LiquidBlock) TheGreatExpansionModBlocks.CRUDE_OIL.get());

	private CrudeOilFluid() {
		super(PROPERTIES);
	}

	public static class Source extends CrudeOilFluid {
		public int getAmount(FluidState state) {
			return 8;
		}

		public boolean isSource(FluidState state) {
			return true;
		}
	}

	public static class Flowing extends CrudeOilFluid {
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		public boolean isSource(FluidState state) {
			return false;
		}
	}
}