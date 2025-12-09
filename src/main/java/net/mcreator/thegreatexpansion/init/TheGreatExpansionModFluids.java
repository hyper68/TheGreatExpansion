/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

import net.mcreator.thegreatexpansion.fluid.CrudeOilFluid;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class TheGreatExpansionModFluids {
	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(BuiltInRegistries.FLUID, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<Fluid, FlowingFluid> CRUDE_OIL = REGISTRY.register("crude_oil", () -> new CrudeOilFluid.Source());
	public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_CRUDE_OIL = REGISTRY.register("flowing_crude_oil", () -> new CrudeOilFluid.Flowing());

	@EventBusSubscriber(Dist.CLIENT)
	public static class FluidsClientSideHandler {
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			ItemBlockRenderTypes.setRenderLayer(CRUDE_OIL.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(FLOWING_CRUDE_OIL.get(), RenderType.translucent());
		}
	}
}