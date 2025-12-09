/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.registries.Registries;

import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class TheGreatExpansionModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(Registries.POTION, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<Potion, Potion> SNOWFLECK_POTION = REGISTRY.register("snowfleck_potion",
			() -> new Potion(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3600, 0, false, true), new MobEffectInstance(MobEffects.WATER_BREATHING, 3000, 0, false, true), new MobEffectInstance(MobEffects.HEALTH_BOOST, 2000, 0, false, true)));
	public static final DeferredHolder<Potion, Potion> AVENCIDOR_POTION = REGISTRY.register("avencidor_potion",
			() -> new Potion(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 4000, 1, false, true), new MobEffectInstance(MobEffects.HEALTH_BOOST, 3600, 0, false, true), new MobEffectInstance(MobEffects.LUCK, 2000, 0, false, true)));
}