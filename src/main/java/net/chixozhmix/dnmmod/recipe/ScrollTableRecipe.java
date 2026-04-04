package net.chixozhmix.dnmmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ScrollTableRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final CompoundTag outputNbt;

    public ScrollTableRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id, CompoundTag outputNbt) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.outputNbt = outputNbt;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if(level.isClientSide)
            return false;

        // Проверяем все 6 входных слотов (индексы 0-5)
        for (int i = 0; i < 6; i++) {
            // Если в рецепте есть ингредиент для этого слота и он не совпадает
            if (i < inputItems.size() && !inputItems.get(i).test(simpleContainer.getItem(i))) {
                return false;
            }
            // Если в рецепте нет ингредиента для этого слота, но предмет есть
            else if (i >= inputItems.size() && !simpleContainer.getItem(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        ItemStack result = output.copy();
        if (outputNbt != null && !outputNbt.isEmpty()) {
            result.setTag(outputNbt.copy());
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        ItemStack result = output.copy();
        if (outputNbt != null && !outputNbt.isEmpty()) {
            result.setTag(outputNbt.copy());
        }
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ScrollTableRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "scroll_table";
    }

    public static class Serializer implements RecipeSerializer<ScrollTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DnMmod.MOD_ID, "scroll_table");

        @Override
        public ScrollTableRecipe fromJson(ResourceLocation recipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            // Читаем NBT если есть
            CompoundTag outputNbt = null;
            if (pSerializedRecipe.has("nbt")) {
                JsonObject nbtObject = GsonHelper.getAsJsonObject(pSerializedRecipe, "nbt");
                // Преобразуем JsonObject в CompoundTag
                outputNbt = jsonToNbt(nbtObject);
            }

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(6, Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size() && i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new ScrollTableRecipe(inputs, output, recipeId, outputNbt);
        }

        @Override
        public @Nullable ScrollTableRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf pBuffer) {
            int inputCount = pBuffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(inputCount, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            CompoundTag outputNbt = pBuffer.readNbt(); // Читаем NBT из сети

            return new ScrollTableRecipe(inputs, output, recipeId, outputNbt);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ScrollTableRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);

            // Отправляем NBT
            ItemStack result = pRecipe.getResultItem(null);
            pBuffer.writeNbt(result.getTag());
        }

        private CompoundTag jsonToNbt(JsonObject json) {
            CompoundTag tag = new CompoundTag();

            json.entrySet().forEach(entry -> {
                String key = entry.getKey();
                var element = entry.getValue();

                if (element.isJsonPrimitive()) {
                    var primitive = element.getAsJsonPrimitive();
                    if (primitive.isString()) {
                        tag.putString(key, primitive.getAsString());
                    } else if (primitive.isNumber()) {
                        // Сохраняем как int (можно расширить для double, float)
                        tag.putInt(key, primitive.getAsInt());
                    } else if (primitive.isBoolean()) {
                        tag.putBoolean(key, primitive.getAsBoolean());
                    }
                }
                else if (element.isJsonObject()) {
                    // Рекурсивно обрабатываем вложенные объекты
                    tag.put(key, jsonToNbt(element.getAsJsonObject()));
                }
                else if (element.isJsonArray()) {
                    // Обрабатываем JSON массивы
                    JsonArray array = element.getAsJsonArray();
                    // Для простоты предположим, что массив содержит объекты
                    // В вашем случае массив data содержит объекты со спеллами
                    net.minecraft.nbt.ListTag listTag = new net.minecraft.nbt.ListTag();

                    for (int i = 0; i < array.size(); i++) {
                        if (array.get(i).isJsonObject()) {
                            listTag.add(jsonToNbt(array.get(i).getAsJsonObject()));
                        } else if (array.get(i).isJsonPrimitive()) {
                            var primitive = array.get(i).getAsJsonPrimitive();
                            if (primitive.isString()) {
                                net.minecraft.nbt.StringTag stringTag = net.minecraft.nbt.StringTag.valueOf(primitive.getAsString());
                                listTag.add(stringTag);
                            } else if (primitive.isNumber()) {
                                net.minecraft.nbt.IntTag intTag = net.minecraft.nbt.IntTag.valueOf(primitive.getAsInt());
                                listTag.add(intTag);
                            }
                        }
                    }
                    tag.put(key, listTag);
                }
            });

            return tag;
        }
    }
}
