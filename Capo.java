package com.countrygamer.capo;

import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.countrygamer.capo.blocks.BlockColorizer;
import com.countrygamer.capo.blocks.BlockEnderShard;
import com.countrygamer.capo.blocks.BlockInflixer;
import com.countrygamer.capo.blocks.BlockPlayerChecker;
import com.countrygamer.capo.blocks.BlockTeleBase;
import com.countrygamer.capo.blocks.tiles.TileEntityColorizer;
import com.countrygamer.capo.blocks.tiles.TileEntityEnderShard;
import com.countrygamer.capo.blocks.tiles.TileEntityInflixer;
import com.countrygamer.capo.blocks.tiles.TileEntityPlayerChecker;
import com.countrygamer.capo.blocks.tiles.TileEntityTele;
import com.countrygamer.capo.client.gui.GuiColorizer;
import com.countrygamer.capo.client.gui.GuiInflixer;
import com.countrygamer.capo.client.gui.GuiInventorySack;
import com.countrygamer.capo.client.gui.GuiPlayerChecker;
import com.countrygamer.capo.inventory.ContainerColorizer;
import com.countrygamer.capo.inventory.ContainerInflixer;
import com.countrygamer.capo.inventory.ContainerInventorySack;
import com.countrygamer.capo.inventory.InventorySack;
import com.countrygamer.capo.items.ItemCharm;
import com.countrygamer.capo.items.ItemInventorySack;
import com.countrygamer.capo.items.ItemMultiDye;
import com.countrygamer.capo.items.ItemMultiItem;
import com.countrygamer.capo.items.ItemTeleCore;
import com.countrygamer.capo.lib.EnumPartition;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.capo.packet.PacketSackName;
import com.countrygamer.capo.packet.PacketSaveDyeColor;
import com.countrygamer.capo.packet.PacketStorePlayerNames;
import com.countrygamer.capo.packet.PacketSubColorsTE;
import com.countrygamer.capo.packet.PacketTriggerInflixer;
import com.countrygamer.capo.proxy.ServerProxy;
import com.countrygamer.core.Core;
import com.countrygamer.core.Handler.PacketPipeline;
import com.countrygamer.core.Items.ItemBase;
import com.countrygamer.core.lib.CoreUtil;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MC_VERSION)
public class Capo implements IFuelHandler, IGuiHandler {
	
