package com.github.legoaggelos.fossils_delight;

import com.github.legoaggelos.fossils_delight.registry.ItemRegistry;

public final class FossilsDelight {
    public static final String MOD_ID = "fossils_delight";

    public static void init() {
        // Write common init code here.
        //Commit test
        ItemRegistry.ITEMS.register();
    }
}
