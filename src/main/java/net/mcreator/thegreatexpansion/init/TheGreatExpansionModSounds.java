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
	public static final DeferredHolder<SoundEvent, SoundEvent> HIGHER = REGISTRY.register("higher", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "higher")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SKYBOUND = REGISTRY.register("skybound", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "skybound")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SKYSTHELIMITSFX = REGISTRY.register("skysthelimitsfx", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "skysthelimitsfx")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SKYCATALYSTSFX = REGISTRY.register("skycatalystsfx", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "skycatalystsfx")));
	public static final DeferredHolder<SoundEvent, SoundEvent> TOME_WIND_WOOSH = REGISTRY.register("tome_wind_woosh", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "tome_wind_woosh")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SHINE = REGISTRY.register("shine", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("the_great_expansion", "shine")));
}