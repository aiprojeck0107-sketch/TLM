package cn.sh1rocu.touhoulittlemaid.util.forge;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Objects;

public class CraftingHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker CRAFTHELPER = MarkerManager.getMarker("CRAFTHELPER");
    private static Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static ItemStack getItemStack(JsonObject json, boolean readNBT) {
        return getItemStack(json, readNBT, false);
    }

    public static Item getItem(String itemName, boolean disallowsAirInRecipe) {
        ResourceLocation itemKey = new ResourceLocation(itemName);
        if (!BuiltInRegistries.ITEM.containsKey(itemKey)) {
            throw new JsonSyntaxException("Unknown item '" + itemName + "'");
        } else {
            Item item = (Item) BuiltInRegistries.ITEM.get(itemKey);
            if (disallowsAirInRecipe && item == Items.AIR) {
                throw new JsonSyntaxException("Invalid item: " + itemName);
            } else {
                return (Item) Objects.requireNonNull(item);
            }
        }
    }

    public static CompoundTag getNBT(JsonElement element) {
        try {
            return element.isJsonObject() ? TagParser.parseTag(GSON.toJson(element)) : TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
        } catch (CommandSyntaxException e) {
            throw new JsonSyntaxException("Invalid NBT Entry: " + String.valueOf(e));
        }
    }

    public static ItemStack getItemStack(JsonObject json, boolean readNBT, boolean disallowsAirInRecipe) {
        String itemName = GsonHelper.getAsString(json, "item");
        Item item = getItem(itemName, disallowsAirInRecipe);
        if (readNBT && json.has("nbt")) {
            CompoundTag nbt = getNBT(json.get("nbt"));
            CompoundTag tmp = new CompoundTag();
            if (nbt.contains("ForgeCaps")) {
                tmp.put("ForgeCaps", nbt.get("ForgeCaps"));
                nbt.remove("ForgeCaps");
            }

            tmp.put("tag", nbt);
            tmp.putString("id", itemName);
            tmp.putInt("Count", GsonHelper.getAsInt(json, "count", 1));
            return ItemStack.of(tmp);
        } else {
            return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
        }
    }
}
