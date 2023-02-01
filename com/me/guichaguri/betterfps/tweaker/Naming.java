/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.tweaker;

public enum Naming {
    M_StaticBlock("<clinit>", null, null, "()V"),
    M_Constructor("<init>", null, null, "()V"),
    C_Minecraft("net.minecraft.client.Minecraft", null),
    C_KeyBinding("net.minecraft.client.settings.KeyBinding", null),
    C_World("net.minecraft.world.World", null, null),
    C_Chunk("net.minecraft.world.chunk.Chunk", null, null),
    C_ChunkCoordIntPair("net.minecraft.world.ChunkCoordIntPair", null, null),
    C_EntityPlayer("net.minecraft.entity.player.EntityPlayer", null, null),
    C_MathHelper("net.minecraft.util.MathHelper", null, null),
    C_PrimedTNT("net.minecraft.entity.item.EntityTNTPrimed", null, null),
    C_ClientBrandRetriever("net.minecraft.client.ClientBrandRetriever", null),
    C_GuiOptions("net.minecraft.client.gui.GuiOptions", null),
    C_WorldClient("net.minecraft.client.multiplayer.WorldClient", null),
    C_WorldServer("net.minecraft.world.WorldServer", null, null),
    C_IntegratedServer("net.minecraft.server.integrated.IntegratedServer", null),
    C_DedicatedServer("net.minecraft.server.dedicated.DedicatedServer", null, null),
    C_TileEntityBeacon("net.minecraft.tileentity.TileEntityBeacon", null, null),
    C_BeamSegment("net.minecraft.tileentity.TileEntityBeacon$BeamSegment", null, null),
    C_TileEntityHopper("net.minecraft.tileentity.TileEntityHopper", null, null),
    C_BlockHopper("net.minecraft.block.BlockHopper", null, null),
    C_ModelBox("net.minecraft.client.model.ModelBox", null),
    C_EntityRenderer("net.minecraft.client.renderer.EntityRenderer", null),
    M_startGame("startGame", null, null, "()V"),
    M_onTick("onTick", null, null, "(I)V"),
    M_sin("sin", "a", null, "(F)F"),
    M_cos("cos", "b", null, "(F)F"),
    M_tick("tick", null, null, "()V"),
    M_onUpdate("onUpdate", null, null, "()V"),
    M_updateBlocks("updateBlocks", null, null, "()V"),
    M_getClientModName("getClientModName", null, null, "()Ljava/lang/String;"),
    M_freeMemory("freeMemory", null, null, "()V"),
    M_initGui("initGui", null, null, "()V"),
    M_startServer("startServer", null, null, "()Z"),
    M_setActivePlayerChunksAndCheckLight("setActivePlayerChunksAndCheckLight", null, null, "()V"),
    M_captureDroppedItems("captureDroppedItems", null, null, null),
    F_memoryReserve("memoryReserve", null, null, "[B"),
    F_SIN_TABLE("SIN_TABLE", "a", null, "[F");

    private final String deob;
    private final String deobRepl;
    private String ob;
    private String obRepl;
    private String bukkit;
    private String bukkitRepl;
    private boolean useOb = false;
    private boolean useBukkit = false;
    private String desc;

    private Naming(String deob, String ob) {
        this(deob, ob, null);
    }

    private Naming(String deob, String ob, String bukkit) {
        this.deob = deob;
        this.deobRepl = deob.replaceAll("\\.", "/");
        if (ob != null) {
            this.useOb = true;
            this.ob = ob;
            this.obRepl = ob.replaceAll("\\.", "/");
        }
        if (bukkit != null) {
            this.useBukkit = true;
            this.bukkit = bukkit;
            this.bukkitRepl = bukkit.replaceAll("\\.", "/");
        }
    }

    private Naming(String deob, String ob, String bukkit, String desc) {
        this(deob, ob, bukkit);
        this.desc = desc;
    }

    public boolean is(String name) {
        if (name.equals(this.deob)) {
            return true;
        }
        if (this.useOb && name.equals(this.ob)) {
            return true;
        }
        return this.useBukkit && name.equals(this.bukkit);
    }

    public boolean isASM(String name) {
        if (name.equals(this.deobRepl)) {
            return true;
        }
        if (this.useOb && name.equals(this.obRepl)) {
            return true;
        }
        return this.useBukkit && name.equals(this.bukkitRepl);
    }

    public boolean is(String name, String desc) {
        return this.desc.equals(desc) && this.is(name);
    }
}

