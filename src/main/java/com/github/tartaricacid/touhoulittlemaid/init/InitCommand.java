package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.command.arguments.HandleTypeArgument;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.ResourceLocation;

public class InitCommand {
    public static void init() {
        ArgumentTypeRegistry.registerArgumentType(new ResourceLocation(TouhouLittleMaid.MOD_ID, "handle_types"), HandleTypeArgument.class, SingletonArgumentInfo.contextFree(HandleTypeArgument::type));
    }
}
