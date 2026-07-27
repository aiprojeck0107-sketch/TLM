package cn.sh1rocu.touhoulittlemaid.api.extension;

public interface IEntity {
    boolean isAddedToWorld();

    void onAddedToWorld();

    void onRemovedFromWorld();
}
