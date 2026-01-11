package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.items.ItemHandlerHelper;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModItems;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModBlocks;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class SkyCatalystRightclickedOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ItemStack pickaxe = ItemStack.EMPTY;
		double EnchtSize = 0;
		double i = 0;
		double j = 0;
		double k = 0;
		double particleRadius = 0;
		double particleAmount = 0;
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == TheGreatExpansionModBlocks.SKY_CATALYST_MARKER.get()) {
			if (entity instanceof Player _player)
				_player.getCooldowns().addCooldown(itemstack.getItem(), 100);
			itemstack.setCount(itemstack.getCount() - 1);
			particleAmount = 8;
			particleRadius = 3;
			for (int index0 = 0; index0 < (int) particleAmount; index0++) {
				world.addParticle(ParticleTypes.ELECTRIC_SPARK, (x + 0 + Mth.nextDouble(RandomSource.create(), -1, 1) * particleRadius), (y + 0 + Mth.nextDouble(RandomSource.create(), -1, 1) * particleRadius),
						(z + 0 + Mth.nextDouble(RandomSource.create(), -1, 1) * particleRadius), (Mth.nextDouble(RandomSource.create(), -0.001, 0.001)), (Mth.nextDouble(RandomSource.create(), -0.001, 0.001)),
						(Mth.nextDouble(RandomSource.create(), -0.001, 0.001)));
			}
			if (entity instanceof Player _player) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				_player.level().getBlockState(_bp).useWithoutItem(_player.level(), _player, BlockHitResult.miss(new Vec3(_bp.getX(), _bp.getY(), _bp.getZ()), Direction.UP, _bp));
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("the_great_expansion:skycatalystsfx")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("the_great_expansion:skycatalystsfx")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
			TheGreatExpansionMod.queueServerWork(80, () -> {
				world.destroyBlock(BlockPos.containing(x, y, z), false);
				if (world instanceof Level _level && !_level.isClientSide())
					_level.explode(null, x, y, z, (float) 0.5, Level.ExplosionInteraction.BLOCK);
				if (entity instanceof ServerPlayer _player) {
					AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("the_great_expansion:x_marks_the_spot"));
					if (_adv != null) {
						AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
						if (!_ap.isDone()) {
							for (String criteria : _ap.getRemainingCriteria())
								_player.getAdvancements().award(_adv, criteria);
						}
					}
				}
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(TheGreatExpansionModItems.SKYLARIAM_GEM.get()).copy();
					_setstack.setCount(1);
					ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
				}
			});
		}
	}
}