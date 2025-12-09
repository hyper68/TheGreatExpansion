package net.mcreator.thegreatexpansion.item;

import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class SnowscapeDiskItem extends Item {
	public SnowscapeDiskItem() {
		super(new Item.Properties().jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(TheGreatExpansionMod.MODID, "snowscape_disk"))));
	}
}