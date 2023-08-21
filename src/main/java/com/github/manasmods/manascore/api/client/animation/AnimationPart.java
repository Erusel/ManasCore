package com.github.manasmods.manascore.api.client.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An animation constitutes a separate part of an animation with its own renderer and shared values
 */
@Getter
public class AnimationPart {

    /**
     * The internal name of the animation part
     */
    private String name;

    private List<RendererConfig> rendererChain;

    private String abortAnimation;

    @Getter
    public static class RendererConfig {
        /**
         * The name of the renderer to use
         */
        private String name;

        /**
         * Parameters that are automatically passed to the renderer, these will be taken either from the AnimationPart Shared Value Storage or the global Animation storage
         * Params have a value, a position and a scope. (Position is the argument position after all required args)
         * Valid scopes are:
         *  - provided => The value is a constant and is read from the config file, may be overridden by the mod in code
         *  - derived => The value is provided by the mod or by minecraft and copied as the name, value has to be ${VARIABLE_NAME}
         *  - dynamic => The value is provided by the mod or by minecraft itself
         */
        private Map<String, ConfigValue> params;

        @Data
        @AllArgsConstructor
        public static class ConfigValue<T> {

            private T value;
            private int position;
            private String scope;

        }
    }

    public static AnimationPart fromJson(JsonObject object) {
        AnimationPart part = new AnimationPart();

        part.name = GsonHelper.getAsString(object, "name");

        if(object.has("abortAnimation")) {
            part.abortAnimation = GsonHelper.getAsString(object, "abortAnimation");
        }

        JsonArray chain = GsonHelper.getAsJsonArray(object, "rendererChain");

        part.rendererChain = new ArrayList<>();

        for(JsonElement element : chain.asList()) {
            JsonObject obj = GsonHelper.convertToJsonObject(element, "rendererChain element");

            RendererConfig config = new RendererConfig();
            config.name = GsonHelper.getAsString(obj, "name");
            config.params = new HashMap<>();

            Map<String, JsonElement> paramsMap = GsonHelper.getAsJsonObject(obj, "params").asMap();

            //Position of the argument
            int pos = 0;

            for(String paramName : paramsMap.keySet()) {
                JsonObject param = GsonHelper.convertToJsonObject(paramsMap.get(paramName), String.format("Renderer %s params element %s", config.name, paramName));

                String scope = GsonHelper.getAsString(param, "scope");
                JsonElement value = param.get("value");

                if(value.isJsonPrimitive()) {
                    if(value.getAsJsonPrimitive().isString()) {
                        config.params.put(paramName, new RendererConfig.ConfigValue(value.getAsString(), pos, scope));
                    }

                    if(value.getAsJsonPrimitive().isBoolean()) {
                        config.params.put(paramName, new RendererConfig.ConfigValue(value.getAsBoolean(), pos, scope));
                    }

                    if(value.getAsJsonPrimitive().isNumber()) {
                        config.params.put(paramName, new RendererConfig.ConfigValue(value.getAsNumber(), pos, scope));
                    }
                }

                pos++;
            }

            part.rendererChain.add(config);
        }

        return part;
    }

}
