package net.mcreator.thegreatexpansion.init;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.mcreator.thegreatexpansion.jei_recipes.*;

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
	public static mezz.jei.api.recipe.RecipeType<InductionFurnaceJeiRecipe> InductionFurnaceJei_Type = new mezz.jei.api.recipe.RecipeType<>(InductionFurnaceJeiRecipeCategory.UID, InductionFurnaceJeiRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<CrusherJEITypeRecipe> CrusherJEIType_Type = new mezz.jei.api.recipe.RecipeType<>(CrusherJEITypeRecipeCategory.UID, CrusherJEITypeRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("the_great_expansion:jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new CapsuleRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new InductionFurnaceJeiRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CrusherJEITypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		List<CapsuleRecipeTypeRecipe> CapsuleRecipeTypeRecipes = recipeManager.getAllRecipesFor(CapsuleRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(CapsuleRecipeType_Type, CapsuleRecipeTypeRecipes);
		List<InductionFurnaceJeiRecipe> InductionFurnaceJeiRecipes = recipeManager.getAllRecipesFor(InductionFurnaceJeiRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(InductionFurnaceJei_Type, InductionFurnaceJeiRecipes);
		List<CrusherJEITypeRecipe> CrusherJEITypeRecipes = recipeManager.getAllRecipesFor(CrusherJEITypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(CrusherJEIType_Type, CrusherJEITypeRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
	}
}