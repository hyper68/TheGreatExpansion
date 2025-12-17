package net.mcreator.thegreatexpansion.init;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.mcreator.thegreatexpansion.jei_recipes.CapsuleRecipeTypeRecipeCategory;
import net.mcreator.thegreatexpansion.jei_recipes.CapsuleRecipeTypeRecipe;

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
	public static mezz.jei.api.recipe.RecipeType<CapsuleRecipeTypeRecipe> CapsuleRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(CapsuleRecipeTypeRecipeCategory.UID, CapsuleRecipeTypeRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("the_great_expansion:jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new CapsuleRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		List<CapsuleRecipeTypeRecipe> CapsuleRecipeTypeRecipes = recipeManager.getAllRecipesFor(CapsuleRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(CapsuleRecipeType_Type, CapsuleRecipeTypeRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
	}
}