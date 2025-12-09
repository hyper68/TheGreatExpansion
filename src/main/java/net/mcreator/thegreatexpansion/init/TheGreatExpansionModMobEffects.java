/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import net.mcreator.thegreatexpansion.potion.SnowboundMobEffect;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class TheGreatExpansionModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> SNOWBOUND = REGISTRY.register("snowbound", () -> new SnowboundMobEffect());
}