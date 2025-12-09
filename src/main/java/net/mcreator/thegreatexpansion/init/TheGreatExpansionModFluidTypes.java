/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.fluids.FluidType;

import net.mcreator.thegreatexpansion.fluid.types.CrudeOilFluidType;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class TheGreatExpansionModFluidTypes {
	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<FluidType, FluidType> CRUDE_OIL_TYPE = REGISTRY.register("crude_oil", () -> new CrudeOilFluidType());
}