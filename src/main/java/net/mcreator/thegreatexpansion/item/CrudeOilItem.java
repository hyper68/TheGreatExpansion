package net.mcreator.thegreatexpansion.item;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModFluids;

public class CrudeOilItem extends BucketItem {
	public CrudeOilItem() {
		super(TheGreatExpansionModFluids.CRUDE_OIL.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)

		);
	}
}