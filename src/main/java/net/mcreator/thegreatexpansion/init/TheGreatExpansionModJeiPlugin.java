package net.mcreator.thegreatexpansion.init;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.mcreator.thegreatexpansion.jei_recipes.CrushedRecipeCategory;
import net.mcreator.thegreatexpansion.jei_recipes.CrushedRecipe;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

import java.util.stream.Collectors;
import java.util.Objects;
import java.util.List;

@JeiPlugin
public class TheGreatExpansionModJeiPlugin implements IModPlugin {
	public static mezz.jei.api.recipe.RecipeType<CrushedRecipe> Crushed_Type = new mezz.jei.api.recipe.RecipeType<>(CrushedRecipeCategory.UID, CrushedRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("the_great_expansion:jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new CrushedRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		List<CrushedRecipe> CrushedRecipes = recipeManager.getAllRecipesFor(CrushedRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(Crushed_Type, CrushedRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
	}
}