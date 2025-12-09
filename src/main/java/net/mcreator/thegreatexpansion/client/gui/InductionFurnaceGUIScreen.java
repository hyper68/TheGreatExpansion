package net.mcreator.thegreatexpansion.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.thegreatexpansion.world.inventory.InductionFurnaceGUIMenu;
import net.mcreator.thegreatexpansion.procedures.InductionFurnaceGetEnergyProcedure;
import net.mcreator.thegreatexpansion.procedures.InductionFurnaceGaugetextureEnergyProcedure;
import net.mcreator.thegreatexpansion.procedures.InductionFurnaceCraftingProcedure;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModScreens;

import java.util.stream.Collectors;
import java.util.Arrays;

import com.mojang.blaze3d.systems.RenderSystem;

public class InductionFurnaceGUIScreen extends AbstractContainerScreen<InductionFurnaceGUIMenu> implements TheGreatExpansionModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;

	public InductionFurnaceGUIScreen(InductionFurnaceGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("the_great_expansion:textures/screens/induction_furnace_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		boolean customTooltipShown = false;
		if (mouseX > leftPos + 20 && mouseX < leftPos + 44 && mouseY > topPos + 29 && mouseY < topPos + 53) {
			String hoverText = InductionFurnaceGetEnergyProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (!customTooltipShown)
			this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(ResourceLocation.parse("the_great_expansion:textures/screens/energy_bar_spritesheet.png"), this.leftPos + 24, this.topPos + 7, Mth.clamp((int) InductionFurnaceGaugetextureEnergyProcedure.execute(world, x, y, z) * 16, 0, 128),
				0, 16, 64, 144, 64);
		guiGraphics.blit(ResourceLocation.parse("the_great_expansion:textures/screens/processing_ring_spritesheet.png"), this.leftPos + 100, this.topPos + 35, Mth.clamp((int) InductionFurnaceCraftingProcedure.execute(world, x, y, z) * 16, 0, 64), 0,
				16, 16, 80, 16);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
	}
}