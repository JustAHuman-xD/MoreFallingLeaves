package me.justahuman.morefallingleaves;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.logging.LogUtils;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MoreFallingLeaves implements ClientModInitializer, ModMenuApi {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Logger LOGGER = LogUtils.getLogger();
    public static double guaranteedLeafDistance = 16;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Starting MoreFallingLeaves");
        File configFile = getConfigFile();
        if (configFile.exists()) {
            try(FileReader reader = new FileReader(configFile)) {
                JsonObject config = GSON.fromJson(reader, JsonObject.class);
                if (config != null && config.get("guaranteedLeafDistance") instanceof JsonPrimitive primitive && primitive.isNumber()) {
                    guaranteedLeafDistance = primitive.getAsDouble();
                    double clamped = Math.clamp(guaranteedLeafDistance, 0, 32);
                    if (clamped != guaranteedLeafDistance) {
                        LOGGER.warn("Config value out of bounds (0, 32), clamping to {}", clamped);
                        guaranteedLeafDistance = clamped;
                    }
                } else {
                    LOGGER.warn("No value found, using default");
                }
            } catch (Exception e) {
                LOGGER.error("Failed to read config", e);
            }
        } else {
            LOGGER.warn("Config file not found, using default");
        }
        LOGGER.info("MoreFallingLeaves started with guaranteedLeafDistance: {}", guaranteedLeafDistance);
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("More Falling Leaves"));
            ConfigCategory category = builder.getOrCreateCategory(Text.literal("Options"));
            category.addEntry(builder.entryBuilder().startIntSlider(Text.literal("Guaranteed Leaf Distance"), (int) (guaranteedLeafDistance * 10), 0, 320)
                    .setTooltip(Text.literal("Refer to the README for more information"))
                    .setDefaultValue(160)
                    .setTextGetter(MoreFallingLeaves::formatOption)
                    .setSaveConsumer(value -> {
                        guaranteedLeafDistance = (double) value / 10d;
                        try(FileWriter configFile = new FileWriter(getConfigFile())) {
                            JsonObject config = new JsonObject();
                            config.addProperty("guaranteedLeafDistance", guaranteedLeafDistance);
                            GSON.toJson(config, configFile);
                        } catch (Exception e) {
                            LOGGER.error("Failed to save config", e);
                        }
                    })
                    .build());
            return builder.build();
        };
    }

    private static Text formatOption(int value) {
        return value == 0 ? ScreenTexts.OFF : Text.literal(Double.toString((double) value / 10d));
    }

    private static File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("morefallingleaves.json").toFile();
    }
}
