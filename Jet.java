package ru.jetdev;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("jet")
public class Jet {
    private static final Logger LOGGER = LogManager.getLogger();

    private Jet() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(KillAuraModule.class);
        MinecraftForge.EVENT_BUS.register(CustomKeyHandler.class);
        MinecraftForge.EVENT_BUS.register(AimbotModule.class);
    }

    private void setup(Event event) {
    }
}
   