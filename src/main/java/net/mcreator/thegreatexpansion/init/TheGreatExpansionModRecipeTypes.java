package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.thegreatexpansion.jei_recipes.InductionFurnaceJeiRecipe;
import net.mcreator.thegreatexpansion.jei_recipes.CrusherJEITypeRecipe;
import net.mcreator.thegreatexpansion.jei_recipes.CapsuleRecipeTypeRecipe;

@EventBusSubscriber
public class TheGreatExpansionModRecipeTypes {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, "the_great_expansion");
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, "the_great_expansion");

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		IEventBus bus = ModList.get().getModContainerById("the_great_expansion").get().getEventBus();
		event.enqueueWork(() -> {
			RECIPE_TYPES.register(bus);
			SERIALIZERS.register(bus);
			RECIPE_TYPES.register("capsule_recipe_type", () -> CapsuleRecipeTypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("capsule_recipe_type", () -> CapsuleRecipeTypeRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("induction_furnace_jei", () -> InductionFurnaceJeiRecipe.Type.INSTANCE);
			SERIALIZERS.register("induction_furnace_jei", () -> InductionFurnaceJeiRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("crusher_jei_type", () -> CrusherJEITypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("crusher_jei_type", () -> CrusherJEITypeRecipe.Serializer.INSTANCE);
		});
	}
}