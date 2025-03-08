package com.github.legoaggelos.fossils_delight;

import com.github.legoaggelos.fossils_delight.registry.ItemRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FossilsDelight {
    public static final String MOD_ID = "fossils_delight";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        // Write common init code here.
        //Commit test
        ItemRegistry.ITEMS.register();
    }
}
