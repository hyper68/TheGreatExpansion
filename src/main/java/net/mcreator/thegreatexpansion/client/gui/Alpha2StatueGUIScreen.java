package net.mcreator.thegreatexpansion.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.thegreatexpansion.world.inventory.Alpha2StatueGUIMenu;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModScreens;

import com.mojang.blaze3d.systems.RenderSystem;

public class Alpha2StatueGUIScreen extends AbstractContainerScreen<Alpha2StatueGUIMenu> implements TheGreatExpansionModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	EditBox SurveyCode;

	public Alpha2StatueGUIScreen(Alpha2StatueGUIMenu container, Inventory inventory, Component text) {
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
		if (elementType == 0 && elementState instanceof String stringState) {
			if (name.equals("SurveyCode"))
				SurveyCode.setValue(stringState);
		}
		menuStateUpdateActive = false;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("the_great_expansion:textures/screens/alpha_2_statue_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		SurveyCode.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (SurveyCode.isFocused())
			return SurveyCode.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String SurveyCodeValue = SurveyCode.getValue();
		super.resize(minecraft, width, height);
		SurveyCode.setValue(SurveyCodeValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.the_great_expansion.alpha_2_statue_gui.label_thank_you_for_playing"), 28, 11, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.the_great_expansion.alpha_2_statue_gui.label_the_mod_is_still_in_development"), 5, 28, -205, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.the_great_expansion.alpha_2_statue_gui.label_alpha_2"), 66, 63, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.the_great_expansion.alpha_2_statue_gui.label_treasures_of_the_desert"), 26, 109, -13485605, false);
	}

	@Override
	public void init() {
		super.init();
		SurveyCode = new EditBox(this.font, this.leftPos + 29, this.topPos + 133, 118, 18, Component.translatable("gui.the_great_expansion.alpha_2_statue_gui.SurveyCode"));
		SurveyCode.setMaxLength(8192);
		SurveyCode.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "SurveyCode", content, false);
		});
		SurveyCode.setHint(Component.translatable("gui.the_great_expansion.alpha_2_statue_gui.SurveyCode"));
		this.addWidget(this.SurveyCode);
	}
}