	public static Logger				log						= Logger.getLogger(Reference.MOD_NAME);
	@Instance(Reference.MOD_ID)
	public static Capo		instance;
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS,
			serverSide = Reference.SERVER_PROXY_CLASS)
	public static ServerProxy			proxy;
	
	// ~Items~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static Item					inventorySack;
	
	public static Item					multiItem;
	public static Item					multidye;
	
	public static Item					stableCore;
	public static String				stableCoreName			= "Stable Core";
	public static Item					unStableCore;
	public static String				unStableCoreName		= "UnStable Core";
	
	public static Item					talisman;
	public static Item					charm;
	public static final String[]		charmNames				= new String[] {
			"Health", "Hunger", "Speed", "Jumping", "Regeneration", "Heat", "Gills",
			"Invisibility", "Night Vision"
																};
	// ~Blocks~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static Block					endShard;
	
	public static Block					playerChecker;
	public static Block					entityDetector;
	
	// will hold the inventory in a tile ent
	public static Block					inventoryHolder;
	
	public static Block					inflixer;
	public static Block					colorizer;
	
	public static Block					teleporterBase;
	public static String				teleporterBaseName		= "Teleporter";
	
	// Mod
	// Compatibility~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public boolean						thermalExpansionLoaded	= false;
	// ~Other~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~Packet~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final PacketPipeline	packetChannel			= new PacketPipeline();
	// ~Tool
	// Mat~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static ToolMaterial			basicMat				= EnumHelper.addToolMaterial(
																		"multiTool", 0, 50, 2.0F,
																		0.0F, 0);
	
	// private static List<String> charmNames = new ArrayList();
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@EventHandler
	/**
	 * Before doing things with the mod.
	 * Do everything in here, unless it has to do with compatibility with other mods.
	 * @param event
	 */
	public void preInit(FMLPreInitializationEvent event) {
		this.doPreProxyThings();
		this.setupConfig(event);
		this.doProxyThings();
		this.registerHandlers();
		this.registerItems();
		this.registerBlocks();
		proxy.registerItemRender();
		this.registerCraftingRecipes();
		this.registerSmeltingRecipes();
		this.registerEntities();
		this.biomes();
		
	}
	
	public void doPreProxyThings() {
		proxy.registerRender();
	}
	
	public void setupConfig(FMLPreInitializationEvent event) {
	}
	
	public void doProxyThings() {
		proxy.registerThings();
	}
	
	public void registerHandlers() {
		MinecraftForge.EVENT_BUS.register(Capo.instance);
		GameRegistry.registerFuelHandler(Capo.instance);
		NetworkRegistry.INSTANCE.registerGuiHandler(Capo.instance,
				Capo.instance);
	}
	
	public void registerItems() {
		this.registerArmor();
		
		Capo.inventorySack = new ItemInventorySack(Reference.MOD_ID, "Player's Sack");
		Core.addItemToTab(Capo.inventorySack);
		
		Capo.multidye = new ItemMultiDye(Reference.MOD_ID, "MultiDye");
		Core.addItemToTab(Capo.multidye);
		
		Capo.multiItem = new ItemMultiItem(Reference.MOD_ID, "MultiItem");
		Core.addItemToTab(Capo.multiItem);
		
		Capo.unStableCore = new ItemTeleCore(Reference.MOD_ID,
				Capo.unStableCoreName);
		Core.addItemToTab(Capo.unStableCore);
		
		Capo.stableCore = new ItemTeleCore(Reference.MOD_ID,
				Capo.stableCoreName);
		Core.addItemToTab(Capo.stableCore);
		
		/*
		Capo.talisman = new ItemBase(Reference.MOD_ID, "Talisman");
		Core.addItemToTab(Capo.talisman);
		
		Capo.charm = new ItemCharm(Reference.MOD_ID, Capo.charmNames);
		Core.addItemToTab(Capo.charm);
		*/
	}
	
	public void registerArmor() {
	}
	
	public void registerBlocks() {
		TileEntity.addMapping(TileEntityEnderShard.class, Reference.MOD_ID + "_EnderShard");
		Capo.endShard = new BlockEnderShard(Material.rock, Reference.MOD_ID,
				"Ender Shard");
		Core.addBlockToTab(Capo.endShard);
		
		TileEntity.addMapping(TileEntityPlayerChecker.class, Reference.MOD_ID + "_PlayerChecker");
		Capo.playerChecker = new BlockPlayerChecker(Reference.MOD_ID, "Player Checker",
				TileEntityPlayerChecker.class);
		Core.addBlockToTab(Capo.playerChecker);
		
		TileEntity.addMapping(TileEntityColorizer.class, Reference.MOD_ID + "_Colorizer");
		Capo.colorizer = new BlockColorizer(Material.rock, Reference.MOD_ID,
				"Colorizer", TileEntityColorizer.class);
		Core.addBlockToTab(Capo.colorizer);
		
		TileEntity.addMapping(TileEntityInflixer.class, Reference.MOD_ID + "_Inflixer");
		Capo.inflixer = new BlockInflixer(Material.rock, Reference.MOD_ID, "Inflixer",
				TileEntityInflixer.class);
		Core.addBlockToTab(Capo.inflixer);
		
		TileEntity.addMapping(TileEntityTele.class, Reference.MOD_ID + "_Teleporter");
		Capo.teleporterBase = new BlockTeleBase(Material.rock, Reference.MOD_ID,
				Capo.teleporterBaseName);
		Core.addBlockToTab(Capo.teleporterBase);
		GameRegistry.addRecipe(new ItemStack(Capo.teleporterBase), new Object[] {
				"lpl", "lpl", "ooo", 'o', Blocks.obsidian, 'l', (new ItemStack(Items.dye, 1, 4)),
				'p', (new ItemStack(Items.dye, 1, 5))
		});
		
	}
	
	public void registerCraftingRecipes() {
		GameRegistry.addRecipe(new ItemStack(Capo.endShard, 1), new Object[] {
				" x ", "xcx", "vvv", 'x', Items.ender_pearl, 'c', Items.ghast_tear, 'v',
				Blocks.obsidian
		});
		
		GameRegistry.addRecipe(
				new ItemStack(Capo.unStableCore),
				new Object[] {
						"o o", " m ", "o o", Character.valueOf('o'), Blocks.obsidian,
						Character.valueOf('m'), Items.map,
				});
		GameRegistry.addRecipe(
				new ItemStack(Capo.stableCore),
				new Object[] {
						"eee", "ece", "eee", Character.valueOf('e'), Items.ender_pearl,
						Character.valueOf('c'), Capo.unStableCore
				});
		
		/*
		GameRegistry.addRecipe(new ItemStack(Capo.talisman), new Object[] {
				"sgs", "g g", " g ", Character.valueOf('s'), Items.string, Character.valueOf('g'),
				Items.gold_ingot
		});
		
		ItemStack[] potions = new ItemStack[] {
				new ItemStack(Items.potionitem, 1, 8229), null,
				new ItemStack(Items.potionitem, 1, 8258), null,
				new ItemStack(Items.potionitem, 1, 8257), new ItemStack(Items.potionitem, 1, 8259),
				new ItemStack(Items.potionitem, 1, 8269), new ItemStack(Items.potionitem, 1, 8270),
				new ItemStack(Items.potionitem, 1, 8262)
		};
		for (int i = 0; i < Capo.charmNames.length; i++) {
			if (i == 1) {
				GameRegistry.addRecipe(new ItemStack(Capo.charm, 1, i),
						new Object[] {
								"lol", "ptp", "lol", Character.valueOf('l'),
								new ItemStack(Items.dye, 1, 4), Character.valueOf('t'),
								Capo.talisman, Character.valueOf('p'), potions[0],
								Character.valueOf('o'), Items.baked_potato
						});
			}
			else if (i == 3) {
				GameRegistry.addRecipe(new ItemStack(Capo.charm, 1, i),
						new Object[] {
								"lol", "ptp", "lol", Character.valueOf('l'),
								new ItemStack(Items.dye, 1, 4), Character.valueOf('t'),
								Capo.talisman, Character.valueOf('p'), potions[2],
								Character.valueOf('o'), Blocks.piston
						});
			}
			else {
				GameRegistry.addRecipe(new ItemStack(Capo.charm, 1, i), new Object[] {
						"lpl", "ptp", "lpl", Character.valueOf('l'),
						new ItemStack(Items.dye, 1, 4), Character.valueOf('t'),
						Capo.talisman, Character.valueOf('p'), potions[i]
				});
			}
		}
		*/
		
	}
	
	public void registerSmeltingRecipes() {
	}
	
	public void registerEntities() {
	}
	
	public void biomes() {
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.packetChannel.initalise(Reference.MOD_ID);
		this.packetChannel.registerPacket(PacketStorePlayerNames.class);
		this.packetChannel.registerPacket(PacketSackName.class);
		this.packetChannel.registerPacket(PacketSaveDyeColor.class);
		this.packetChannel.registerPacket(PacketSubColorsTE.class);
		this.packetChannel.registerPacket(PacketTriggerInflixer.class);
		
		this.Cofh_ThermalExpansion();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.packetChannel.postInitialise();
	}
	
	// ~Events~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@SubscribeEvent
	public void blockHarvestEvent(HarvestDropsEvent event) {
		if (event.isSilkTouching) Capo.log.info("silk touch");
		EntityPlayer player = event.harvester;
		if (player != null) {
			ItemStack heldStack = player.getHeldItem();
			if (heldStack != null && heldStack.getItem() instanceof ItemMultiItem) {
				ItemStack usageStack = new ItemStack(Items.lava_bucket);
				if (ItemMultiItem.hasItemInflixed(heldStack, usageStack)) {
					
					ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(
							new ItemStack(event.block));
					
					if (itemstack != null) {
						event.drops.clear();
						event.drops.add(itemstack.copy());
						event.dropChance = 1.0F;
						
						ItemStack multiStack = heldStack.copy();
						NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(multiStack);
						if (ItemMultiItem.takeStack
								&& ItemMultiItem.getPartition(multiStack) != EnumPartition.BUCKET) {
							multiStack = ItemMultiItem.setStackInSlot(multiStack,
									ItemMultiItem.getSlotOfStack(multiStack, usageStack),
									new ItemStack(Items.bucket));
						}
						
						player.setCurrentItemOrArmor(0, multiStack);
						
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void pre(RenderPlayerEvent.Pre event) {
		EntityPlayer player = event.entityPlayer;
		if (player != null) {
			if (player.inventory.hasItemStack(new ItemStack(Capo.charm, 1, 7))) {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void entityHurt(LivingHurtEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (player != null) {
				if (player.inventory.hasItemStack(new ItemStack(Capo.charm, 1, 5))) {
					if (event.source.isFireDamage()) {
						player.extinguish();
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	// taken from Azanor's Thaumcraft (Boots of the Traveller setup)
	public HashMap<Integer, Float>	entIDToStepHeight	= new HashMap();
	
	@SubscribeEvent
	public void livingTick(LivingEvent.LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (player.worldObj.isRemote
					&& this.entIDToStepHeight.containsKey(player.getEntityId())) {
				player.stepHeight = this.entIDToStepHeight.get(player.getEntityId()).floatValue();
				this.entIDToStepHeight.remove(player.getEntityId());
			}
		}
	}
	
	// end taken
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~Fuel Handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public int getBurnTime(ItemStack fuel) {
		return 0;
	}
	
	// ~Gui Handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEnt = world.getTileEntity(x, y, z);
		if (tileEnt instanceof TileEntityColorizer && ID == Reference.guiColorizer) {
			return new ContainerColorizer(player.inventory, (TileEntityColorizer) tileEnt);
		}
		else if (tileEnt instanceof TileEntityInflixer && ID == Reference.guiInflixer) {
			return new ContainerInflixer(player.inventory, (TileEntityInflixer) tileEnt);
		}
		else if (ID == Reference.guiInvSack) {
			return new ContainerInventorySack(player, player.inventory, new InventorySack(
					player.getHeldItem()));
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEnt = world.getTileEntity(x, y, z);
		if (tileEnt instanceof TileEntityPlayerChecker && ID == Reference.guiPlayerChecker) {
			return new GuiPlayerChecker((TileEntityPlayerChecker) tileEnt);
		}
		else if (tileEnt instanceof TileEntityColorizer && ID == Reference.guiColorizer) {
			return new GuiColorizer(player, (TileEntityColorizer) tileEnt);
		}
		else if (tileEnt instanceof TileEntityInflixer && ID == Reference.guiInflixer) {
			return new GuiInflixer(player, (TileEntityInflixer) tileEnt);
		}
		else if (ID == Reference.guiInvSack) {
			return new GuiInventorySack(player, player.inventory, new InventorySack(
					player.getHeldItem()));
		}
		return null;
	}
	
	// ~Mod Compatibility~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private void Cofh_ThermalExpansion() {
		if (CoreUtil.isModLoaded("ThermalExpansion")) {
			this.thermalExpansionLoaded = true;
			Capo.log.info("\n\n");
			Capo.log.info("ThermalExpansion Loaded and Ready");
			try {
				// 250 = 1 ender pearl
				FluidStack enderStack = FluidRegistry.getFluidStack("ender", 1000);
				
				NBTTagCompound toSend = new NBTTagCompound();
				toSend.setInteger("energy", 0);
				toSend.setTag("input", new NBTTagCompound());
				toSend.setTag("output", new NBTTagCompound());
				toSend.setTag("fluid", new NBTTagCompound());
				
				(new ItemStack(Items.stick)).writeToNBT(toSend.getCompoundTag("input"));
				Capo.log.info("Input Ready");
				(new ItemStack(Items.apple)).writeToNBT(toSend.getCompoundTag("output"));
				Capo.log.info("Output Ready");
				toSend.setBoolean("reversible", false);
				enderStack.writeToNBT(toSend.getCompoundTag("fluid"));
				FMLInterModComms.sendMessage("ThermalExpansion", "TransposerFillRecipe", toSend);
				Capo.log.info("Sent Recipie");
			} catch (Exception e) {
				System.err.println(e.getStackTrace());
				
			}
			
		}
	}
	
	// ~Other~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
