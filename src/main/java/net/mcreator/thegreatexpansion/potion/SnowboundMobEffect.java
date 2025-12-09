package net.mcreator.thegreatexpansion.potion;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModAttributes;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class SnowboundMobEffect extends MobEffect {
	public SnowboundMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
		this.withSoundOnAdded(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("the_great_expansion:snowboundeffect")));
		this.addAttributeModifier(TheGreatExpansionModAttributes.APALATIAN, ResourceLocation.fromNamespaceAndPath(TheGreatExpansionMod.MODID, "effect.snowbound_0"), 0.5, AttributeModifier.Operation.ADD_VALUE);
	}
}