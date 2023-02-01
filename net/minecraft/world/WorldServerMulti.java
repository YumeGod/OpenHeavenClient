/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerMulti
extends WorldServer {
    private final WorldServer delegate;

    public WorldServerMulti(MinecraftServer server, ISaveHandler saveHandlerIn, int dimensionId, WorldServer delegate, Profiler profilerIn) {
        super(server, saveHandlerIn, new DerivedWorldInfo(delegate.getWorldInfo()), dimensionId, profilerIn);
        this.delegate = delegate;
        delegate.getWorldBorder().addListener(new IBorderListener(){

            @Override
            public void onSizeChanged(WorldBorder border, double newSize) {
                WorldServerMulti.this.getWorldBorder().setTransition(newSize);
            }

            @Override
            public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time) {
                WorldServerMulti.this.getWorldBorder().setTransition(oldSize, newSize, time);
            }

            @Override
            public void onCenterChanged(WorldBorder border, double x, double z) {
                WorldServerMulti.this.getWorldBorder().setCenter(x, z);
            }

            @Override
            public void onWarningTimeChanged(WorldBorder border, int newTime) {
                WorldServerMulti.this.getWorldBorder().setWarningTime(newTime);
            }

            @Override
            public void onWarningDistanceChanged(WorldBorder border, int newDistance) {
                WorldServerMulti.this.getWorldBorder().setWarningDistance(newDistance);
            }

            @Override
            public void onDamageAmountChanged(WorldBorder border, double newAmount) {
                WorldServerMulti.this.getWorldBorder().setDamageAmount(newAmount);
            }

            @Override
            public void onDamageBufferChanged(WorldBorder border, double newSize) {
                WorldServerMulti.this.getWorldBorder().setDamageBuffer(newSize);
            }
        });
    }

    @Override
    protected void saveLevel() {
    }

    @Override
    public World init() {
        this.mapStorage = this.delegate.getMapStorage();
        this.worldScoreboard = this.delegate.getScoreboard();
        String s = VillageCollection.fileNameForProvider(this.provider);
        VillageCollection villagecollection = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, s);
        if (villagecollection == null) {
            this.villageCollectionObj = new VillageCollection(this);
            this.mapStorage.setData(s, this.villageCollectionObj);
        } else {
            this.villageCollectionObj = villagecollection;
            this.villageCollectionObj.setWorldsForAll(this);
        }
        return this;
    }
}

