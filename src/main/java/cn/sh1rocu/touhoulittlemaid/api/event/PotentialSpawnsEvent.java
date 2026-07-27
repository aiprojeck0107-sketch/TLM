package cn.sh1rocu.touhoulittlemaid.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PotentialSpawnsEvent extends CancellableEvent {
    private final LevelAccessor level;
    private final MobCategory mobcategory;
    private final BlockPos pos;
    @Nullable
    private List<MobSpawnSettings.SpawnerData> list;
    private List<MobSpawnSettings.SpawnerData> view;

    public PotentialSpawnsEvent(LevelAccessor level, MobCategory category, BlockPos pos, WeightedRandomList<MobSpawnSettings.SpawnerData> oldList) {
        this.level = level;
        this.pos = pos;
        this.mobcategory = category;
        this.list = null;
        this.view = oldList.unwrap();
    }

    public static final Event<Callback> CALLBACK = EventFactory.createArrayBacked(Callback.class, callbacks -> event -> {
        for (Callback callback : callbacks) {
            callback.post(event);
        }
    });

    public LevelAccessor getLevel() {
        return level;
    }

    public MobCategory getMobCategory() {
        return mobcategory;
    }

    public BlockPos getPos() {
        return pos;
    }

    public List<MobSpawnSettings.SpawnerData> getSpawnerDataList() {
        return view;
    }

    private void makeList() {
        if (list == null) {
            list = new ArrayList<>(view);
            view = Collections.unmodifiableList(list);
        }
    }

    public void addSpawnerData(MobSpawnSettings.SpawnerData data) {
        makeList();
        list.add(data);
    }

    public boolean removeSpawnerData(MobSpawnSettings.SpawnerData data) {
        makeList();
        return list.remove(data);
    }

    public interface Callback {
        void post(PotentialSpawnsEvent event);
    }
}
