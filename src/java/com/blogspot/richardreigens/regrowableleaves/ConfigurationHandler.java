package com.blogspot.richardreigens.regrowableleaves;

import com.blogspot.richardreigens.regrowableleaves.reference.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by LiLRichy on 12/26/2015.
 */
@Config(modid = Reference.MOD_ID)
public class ConfigurationHandler
{
	@Name(value = "General Settings")
    @Config.Comment(value = "General regrow settings")
    public static final GeneralSettings generalSettings = new GeneralSettings();

    public static class GeneralSettings
    {
    	@Name("Leaf regrow rate")
        @Config.Comment("Rate that leaves will regrow. Lower number is faster.")
    	@RangeInt(min = 0, max = 10)
        public int leafRegrowthRate = 3;  // 0-10

    	@Name("Light required to grow")
        @Config.Comment("Light level required for leaves to start regrowing. 0 = no light required.")
    	@RangeInt(min = 0, max = 13)
        public int lightRequiredToGrow = 4; // 0-13
    	
    	@Name("Grow outward")
        @Config.Comment("Must leaves grow next to other leaves or logs (aka \"can sustain leaves\") to regrow")
        public boolean growOutward = false;
        
    	@Name("Debug mode")
        @Config.Comment("Enable this to show blocks and output debug text into console.")
        public boolean debugMode = false;
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
    
}
