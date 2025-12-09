/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.Registries;

import net.mcreator.thegreatexpansion.entity.SubZombieEntity;
import net.mcreator.thegreatexpansion.entity.StalkerEntity;
import net.mcreator.thegreatexpansion.entity.SandCrabEntity;
import net.mcreator.thegreatexpansion.entity.LeadArrowProjectileEntity;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

@EventBusSubscriber
public class TheGreatExpansionModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<SandCrabEntity>> SAND_CRAB = register("sand_crab",
			EntityType.Builder.<SandCrabEntity>of(SandCrabEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(0.6f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<LeadArrowProjectileEntity>> LEAD_ARROW_PROJECTILE = register("lead_arrow_projectile",
			EntityType.Builder.<LeadArrowProjectileEntity>of(LeadArrowProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<StalkerEntity>> STALKER = register("stalker",
			EntityType.Builder.<StalkerEntity>of(StalkerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<SubZombieEntity>> SUB_ZOMBIE = register("sub_zombie",
			EntityType.Builder.<SubZombieEntity>of(SubZombieEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.ridingOffset(-0.6f).sized(0.6f, 1.8f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(RegisterSpawnPlacementsEvent event) {
		SandCrabEntity.init(event);
		StalkerEntity.init(event);
		SubZombieEntity.init(event);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SAND_CRAB.get(), SandCrabEntity.createAttributes().build());
		event.put(STALKER.get(), StalkerEntity.createAttributes().build());
		event.put(SUB_ZOMBIE.get(), SubZombieEntity.createAttributes().build());
	}
}