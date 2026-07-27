package com.github.tartaricacid.touhoulittlemaid.inventory.container.task;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class DefaultMaidTaskConfigContainer extends TaskConfigContainer {
    public static final MenuType<DefaultMaidTaskConfigContainer> TYPE = new ExtendedScreenHandlerType<>(
            (id, inv, data) -> new DefaultMaidTaskConfigContainer(id, inv, data.readInt())
    );

    public DefaultMaidTaskConfigContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }
}
