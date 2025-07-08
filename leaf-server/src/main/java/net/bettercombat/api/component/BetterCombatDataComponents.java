package net.bettercombat.api.component;

import net.bettercombat.BetterCombatMod;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class BetterCombatDataComponents {
    public static final DataComponentType<ResourceLocation> WEAPON_PRESET_ID = register(ResourceLocation.fromNamespaceAndPath(BetterCombatMod.ID, "preset_id"),
            builder -> builder.persistent(ResourceLocation.CODEC)
    );

    private static <T> DataComponentType<T> register(ResourceLocation id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builderOperator.apply(DataComponentType.builder()).build());
    }
}
