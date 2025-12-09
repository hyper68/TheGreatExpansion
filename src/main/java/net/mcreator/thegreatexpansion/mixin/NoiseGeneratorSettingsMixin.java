package net.mcreator.thegreatexpansion.mixin;

import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.core.Holder;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModBiomes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;

@Mixin(NoiseGeneratorSettings.class)
public class NoiseGeneratorSettingsMixin implements TheGreatExpansionModBiomes.TheGreatExpansionModNoiseGeneratorSettings {
	@Unique
	private Holder<DimensionType> the_great_expansion_dimensionTypeReference;

	@WrapMethod(method = "surfaceRule")
	public SurfaceRules.RuleSource surfaceRule(Operation<SurfaceRules.RuleSource> original) {
		SurfaceRules.RuleSource retval = original.call();
		if (this.the_great_expansion_dimensionTypeReference != null) {
			retval = TheGreatExpansionModBiomes.adaptSurfaceRule(retval, this.the_great_expansion_dimensionTypeReference);
		}
		return retval;
	}

	@Override
	public void setthe_great_expansionDimensionTypeReference(Holder<DimensionType> dimensionType) {
		this.the_great_expansion_dimensionTypeReference = dimensionType;
	}
}