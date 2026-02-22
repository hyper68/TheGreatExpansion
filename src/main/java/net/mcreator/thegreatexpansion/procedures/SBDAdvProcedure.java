package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class SBDAdvProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (hasEntityInInventory(entity, new ItemStack(TheGreatExpansionModItems.ALUMINUM_INGOT.get())) || hasEntityInInventory(entity, new ItemStack(TheGreatExpansionModItems.TIN_INGOT.get()))
				|| hasEntityInInventory(entity, new ItemStack(TheGreatExpansionModItems.LEAD_INGOT.get())) || hasEntityInInventory(entity, new ItemStack(TheGreatExpansionModItems.TITANIUM_INGOT.get()))
				|| hasEntityInInventory(entity, new ItemStack(TheGreatExpansionModItems.MERCURY_INGOT.get()))) {
			if (entity instanceof ServerPlayer _player) {
				AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("the_great_expansion:same_but_different"));
				if (_adv != null) {
					AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
					if (!_ap.isDone()) {
						for (String criteria : _ap.getRemainingCriteria())
							_player.getAdvancements().award(_adv, criteria);
					}
				}
			}
		}
	}

	private static boolean hasEntityInInventory(Entity entity, ItemStack itemstack) {
		if (entity instanceof Player player)
			return player.getInventory().contains(stack -> !stack.isEmpty() && ItemStack.isSameItem(stack, itemstack));
		return false;
	}
}