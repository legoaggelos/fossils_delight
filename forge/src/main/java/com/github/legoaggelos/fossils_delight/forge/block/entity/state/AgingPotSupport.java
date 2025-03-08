package com.github.legoaggelos.fossils_delight.forge.block.entity.state;

import net.minecraft.util.StringRepresentable;

public enum AgingPotSupport implements StringRepresentable
{
	NONE("none"),
	TRAY("tray"),
	HANDLE("handle");

	private final String supportName;

	AgingPotSupport(String name) {
		this.supportName = name;
	}

	@Override
	public String toString() {
		return this.getSerializedName();
	}

	@Override
	public String getSerializedName() {
		return this.supportName;
	}
}
