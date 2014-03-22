package com.countrygamer.capo.blocks.tiles;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class IncubationRecipies {
	private static final IncubationRecipies incubatingBase = new IncubationRecipies();

	/** The list of smelting results. */
	private HashMap<ItemStack[], ItemStack> incubationList = new HashMap<ItemStack[], ItemStack>();

	/**
	 * Used to call methods addIncubation and getIncubationResult.
	 */
	public static final IncubationRecipies smelting() {
		return incubatingBase;
	}

	public final int numberOfEggs = 5;
	public final ItemStack[] meatStacks = new ItemStack[] {
			new ItemStack(Items.porkchop, 1), // pig
			new ItemStack(Items.beef, 1), // cow
			new ItemStack(Items.string, 1), // sheep
			new ItemStack(Items.chicken, 1), // chicken
			new ItemStack(Items.beef, 1), // shroom
	};
	public final ItemStack[] foodStacks = new ItemStack[] {
			new ItemStack(Items.carrot, 1), // pig
			new ItemStack(Items.wheat, 1), // cow
			new ItemStack(Items.wheat, 1), // sheep
			new ItemStack(Items.wheat_seeds, 1), // chicken
			new ItemStack(Items.wheat, 1), // shroom
	};
	public final ItemStack[] bodyStacks1 = new ItemStack[] {
			new ItemStack(Items.leather, 1), new ItemStack(Items.leather, 1),
			new ItemStack(Blocks.wool, 1), new ItemStack(Items.feather, 1),
			new ItemStack(Blocks.red_mushroom, 1), };
	public final ItemStack[] bodyStacks2 = new ItemStack[] {
			new ItemStack(Items.leather, 1), new ItemStack(Items.leather, 1),
			new ItemStack(Items.leather, 1), new ItemStack(Items.feather, 1),
			new ItemStack(Items.leather, 1), };
	public final ItemStack[] bodyStacks3 = new ItemStack[] {
			new ItemStack(Items.leather, 1), new ItemStack(Items.leather, 1),
			new ItemStack(Blocks.wool, 1), new ItemStack(Items.feather, 1),
			new ItemStack(Blocks.red_mushroom, 1), };
	public final ItemStack[] results = new ItemStack[] {
			new ItemStack(Items.spawn_egg, 1, 90),
			new ItemStack(Items.spawn_egg, 1, 92),
			new ItemStack(Items.spawn_egg, 1, 91),
			new ItemStack(Items.spawn_egg, 1, 93),
			new ItemStack(Items.spawn_egg, 1, 96), };

	private IncubationRecipies() {
		// this.addIncubation(Blocks.oreIron.blockID, new
		// ItemStack(Items.ingotIron), 0.7F);
		// pigEgg = 3 leather + 1 rawPork + 1 carrot
		// cowEgg = 3 leather + 1 rawBeef + 1 wheat
		// sheepEgg = 1 leather + 2 wool + 1 wheat + 1 string
		// chickenEgg = 3 feathers + 1 rawChicken + 1 seeds
		// shroomEgg = 1 leather + 2 redShroom + 1 wheat + 1 rawBeef
		// EntityList Class for meta ids/mob ids
		for (int eggNum = 0; eggNum < numberOfEggs; eggNum++) {
			this.addIncubation(meatStacks[eggNum], foodStacks[eggNum],
					bodyStacks1[eggNum], bodyStacks2[eggNum],
					bodyStacks3[eggNum], results[eggNum]);
		}

		/*
		 * this.addIncubation( new ItemStack(Items.porkRaw, 1), new
		 * ItemStack(Items.carrot, 1), new ItemStack(Items.leather, 1), new
		 * ItemStack(Items.leather, 1), new ItemStack(Items.leather, 1), new
		 * ItemStack(Items.monsterPlacer, 1, 90)); this.addIncubation( new
		 * ItemStack(Items.beefRaw, 1), new ItemStack(Items.wheat, 1), new
		 * ItemStack(Items.leather, 1), new ItemStack(Items.leather, 1), new
		 * ItemStack(Items.leather, 1), new ItemStack(Items.monsterPlacer, 1,
		 * 92)); this.addIncubation( new ItemStack(Items.silk, 1), new
		 * ItemStack(Items.wheat, 1), new ItemStack(Blocks.cloth, 1), new
		 * ItemStack(Items.leather, 1), new ItemStack(Blocks.cloth, 1), new
		 * ItemStack(Items.monsterPlacer, 1, 91)); this.addIncubation( new
		 * ItemStack(Items.chickenRaw, 1), new ItemStack(Items.seeds, 1), new
		 * ItemStack(Items.feather, 1), new ItemStack(Items.feather, 1), new
		 * ItemStack(Items.feather, 1), new ItemStack(Items.monsterPlacer, 1,
		 * 93)); this.addIncubation( new ItemStack(Items.beefRaw, 1), new
		 * ItemStack(Items.wheat, 1), new ItemStack(Blocks.mushroomRed, 1), new
		 * ItemStack(Items.leather, 1), new ItemStack(Blocks.mushroomRed, 1),
		 * new ItemStack(Items.monsterPlacer, 1, 96));
		 */

	}

	public void addIncubation(ItemStack meatStack, ItemStack foodStack,
			ItemStack bodyStack1, ItemStack bodyStack2, ItemStack bodyStack3,
			ItemStack resultStack) {
		this.incubationList.put(new ItemStack[] { meatStack, foodStack,
				bodyStack1, bodyStack2, bodyStack3 }, resultStack);
	}

	public ItemStack getIncubationResult(ItemStack meatStack,
			ItemStack foodStack, ItemStack bodyStack1, ItemStack bodyStack2,
			ItemStack bodyStack3) {
		if (meatStack == null || foodStack == null || bodyStack1 == null
				|| bodyStack2 == null || bodyStack3 == null) {
			return null;
		}
		ItemStack ret = null;
		ret = (ItemStack) this.incubationList.get(new ItemStack[] { meatStack,
				foodStack, bodyStack1, bodyStack2, bodyStack3 });

		return ret;
	}

	public Map<ItemStack[], ItemStack> getIncubationList() {
		return incubationList;
	}

}
