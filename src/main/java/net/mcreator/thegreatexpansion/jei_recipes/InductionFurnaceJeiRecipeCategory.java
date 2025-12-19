package net.mcreator.thegreatexpansion.jei_recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.NonNullList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModJeiPlugin;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModBlocks;

import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.constants.VanillaTypes;

import java.util.List;

public class InductionFurnaceJeiRecipeCategory implements IRecipeCategory<InductionFurnaceJeiRecipe> {
	public final static ResourceLocation UID = ResourceLocation.parse("the_great_expansion:induction_furnace_jei");
	public final static ResourceLocation TEXTURE = ResourceLocation.parse("the_great_expansion:textures/screens/inductionfurnacejei.png");
	private final IDrawable background;
	private final IDrawable icon;
	private final Minecraft mc = Minecraft.getInstance();

	public InductionFurnaceJeiRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 165);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TheGreatExpansionModBlocks.INDUCTION_FURNACE.get().asItem()));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<InductionFurnaceJeiRecipe> getRecipeType() {
		return TheGreatExpansionModJeiPlugin.InductionFurnaceJei_Type;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Induction Furnace Jei");
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public int getWidth() {
		return this.background.getWidth();
	}

	@Override
	public int getHeight() {
		return this.background.getHeight();
	}

	@Override
	public void draw(InductionFurnaceJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics);

	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, InductionFurnaceJeiRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> recipeOutputs = recipe.getResultItems();
		List<ItemStack> actualOutputs = NonNullList.withSize(1, ItemStack.EMPTY);
		for (int i = 0; i < recipeOutputs.size(); i++) {
			actualOutputs.set(i, recipeOutputs.get(i));
		}
		builder.addSlot(RecipeIngredientRole.INPUT, 61, 35).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 35).addItemStack(actualOutputs.get(0));
	}
}