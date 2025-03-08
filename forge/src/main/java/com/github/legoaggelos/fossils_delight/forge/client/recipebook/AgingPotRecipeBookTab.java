package com.github.legoaggelos.fossils_delight.forge.client.recipebook;

public enum AgingPotRecipeBookTab
{
	MEALS("meals"),
	DRINKS("drinks"),
	MISC("misc");

	public final String name;

	AgingPotRecipeBookTab(String name) {
		this.name = name;
	}

	public static AgingPotRecipeBookTab findByName(String name) {
		for (AgingPotRecipeBookTab value : values()) {
			if (value.name.equals(name)) {
				return value;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
