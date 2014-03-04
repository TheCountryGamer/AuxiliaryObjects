package com.countrygamer.auxiliaryobjects.lib;

import com.countrygamer.auxiliaryobjects.AuxiliaryObjects;

public enum EnumPartition {
	NONE(0, "None"), DURABILTY(1, "Durability"), POTION(2, "Potion"), BUCKET(3, "Bucket"), LAVABUCKET(
			4, "Lava Bucket");
	
	private final int		id;
	private final String	name;
	
	private EnumPartition(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static EnumPartition getEnumForID(int id) {
		if (id < 0) return getEnumForID(EnumPartition.values().length - 1);
		EnumPartition partition = EnumPartition.NONE;
		for (EnumPartition enumP : EnumPartition.values()) {
			// AuxiliaryObjects.log.info("Enum; " + id + ":" + enumP.getID());
			if (enumP.getID() == id) {
				partition = enumP;
				break;
			}
		}
		return partition;
	}
	
}
