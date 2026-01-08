package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.recipe.CokeOvenRecipes;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DnMmod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CokeOvenRecipes>> COKE_OVEN_RECIPE =
            SERIALIZERS.register("coke_oven", () -> CokeOvenRecipes.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
