package com.countrygamer.capo.common;

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

import com.countrygamer.capo.client.gui.GuiAssembler;
import com.countrygamer.capo.client.gui.GuiAssemblerSettings;
import com.countrygamer.capo.client.gui.GuiColorizer;
import com.countrygamer.capo.client.gui.GuiColorizerII;
import com.countrygamer.capo.client.gui.GuiCompressor;
import com.countrygamer.capo.client.gui.GuiInflixer;
import com.countrygamer.capo.client.gui.GuiInventorySack;
import com.countrygamer.capo.client.gui.GuiModuleBase;
import com.countrygamer.capo.client.gui.GuiPlayerChecker;
import com.countrygamer.capo.common.block.BlockColorizer;
import com.countrygamer.capo.common.block.BlockColorizerII;
import com.countrygamer.capo.common.block.BlockCompressor;
import com.countrygamer.capo.common.block.BlockEnderShard;
import com.countrygamer.capo.common.block.BlockInflixer;
import com.countrygamer.capo.common.block.BlockIotaTable;
import com.countrygamer.capo.common.block.BlockMineralExtractor;
import com.countrygamer.capo.common.block.BlockModuleAssembler;
import com.countrygamer.capo.common.block.BlockModuleBase;
import com.countrygamer.capo.common.block.BlockMultiPipe;
import com.countrygamer.capo.common.block.BlockPlayerChecker;
import com.countrygamer.capo.common.block.BlockTeleBase;
import com.countrygamer.capo.common.block.BlockWall;
import com.countrygamer.capo.common.inventory.InventorySack;
import com.countrygamer.capo.common.inventory.container.ContainerAssembler;
import com.countrygamer.capo.common.inventory.container.ContainerAssemblerSettings;
import com.countrygamer.capo.common.inventory.container.ContainerColorizer;
import com.countrygamer.capo.common.inventory.container.ContainerColorizerII;
import com.countrygamer.capo.common.inventory.container.ContainerCompressor;
import com.countrygamer.capo.common.inventory.container.ContainerInflixer;
import com.countrygamer.capo.common.inventory.container.ContainerInventorySack;
import com.countrygamer.capo.common.inventory.container.ContainerModuleBase;
import com.countrygamer.capo.common.item.ItemCharm;
import com.countrygamer.capo.common.item.ItemInventorySack;
import com.countrygamer.capo.common.item.ItemModuleWall;
import com.countrygamer.capo.common.item.ItemMultiDye;
import com.countrygamer.capo.common.item.ItemMultiItem;
import com.countrygamer.capo.common.item.ItemTeleCore;
import com.countrygamer.capo.common.item.ItemTriDye;
import com.countrygamer.capo.common.item.ItemVainer;
import com.countrygamer.capo.common.lib.EnumPartition;
import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.packet.PacketCompressorMode;
import com.countrygamer.capo.common.packet.PacketOpenAssemblerSettings;
import com.countrygamer.capo.common.packet.PacketSackName;
import com.countrygamer.capo.common.packet.PacketSaveDyeColor;
import com.countrygamer.capo.common.packet.PacketStorePlayerNames;
import com.countrygamer.capo.common.packet.PacketSubColorsTE;
import com.countrygamer.capo.common.packet.PacketTriggerAssembler;
import com.countrygamer.capo.common.packet.PacketTriggerColorizerII;
import com.countrygamer.capo.common.packet.PacketTriggerInflixer;
import com.countrygamer.capo.common.tileentity.TileEntityAssembler;
import com.countrygamer.capo.common.tileentity.TileEntityColorizer;
import com.countrygamer.capo.common.tileentity.TileEntityColorizerII;
import com.countrygamer.capo.common.tileentity.TileEntityCompressor;
import com.countrygamer.capo.common.tileentity.TileEntityEnderShard;
import com.countrygamer.capo.common.tileentity.TileEntityExtractor;
import com.countrygamer.capo.common.tileentity.TileEntityInflixer;
import com.countrygamer.capo.common.tileentity.TileEntityIotaTable;
import com.countrygamer.capo.common.tileentity.TileEntityModuleBase;
import com.countrygamer.capo.common.tileentity.TileEntityMultiPipe;
import com.countrygamer.capo.common.tileentity.TileEntityPlayerChecker;
import com.countrygamer.capo.common.tileentity.TileEntityTele;
import com.countrygamer.capo.common.tileentity.TileEntityWall;
import com.countrygamer.core.Base.common.item.ItemBase;
import com.countrygamer.core.Base.common.packet.PacketPipeline;
import com.countrygamer.core.common.Core;
import com.countrygamer.core.common.craftingsystem.DiagramRecipes;
import com.countrygamer.core.common.lib.CoreUtil;

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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MC_VERSION)
public class Capo implements IFuelHandler, IGuiHandler {
	
