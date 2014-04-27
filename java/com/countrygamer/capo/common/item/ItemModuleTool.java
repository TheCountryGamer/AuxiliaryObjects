package com.countrygamer.capo.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.tileentity.TileEntityModuleBase;
import com.countrygamer.capo.common.tileentity.TileEntityWall;
import com.countrygamer.core.Base.common.item.ItemBase;

public class ItemModuleTool extends ItemBase {
	
	public ItemModuleTool(String modid, String name) {
		super(modid, name);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y,
			int z, int side, float xf, float yf, float zf) {
		//Capo.log.info("" + side);
		if (world.getBlock(x, y, z) == Capo.wallBlock) {
			int[][] coords = this.getCoordsOfWalls(world, x, y, z, side);
			
			for (int[] coord : coords) {
				int x1 = coord[0];
				int y1 = coord[1];
				int z1 = coord[2];
				
				TileEntity tileEnt = world.getTileEntity(x1, y1, z1);
				if (tileEnt != null && tileEnt instanceof TileEntityWall) {
					TileEntityWall tileEntWall = (TileEntityWall) tileEnt;
					
					tileEnt = world.getTileEntity(tileEntWall.moduleBaseX, tileEntWall.moduleBaseY,
							tileEntWall.moduleBaseZ);
					if (tileEnt != null && tileEnt instanceof TileEntityModuleBase) {
						((TileEntityModuleBase) tileEnt).setWallToHide(x1, y1, z1);
						
						if (world.isRemote) {
							world.markBlockForUpdate(x1, y1, z1);
						}
					}
					else {
						Capo.log.info("No Parent Module");
					}
				}
				else {
					String error = tileEnt == null ? "Null Tile" : "Not Wall Tile";
					Capo.log.info("Invalid Wall: " + error);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	private int[][] getCoordsOfWalls(World world, int x, int y, int z, int side) {
		int[][] coords = new int[9][];
		int index = 0;
		
		if (side == 0 || side == 1) { // Y side (calc X & Z)
			for (int x1 = x - 1; x1 <= x + 1; x1++) {
				for (int z1 = z - 1; z1 <= z + 1; z1++) {
					coords[index] = new int[]{x1, y, z1};
					index += 1;
				}
			}
		}
		else if (side == 2 || side == 3) { // Z side (calc X & Y)
			for (int x1 = x - 1; x1 <= x + 1; x1++) {
				for (int y1 = y - 1; y1 <= y + 1; y1++) {
					coords[index] = new int[]{x1, y1, z};
					index += 1;
				}
			}
		}
		else if (side == 4 || side == 5) { // X side (calc Y & Z)
			for (int y1 = y - 1; y1 <= y + 1; y1++) {
				for (int z1 = z - 1; z1 <= z + 1; z1++) {
					coords[index] = new int[]{x, y1, z1};
					index += 1;
				}
			}
		}
		
		return coords;
	}
	
}
