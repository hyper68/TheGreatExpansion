package net.mcreator.thegreatexpansion.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.thegreatexpansion.world.inventory.WaterPumpGUIMenu;
import net.mcreator.thegreatexpansion.procedures.WaterLevelFinderIceMachineProcedure;
import net.mcreator.thegreatexpansion.procedures.PressureCookerPowerValueProcedure;
import net.mcreator.thegreatexpansion.network.WaterPumpGUIButtonMessage;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModScreens;
import net.mcreator.thegreatexpansion.block.entity.WaterPumpBlockEntity;

import com.mojang.blaze3d.systems.RenderSystem;

public class WaterPumpGUIScreen extends AbstractContainerScreen<WaterPumpGUIMenu> implements TheGreatExpansionModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	Button button_test;

	public WaterPumpGUIScreen(WaterPumpGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 104;
		this.imageHeight = 115;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("the_great_expansion:textures/screens/water_pump_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(ResourceLocation.parse("the_great_expansion:textures/screens/water_tank_spritesheet.png"), this.leftPos + 24, this.topPos + 17, Mth.clamp((int) WaterLevelFinderIceMachineProcedure.execute(world, x, y, z) * 54, 0, 594), 0, 54,
				72, 648, 72);
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
		guiGraphics.drawString(this.font, Component.translatable("gui.the_great_expansion.water_pump_gui.label_water_level"), 24, 8, -12829636, false);
		guiGraphics.drawString(this.font, PressureCookerPowerValueProcedure.execute(world, x, y, z), 16, 94, -2302863, false);
	}

	@Override
	public void init() {
		super.init();
		button_test = Button.builder(Component.translatable("gui.the_great_expansion.water_pump_gui.button_test"), e -> {
			int x = WaterPumpGUIScreen.this.x;
			int y = WaterPumpGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new WaterPumpGUIButtonMessage(0, x, y, z));
				WaterPumpGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 58, this.topPos + 73, 46, 20).build();
		this.addRenderableWidget(button_test);
		this.addFluidWidget();
	}

	public void addFluidWidget() {
		WaterPumpBlockEntity blockEntity = (WaterPumpBlockEntity) this.world.getBlockEntity(BlockPos.containing(x, y, z));
		addRenderableOnly(new net.mcreator.thegreatexpansion.utils.FluidStackWidget(this, blockEntity.getFluidTank0(), this.leftPos + 10, this.topPos + 11, 16, 16));
	}
}