	public static Logger log = Logger.getLogger(Reference.MOD_NAME);
	
	@Instance(Reference.MOD_ID)
	public static Capo instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS,
			serverSide = Reference.SERVER_PROXY_CLASS)
	public static ServerProxy proxy;
	
	// ~Items~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static Item inventorySack;
	
	public static Item multiItem;
	public static Item multidye;
	public static Item tridye;
	
	public static Item stableCore;
	public static Item unStableCore;
	
	public static Item talisman;
	public static Item charm;
	public static final String[] charmNames = new String[] {
			"Health", "Hunger", "Speed", "Jumping", "Regeneration", "Heat", "Gills",
			"Invisibility", "Night Vision"
	};
	
	public static Item vainer;
	
	public static Item moduleSentry;
	public static Item moduleWall;
	
	// ~Blocks~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static Block endShard;
	
	public static Block playerChecker;
	public static Block entityDetector;
	
	public static Block inflixer;
	public static Block colorizer;
	public static Block colorizerII;
	public static Block frequencySetter;
	
	public static Block teleporterBase;
	
	public static Block multiPipe_Energy;
	public static Block multiPipe_Item;
	public static Block multiPipe_Liquid;
	
	public static Block iotationTable;
	
	public static Block mineralExtractor;
	public static Block compressor;
	
	public static Block moduleBase;
	public static Block wallBlock;
	
	public static Block moduleAssembler;
	
	// Mod
	// Compatibility~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public boolean thermalExpansionLoaded = false;
	// ~Other~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~Packet~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final PacketPipeline packetChannel = new PacketPipeline();
	// ~Tool
	// Mat~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static ToolMaterial basicMat = EnumHelper.addToolMaterial("multiTool", 0, 50, 2.0F,
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
		NetworkRegistry.INSTANCE.registerGuiHandler(Capo.instance, Capo.instance);
	}
	
	public void registerItems() {
		this.registerArmor();
		
		Capo.inventorySack = new ItemInventorySack(Reference.MOD_ID, "Player's Sack");
		Core.addItemToTab(Capo.inventorySack);
		
		Capo.multidye = new ItemMultiDye(Reference.MOD_ID, "MultiDye");
		Core.addItemToTab(Capo.multidye);
		
		Capo.multiItem = new ItemMultiItem(Reference.MOD_ID, "MultiItem");
		Core.addItemToTab(Capo.multiItem);
		
		Capo.unStableCore = new ItemTeleCore(Reference.MOD_ID, "UnStable Core");
		Core.addItemToTab(Capo.unStableCore);
		
		Capo.stableCore = new ItemTeleCore(Reference.MOD_ID, "Stable Core");
		Core.addItemToTab(Capo.stableCore);
		
		Capo.talisman = new ItemBase(Reference.MOD_ID, "Talisman");
		Core.addItemToTab(Capo.talisman);
		
		Capo.charm = new ItemCharm(Reference.MOD_ID, Capo.charmNames);
		Core.addItemToTab(Capo.charm);
		
		Capo.vainer = new ItemVainer(Reference.MOD_ID, "Vainer", EnumHelper.addToolMaterial(
				"VainerMat", ToolMaterial.EMERALD.getHarvestLevel(),
				(int) (ToolMaterial.EMERALD.getMaxUses() * 0.75),
				ToolMaterial.IRON.getEfficiencyOnProperMaterial(),
				ToolMaterial.EMERALD.getDamageVsEntity(), ToolMaterial.GOLD.getEnchantability()));
		Core.addItemToTab(Capo.vainer);
		
		Capo.tridye = new ItemTriDye(Reference.MOD_ID, "Tri-Dye");
		Core.addItemToTab(Capo.tridye);
		
		// Capo.moduleSentry;
		
		Capo.moduleWall = new ItemModuleWall(Reference.MOD_ID, "Module Wall");
		Core.addItemToTab(Capo.moduleWall);
		
	}
	
	public void registerArmor() {
	}
	
	public void registerBlocks() {
		TileEntity.addMapping(TileEntityEnderShard.class, Reference.MOD_ID + "_EnderShard");
		Capo.endShard = new BlockEnderShard(Material.ground, Reference.MOD_ID, "Ender Shard");
		Core.addBlockToTab(Capo.endShard);
		
		TileEntity.addMapping(TileEntityPlayerChecker.class, Reference.MOD_ID + "_PlayerChecker");
		Capo.playerChecker = new BlockPlayerChecker(Reference.MOD_ID, "Player Checker",
				TileEntityPlayerChecker.class);
		Core.addBlockToTab(Capo.playerChecker);
		
		TileEntity.addMapping(TileEntityInflixer.class, Reference.MOD_ID + "_Inflixer");
		Capo.inflixer = new BlockInflixer(Material.ground, Reference.MOD_ID, "Inflixer",
				TileEntityInflixer.class);
		Core.addBlockToTab(Capo.inflixer);
		
		TileEntity.addMapping(TileEntityColorizer.class, Reference.MOD_ID + "_Colorizer");
		Capo.colorizer = new BlockColorizer(Material.ground, Reference.MOD_ID, "Colorizer",
				TileEntityColorizer.class);
		Core.addBlockToTab(Capo.colorizer);
		
		TileEntity.addMapping(TileEntityColorizerII.class, "");
		Capo.colorizerII = new BlockColorizerII(Material.ground, Reference.MOD_ID,
				"Colorizer Mark II", TileEntityColorizerII.class);
		Core.addBlockToTab(Capo.colorizerII);
		
		TileEntity.addMapping(TileEntityTele.class, Reference.MOD_ID + "_Teleporter");
		Capo.teleporterBase = new BlockTeleBase(Material.ground, Reference.MOD_ID, "Teleporter");
		Core.addBlockToTab(Capo.teleporterBase);
		
		TileEntity.addMapping(TileEntityMultiPipe.class, Reference.MOD_ID + "_MultiPipe");
		Capo.multiPipe_Energy = new BlockMultiPipe(Reference.MOD_ID, "MPE",
				BlockMultiPipe.EnumPipeType.ENERGY);
		Core.addBlockToTab(Capo.multiPipe_Energy);
		Capo.multiPipe_Item = new BlockMultiPipe(Reference.MOD_ID, "MPI",
				BlockMultiPipe.EnumPipeType.ITEM);
		Core.addBlockToTab(Capo.multiPipe_Item);
		Capo.multiPipe_Liquid = new BlockMultiPipe(Reference.MOD_ID, "MPL",
				BlockMultiPipe.EnumPipeType.LIQUID);
		Core.addBlockToTab(Capo.multiPipe_Liquid);
		
		TileEntity.addMapping(TileEntityIotaTable.class, Reference.MOD_ID + "_IotaTable");
		Capo.iotationTable = new BlockIotaTable(Material.ground, Reference.MOD_ID, "Crushing",
				TileEntityIotaTable.class);
		Capo.iotationTable.setHardness(0.8F).setResistance(0.5F);
		Core.addBlockToTab(Capo.iotationTable);
		GameRegistry.addRecipe(new ItemStack(Capo.iotationTable), "sls", "cqc", "c c", 's',
				Items.stick, 'l', new ItemStack(Blocks.stone_slab, 1, 0), 'c', Blocks.cobblestone,
				'q', new ItemStack(Blocks.stone_slab, 1, 3));
		
		TileEntity.addMapping(TileEntityExtractor.class, Reference.MOD_ID + "_Extractor");
		Capo.mineralExtractor = new BlockMineralExtractor(Material.ground, Reference.MOD_ID,
				"Mineral Extractor", TileEntityExtractor.class);
		Capo.mineralExtractor.setHardness(0.8F).setResistance(0.5F);
		Core.addBlockToTab(Capo.mineralExtractor);
		
		TileEntity.addMapping(TileEntityCompressor.class, Reference.MOD_ID + "_Compressor");
		Capo.compressor = new BlockCompressor(Material.ground, Reference.MOD_ID, "Compressor",
				TileEntityCompressor.class);
		Capo.compressor.setHardness(0.8F).setResistance(0.5F);
		Core.addBlockToTab(Capo.compressor);
		
		TileEntity.addMapping(TileEntityModuleBase.class, Reference.MOD_ID + "_ModuleBase");
		Capo.moduleBase = new BlockModuleBase(Material.ground, Reference.MOD_ID, "Module Base",
				TileEntityModuleBase.class);
		Core.addBlockToTab(Capo.moduleBase);
		
		TileEntity.addMapping(TileEntityWall.class, Reference.MOD_ID + "_Wall");
		Capo.wallBlock = new BlockWall(Material.ground, Reference.MOD_ID, "Wall",
				TileEntityWall.class);
		
		TileEntity.addMapping(TileEntityAssembler.class, Reference.MOD_ID + "_ModuleAssembler");
		Capo.moduleAssembler = new BlockModuleAssembler(Material.ground, Reference.MOD_ID,
				"Module Assembler", TileEntityAssembler.class);
		Core.addBlockToTab(Capo.moduleAssembler);
		
	}
	
	public void registerCraftingRecipes() {
		this.craftingItems();
		this.craftingBlocks();
	}
	
	public void craftingItems() {
		GameRegistry.addRecipe(new ItemStack(Capo.endShard, 1), " x ", "xcx", "vvv", 'x',
				Items.ender_pearl, 'c', Items.ghast_tear, 'v', Blocks.obsidian);
		
		GameRegistry.addRecipe(new ItemStack(Capo.inventorySack), "s s", "wcw", "www", 's',
				Items.string, 'w', Blocks.wool, 'c', Blocks.chest);
		
		GameRegistry.addRecipe(new ItemStack(Capo.multiItem), "i i", " s ", "scs", 'i',
				Items.iron_ingot, 's', Items.stick, 'c', Blocks.chest);
		
		GameRegistry.addRecipe(new ItemStack(Capo.unStableCore), "o o", " m ", "o o", 'o',
				Blocks.obsidian, 'm', Items.map);
		GameRegistry.addRecipe(new ItemStack(Capo.stableCore), "eee", "ece", "eee", 'e',
				Items.ender_pearl, 'c', Capo.unStableCore);
		
		GameRegistry.addRecipe(new ItemStack(Capo.talisman), "sgs", "g g", " g ", 's',
				Items.string, 'g', Items.gold_ingot);
		
		GameRegistry.addRecipe(new ItemStack(Capo.vainer), "ddd", "wgw", "ses", 'd', Items.diamond,
				'w', Blocks.wool, 'g', new ItemStack(Items.golden_pickaxe), 's', Items.string, 'e',
				Items.emerald);
		
		ItemStack[] potions = new ItemStack[] {
				new ItemStack(Items.potionitem, 1, 8229), null,
				new ItemStack(Items.potionitem, 1, 8258), null,
				new ItemStack(Items.potionitem, 1, 8257), new ItemStack(Items.potionitem, 1, 8259),
				new ItemStack(Items.potionitem, 1, 8269), new ItemStack(Items.potionitem, 1, 8270),
				new ItemStack(Items.potionitem, 1, 8262)
		};
		for (int i = 0; i < Capo.charmNames.length; i++) {
			if (i == 1) {
				GameRegistry.addRecipe(new ItemStack(Capo.charm, 1, i), "lol", "ptp", "lol", 'l',
						new ItemStack(Items.dye, 1, 4), 't', Capo.talisman, 'p', potions[0], 'o',
						Items.baked_potato);
			}
			else if (i == 3) {
				GameRegistry.addRecipe(new ItemStack(Capo.charm, 1, i), "lol", "ptp", "lol", 'l',
						new ItemStack(Items.dye, 1, 4), 't', Capo.talisman, 'p', potions[2], 'o',
						Blocks.piston);
			}
			else {
				GameRegistry.addRecipe(new ItemStack(Capo.charm, 1, i), "lpl", "ptp", "lpl", 'l',
						new ItemStack(Items.dye, 1, 4), 't', Capo.talisman, 'p', potions[i]);
			}
		}
	}
	
	public void craftingBlocks() {
		GameRegistry.addRecipe(new ItemStack(Capo.endShard), " g ", "geg", "ooo", 'g',
				Items.ghast_tear, 'e', Items.ender_pearl, 'o', Blocks.obsidian);
		
		GameRegistry.addRecipe(new ItemStack(Capo.colorizer), "ppp", "sns", "sws", 'p',
				Items.paper, 's', Items.stick, 'n', Capo.inflixer, 'w', Blocks.planks);
		
		GameRegistry.addRecipe(new ItemStack(Capo.inflixer), "sss", "iwi", "ici", 's',
				Blocks.wooden_slab, 'i', Items.iron_ingot, 'w', Blocks.crafting_table, 'c',
				Blocks.chest);
		
		GameRegistry.addRecipe(new ItemStack(Capo.teleporterBase), "lpl", "lpl", "ooo", 'o',
				Blocks.obsidian, 'l', (new ItemStack(Items.dye, 1, 4)), 'p', (new ItemStack(
						Items.dye, 1, 5)));
		
		GameRegistry.addRecipe(new ItemStack(Capo.iotationTable), "sss", "ccc", "c c", 's',
				Blocks.stone, 'c', Blocks.cobblestone);
		
		GameRegistry.addRecipe(new ItemStack(Capo.mineralExtractor), "cgc", "iii", "ipi", 'c',
				Blocks.cobblestone, 'g', Items.gold_ingot, 'i', Items.iron_ingot, 'p',
				Items.iron_pickaxe);
		
		GameRegistry.addRecipe(new ItemStack(Capo.compressor), "wpw", "i i", "ici", 'w',
				Blocks.planks, 'p', Blocks.piston, 'i', Items.iron_ingot, 'c', Blocks.cobblestone);
		
	}
	
	public void registerSmeltingRecipes() {
	}
	
	public void registerEntities() {
	}
	
	public void biomes() {
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Capo.packetChannel.initalise(Reference.MOD_ID);
		Capo.packetChannel.registerPacket(PacketStorePlayerNames.class);
		Capo.packetChannel.registerPacket(PacketSackName.class);
		Capo.packetChannel.registerPacket(PacketSaveDyeColor.class);
		Capo.packetChannel.registerPacket(PacketSubColorsTE.class);
		Capo.packetChannel.registerPacket(PacketTriggerInflixer.class);
		Capo.packetChannel.registerPacket(PacketCompressorMode.class);
		Capo.packetChannel.registerPacket(PacketTriggerColorizerII.class);
		Capo.packetChannel.registerPacket(PacketTriggerAssembler.class);
		Capo.packetChannel.registerPacket(PacketOpenAssemblerSettings.class);
		
		this.Cofh_ThermalExpansion();
		
		DiagramRecipes.Recipe iotaTableRecipe = new DiagramRecipes.Recipe(Capo.iotationTable, 0);
		this.generateIotaTableComponents(iotaTableRecipe);
		DiagramRecipes.addRecipe("IotaTable", iotaTableRecipe);
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Capo.packetChannel.postInitialise();
	}
	
	// ~Events~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@SubscribeEvent
	public void blockHarvestEvent(HarvestDropsEvent event) {
		// if (event.isSilkTouching) Capo.log.info("silk touch");
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
						if (ItemMultiItem.takeStack
								&& ItemMultiItem.getPartition(multiStack) != EnumPartition.LAVABUCKET) {
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
	@SideOnly(Side.CLIENT)
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
		if (event.entity != null && event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (player.inventory.hasItemStack(new ItemStack(Capo.charm, 1, 5))) {
				if (event.source.isFireDamage()) {
					player.extinguish();
					event.setCanceled(true);
				}
			}
		}
	}
	
	// taken from Azanor's Thaumcraft (Boots of the Traveller setup)
	public HashMap<Integer, Float> entIDToStepHeight = new HashMap<Integer, Float>();
	
	@SubscribeEvent
	public void livingTick(LivingEvent.LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (player.worldObj.isRemote
					&& this.entIDToStepHeight.containsKey(player.getEntityId())) {
				player.stepHeight = this.entIDToStepHeight.get(player.getEntityId());
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
		else if (tileEnt instanceof TileEntityCompressor && ID == Reference.guiCompressor) {
			return new ContainerCompressor(player.inventory, (TileEntityCompressor) tileEnt);
		}
		else if (tileEnt instanceof TileEntityAssembler && ID == Reference.guiAssembler) {
			return new ContainerAssembler(player.inventory, (TileEntityAssembler) tileEnt);
		}
		else if (tileEnt instanceof TileEntityAssembler && ID == Reference.guiAssemblerSettings) {
			return new ContainerAssemblerSettings(player.inventory, (TileEntityAssembler)tileEnt);
		}
		else if (ID == Reference.guiInvSack) {
			return new ContainerInventorySack(player, player.inventory, new InventorySack(
					player.getHeldItem()));
		}
		else if (tileEnt instanceof TileEntityColorizerII && ID == Reference.guiColorizerII) {
			return new ContainerColorizerII(player.inventory, (TileEntityColorizerII) tileEnt);
		}
		else if (tileEnt instanceof TileEntityModuleBase && ID == Reference.guiModuleBase) {
			return new ContainerModuleBase(player.inventory, (TileEntityModuleBase) tileEnt);
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
		else if (tileEnt instanceof TileEntityCompressor && ID == Reference.guiCompressor) {
			return new GuiCompressor(player, (TileEntityCompressor) tileEnt);
		}
		else if (tileEnt instanceof TileEntityAssembler && ID == Reference.guiAssembler) {
			return new GuiAssembler(player, (TileEntityAssembler) tileEnt);
		}
		else if (tileEnt instanceof TileEntityAssembler && ID == Reference.guiAssemblerSettings) {
			return new GuiAssemblerSettings(player, new ContainerAssemblerSettings(
					player.inventory, (TileEntityAssembler)tileEnt));
		}
		else if (ID == Reference.guiInvSack) {
			return new GuiInventorySack(player, player.inventory, new InventorySack(
					player.getHeldItem()));
		}
		else if (tileEnt instanceof TileEntityColorizerII && ID == Reference.guiColorizerII) {
			return new GuiColorizerII(player, (TileEntityColorizerII) tileEnt);
		}
		else if (tileEnt instanceof TileEntityModuleBase && ID == Reference.guiModuleBase) {
			return new GuiModuleBase(player, (TileEntityModuleBase) tileEnt);
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
	
	// ~Core Diagram Recipes~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private void generateIotaTableComponents(DiagramRecipes.Recipe recipe) {
		DiagramRecipes.Recipe.RecipeComponent legNE = new DiagramRecipes.Recipe.RecipeComponent(
				"Leg NE", recipe, new ItemStack(Blocks.cobblestone));
		legNE.addSidedUVArea(2, 0.8F, 1.0F, 0.0F, 0.6F);
		legNE.addSidedUVArea(5, 0.0F, 0.2F, 0.0F, 0.6F);
		legNE.addSidedUVArea(0, 0.8F, 1.0F, 0.0F, 0.2F);
		recipe.addRecipeComponent(legNE);
		
		DiagramRecipes.Recipe.RecipeComponent legSE = new DiagramRecipes.Recipe.RecipeComponent(
				"Leg SE", recipe, new ItemStack(Blocks.cobblestone));
		legSE.addSidedUVArea(5, 0.8F, 1.0F, 0.0F, 0.6F);
		legSE.addSidedUVArea(3, 0.8F, 1.0F, 0.0F, 0.6F);
		legSE.addSidedUVArea(0, 0.8F, 1.0F, 0.8F, 1.0F);
		recipe.addRecipeComponent(legSE);
		
		DiagramRecipes.Recipe.RecipeComponent legSW = new DiagramRecipes.Recipe.RecipeComponent(
				"Leg SW", recipe, new ItemStack(Blocks.cobblestone));
		legSW.addSidedUVArea(3, 0.0F, 0.2F, 0.0F, 0.6F);
		legSW.addSidedUVArea(4, 0.8F, 1.0F, 0.0F, 0.6F);
		legSW.addSidedUVArea(0, 0.0F, 0.2F, 0.8F, 1.0F);
		recipe.addRecipeComponent(legSW);
		
		DiagramRecipes.Recipe.RecipeComponent legNW = new DiagramRecipes.Recipe.RecipeComponent(
				"Leg NW", recipe, new ItemStack(Blocks.cobblestone));
		legNW.addSidedUVArea(4, 0.0F, 0.2F, 0.0F, 0.6F);
		legNW.addSidedUVArea(2, 0.0F, 0.2F, 0.0F, 0.6F);
		legNW.addSidedUVArea(0, 0.0F, 0.2F, 0.0F, 0.2F);
		recipe.addRecipeComponent(legNW);
		
		DiagramRecipes.Recipe.RecipeComponent cobbleBase = new DiagramRecipes.Recipe.RecipeComponent(
				"CobbleSlab", recipe, new ItemStack(Blocks.stone_slab, 1, 3));
		for (int i = 2; i < 6; i++)
			cobbleBase.addSidedUVArea(i, 0.0F, 1.0F, 0.6F, 0.83F);
		recipe.addRecipeComponent(cobbleBase);
		
		DiagramRecipes.Recipe.RecipeComponent stoneBase = new DiagramRecipes.Recipe.RecipeComponent(
				"StoneSlab", recipe, new ItemStack(Blocks.stone_slab, 1, 0));
		for (int i = 2; i < 6; i++)
			stoneBase.addSidedUVArea(i, 0.0F, 1.0F, 0.83F, 0.93F);
		stoneBase.addSidedUVArea(1, 0.1F, 0.9F, 0.1F, 0.9F);
		recipe.addRecipeComponent(stoneBase);
		
		DiagramRecipes.Recipe.RecipeComponent stickN = new DiagramRecipes.Recipe.RecipeComponent(
				"Stick N", recipe, new ItemStack(Items.stick));
		stickN.addSidedUVArea(2, 0.0F, 1.0F, 0.93F, 1.0F);
		stickN.addSidedUVArea(1, 0.0F, 1.0F, 0.0F, 0.1F);
		recipe.addRecipeComponent(stickN);
		
		DiagramRecipes.Recipe.RecipeComponent stickE = new DiagramRecipes.Recipe.RecipeComponent(
				"Stick E", recipe, new ItemStack(Items.stick));
		stickE.addSidedUVArea(5, 0.0F, 1.0F, 0.93F, 1.0F);
		stickE.addSidedUVArea(1, 0.9F, 1.0F, 0.0F, 1.0F);
		recipe.addRecipeComponent(stickE);
		
		DiagramRecipes.Recipe.RecipeComponent stickS = new DiagramRecipes.Recipe.RecipeComponent(
				"Stick S", recipe, new ItemStack(Items.stick));
		stickS.addSidedUVArea(3, 0.0F, 1.0F, 0.93F, 1.0F);
		stickS.addSidedUVArea(1, 0.0F, 1.0F, 0.0F, 1.0F);
		recipe.addRecipeComponent(stickS);
		
		DiagramRecipes.Recipe.RecipeComponent stickW = new DiagramRecipes.Recipe.RecipeComponent(
				"Stick W", recipe, new ItemStack(Items.stick));
		stickW.addSidedUVArea(4, 0.0F, 1.0F, 0.93F, 1.0F);
		stickW.addSidedUVArea(1, 0.0F, 0.1F, 0.0F, 1.0F);
		recipe.addRecipeComponent(stickW);
		
	}
	// ~Other~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
