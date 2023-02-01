/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.drakApi.PlayerUpdateEvent;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;

public class Atmosphere
extends Module {
    private final Option<Boolean> weather_control = new Option<Boolean>("WeatherControl", true);
    public static final Mode<String> weather_mode = new Mode("WeatherState", new String[]{"Clean", "Rain", "Thunder", "Snowfall", "Snowstorm"}, "Clean");
    public static final Numbers<Double> time = new Numbers<Double>("Hour", 12.0, 1.0, 24.0, 1.0);

    public Atmosphere() {
        super("Atmosphere", ModuleType.Render);
        this.addValues(weather_mode, this.weather_control, time);
    }

    public static long getTime() {
        return ((Double)time.get()).longValue() * 1000L;
    }

    @EventHandler
    public void onUpdate(PlayerUpdateEvent event) {
        if (((Boolean)this.weather_control.get()).booleanValue()) {
            switch ((String)weather_mode.get()) {
                case "Snowfall": 
                case "Rain": {
                    Minecraft.theWorld.setRainStrength(1.0f);
                    Minecraft.theWorld.setThunderStrength(0.0f);
                    break;
                }
                case "Snowstorm": 
                case "Thunder": {
                    Minecraft.theWorld.setRainStrength(1.0f);
                    Minecraft.theWorld.setThunderStrength(1.0f);
                    break;
                }
                default: {
                    Minecraft.theWorld.setRainStrength(0.0f);
                    Minecraft.theWorld.setThunderStrength(0.0f);
                }
            }
        }
    }
}

