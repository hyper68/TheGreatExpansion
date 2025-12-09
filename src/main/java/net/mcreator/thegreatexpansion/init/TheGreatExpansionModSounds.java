/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class TheGreatExpansionModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> SNOWBOUND = REGISTRY.register("snowbound", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "snowbound")));
	public static final DeferredHolder<SoundEvent, SoundEvent> HOLLOWBELLS = REGISTRY.register("hollowbells", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "hollowbells")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SNOWBOUNDEFFECT = REGISTRY.register("snowboundeffect", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "snowboundeffect")));
}