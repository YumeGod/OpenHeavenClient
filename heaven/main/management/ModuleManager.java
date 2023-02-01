/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.management;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventKey;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.world.EventTick;
import heaven.main.management.EventManager;
import heaven.main.management.FileManager;
import heaven.main.management.Manager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AimAssist;
import heaven.main.module.modules.combat.AimBow;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.module.modules.combat.AntiFireball;
import heaven.main.module.modules.combat.AutoArmor;
import heaven.main.module.modules.combat.AutoClicker;
import heaven.main.module.modules.combat.AutoHead;
import heaven.main.module.modules.combat.AutoPotion;
import heaven.main.module.modules.combat.AutoSoup;
import heaven.main.module.modules.combat.AutoSword;
import heaven.main.module.modules.combat.BetterSword;
import heaven.main.module.modules.combat.Criticals;
import heaven.main.module.modules.combat.CustomAura;
import heaven.main.module.modules.combat.FastBow;
import heaven.main.module.modules.combat.FastThrow;
import heaven.main.module.modules.combat.HitBox;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.combat.NewRegen;
import heaven.main.module.modules.combat.NoAttackDelay;
import heaven.main.module.modules.combat.Reach;
import heaven.main.module.modules.combat.Regen;
import heaven.main.module.modules.combat.SuperKnockBack;
import heaven.main.module.modules.combat.TNTBlock;
import heaven.main.module.modules.combat.TPAura;
import heaven.main.module.modules.combat.TargetStrafe;
import heaven.main.module.modules.combat.TerminatorBot;
import heaven.main.module.modules.combat.Trigger;
import heaven.main.module.modules.combat.Velocity;
import heaven.main.module.modules.globals.ASyncScreenShot;
import heaven.main.module.modules.globals.AnimatedView;
import heaven.main.module.modules.globals.Chat;
import heaven.main.module.modules.globals.ChatCopy;
import heaven.main.module.modules.globals.ChatTranslator;
import heaven.main.module.modules.globals.ChunkAnimator;
import heaven.main.module.modules.globals.ContainerAnimations;
import heaven.main.module.modules.globals.ItemPhysic;
import heaven.main.module.modules.globals.LowFire;
import heaven.main.module.modules.globals.MemoryFix;
import heaven.main.module.modules.globals.MouseDelayFix;
import heaven.main.module.modules.globals.NoBackground;
import heaven.main.module.modules.globals.VoidFlickFix;
import heaven.main.module.modules.misc.AntiExploit;
import heaven.main.module.modules.misc.AntiSpam;
import heaven.main.module.modules.misc.AutoHypixel;
import heaven.main.module.modules.misc.AutoL;
import heaven.main.module.modules.misc.Disabler;
import heaven.main.module.modules.misc.HackerDetect;
import heaven.main.module.modules.misc.HitSound;
import heaven.main.module.modules.misc.LagBackCheck;
import heaven.main.module.modules.misc.Lightningtracker;
import heaven.main.module.modules.misc.MCF;
import heaven.main.module.modules.misc.MultiActions;
import heaven.main.module.modules.misc.NoCommand;
import heaven.main.module.modules.misc.PacketMotior;
import heaven.main.module.modules.misc.PingSpoof;
import heaven.main.module.modules.misc.ServerCrasher;
import heaven.main.module.modules.misc.ServerSwitcher;
import heaven.main.module.modules.misc.Spammer;
import heaven.main.module.modules.misc.Teams;
import heaven.main.module.modules.movement.AirJump;
import heaven.main.module.modules.movement.AirLadder;
import heaven.main.module.modules.movement.AntiVoid;
import heaven.main.module.modules.movement.BlockWalk;
import heaven.main.module.modules.movement.FastFall;
import heaven.main.module.modules.movement.FastStairs;
import heaven.main.module.modules.movement.FastStop;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.IceSpeed;
import heaven.main.module.modules.movement.InvMove;
import heaven.main.module.modules.movement.Jesus;
import heaven.main.module.modules.movement.Longjump;
import heaven.main.module.modules.movement.NoJumpDelay;
import heaven.main.module.modules.movement.NoSlow;
import heaven.main.module.modules.movement.NoWeb;
import heaven.main.module.modules.movement.Parkour;
import heaven.main.module.modules.movement.PerfectHorseJump;
import heaven.main.module.modules.movement.SlimeJump;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.movement.Sprint;
import heaven.main.module.modules.movement.Step;
import heaven.main.module.modules.movement.Strafe;
import heaven.main.module.modules.movement.Unstuck;
import heaven.main.module.modules.movement.WallClimb;
import heaven.main.module.modules.player.AbortBreaking;
import heaven.main.module.modules.player.AntiAFK;
import heaven.main.module.modules.player.AntiAim;
import heaven.main.module.modules.player.AntiAim2;
import heaven.main.module.modules.player.AntiCactus;
import heaven.main.module.modules.player.AntiDebuff;
import heaven.main.module.modules.player.AntiDesync;
import heaven.main.module.modules.player.AntiObsidian;
import heaven.main.module.modules.player.AntiTabComplete;
import heaven.main.module.modules.player.ArrowDodge;
import heaven.main.module.modules.player.AutoDoor;
import heaven.main.module.modules.player.AutoEat;
import heaven.main.module.modules.player.AutoFish;
import heaven.main.module.modules.player.AutoTool;
import heaven.main.module.modules.player.Blink;
import heaven.main.module.modules.player.Bob;
import heaven.main.module.modules.player.Eagle;
import heaven.main.module.modules.player.FastDrop;
import heaven.main.module.modules.player.FastUse;
import heaven.main.module.modules.player.FlagRemover;
import heaven.main.module.modules.player.GhostHand;
import heaven.main.module.modules.player.GhostHit;
import heaven.main.module.modules.player.InvCleaner;
import heaven.main.module.modules.player.NoFall;
import heaven.main.module.modules.player.Phase;
import heaven.main.module.modules.player.PlayerFinder;
import heaven.main.module.modules.player.PotionSaver;
import heaven.main.module.modules.player.Respawn;
import heaven.main.module.modules.render.Animations;
import heaven.main.module.modules.render.Arrow;
import heaven.main.module.modules.render.Atmosphere;
import heaven.main.module.modules.render.BlockOverlay;
import heaven.main.module.modules.render.Breadcrumbs;
import heaven.main.module.modules.render.Cape;
import heaven.main.module.modules.render.Chams;
import heaven.main.module.modules.render.ChestESP;
import heaven.main.module.modules.render.ChinaHat;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.module.modules.render.Compass;
import heaven.main.module.modules.render.Crosshair;
import heaven.main.module.modules.render.DMGParticle;
import heaven.main.module.modules.render.ESP;
import heaven.main.module.modules.render.EmoJiMask;
import heaven.main.module.modules.render.EnchantEffect;
import heaven.main.module.modules.render.EntityHurtColor;
import heaven.main.module.modules.render.FPSBoost;
import heaven.main.module.modules.render.FPSHurtCam;
import heaven.main.module.modules.render.FreeCam;
import heaven.main.module.modules.render.FullBright;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.Health;
import heaven.main.module.modules.render.InventoryHUD;
import heaven.main.module.modules.render.ItemESP;
import heaven.main.module.modules.render.Keyrender;
import heaven.main.module.modules.render.ModuleIndicator;
import heaven.main.module.modules.render.MoreParticles;
import heaven.main.module.modules.render.MotionBlur;
import heaven.main.module.modules.render.NameProtect;
import heaven.main.module.modules.render.NameTags;
import heaven.main.module.modules.render.NoHurtCam;
import heaven.main.module.modules.render.NoPumpkinHead;
import heaven.main.module.modules.render.PacketGraph;
import heaven.main.module.modules.render.Path;
import heaven.main.module.modules.render.PlayerInfo;
import heaven.main.module.modules.render.PlayerList;
import heaven.main.module.modules.render.PlayerSize;
import heaven.main.module.modules.render.Projectiles;
import heaven.main.module.modules.render.Radar;
import heaven.main.module.modules.render.RainbowRect;
import heaven.main.module.modules.render.Rotate;
import heaven.main.module.modules.render.SessionInfo;
import heaven.main.module.modules.render.SetScoreboard;
import heaven.main.module.modules.render.Skeltal;
import heaven.main.module.modules.render.StaffCheck;
import heaven.main.module.modules.render.TNTTag;
import heaven.main.module.modules.render.TargetHUD;
import heaven.main.module.modules.render.Tracers;
import heaven.main.module.modules.render.Trails;
import heaven.main.module.modules.render.ViewClip;
import heaven.main.module.modules.render.WillFallIn;
import heaven.main.module.modules.render.Wings;
import heaven.main.module.modules.render.Xray;
import heaven.main.module.modules.script.Example;
import heaven.main.module.modules.world.AntiInvisibility;
import heaven.main.module.modules.world.AutoBreak;
import heaven.main.module.modules.world.BedFucker;
import heaven.main.module.modules.world.ChestAura;
import heaven.main.module.modules.world.ChestStealer;
import heaven.main.module.modules.world.FastBreak;
import heaven.main.module.modules.world.FastPlace;
import heaven.main.module.modules.world.GameSpeed;
import heaven.main.module.modules.world.GodMode;
import heaven.main.module.modules.world.NoRotate;
import heaven.main.module.modules.world.NoWeather;
import heaven.main.module.modules.world.PlayerPositions;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.module.modules.world.TP2Bed;
import heaven.main.module.modules.world.WindowChestHack;
import heaven.main.module.modules.world.WorldTime;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.lwjgl.input.Keyboard;

public class ModuleManager
implements Manager {
    public final List<Module> modules = new ArrayList<Module>();
    private boolean enabledNeededMod = true;
    private final ArrayList<Module> modulesIsPretending = new ArrayList();
    private final ArrayList<Module> modulesUnAllowed = new ArrayList();
    private boolean isModulePretend;

    @Override
    public void init() {
        this.regMods(new AntiBot(), new AimAssist(), new AutoArmor(), new AutoHead(), new AutoSoup(), new AutoClicker(), new AutoPotion(), new AutoSword(), new AimBow(), new AntiFireball(), new BetterSword(), new FastBow(), new FastThrow(), new CustomAura(), new Criticals(), new HitBox(), new NoAttackDelay(), new KillAura(), new Reach(), new Regen(), new NewRegen(), new TargetStrafe(), new TNTBlock(), new SuperKnockBack(), new Trigger(), new TerminatorBot(), new TPAura(), new Velocity());
        this.regMods(new AntiVoid(), new Fly(), new InvMove(), new Jesus(), new NoSlow(), new NoWeb(), new FastFall(), new BlockWalk(), new FastStairs(), new PerfectHorseJump(), new FastStop(), new WallClimb(), new AirJump(), new AirLadder(), new Unstuck(), new Speed(), new IceSpeed(), new Longjump(), new SlimeJump(), new NoJumpDelay(), new Sprint(), new Step(), new Parkour(), new Strafe());
        this.regMods(new AutoTool(), new AutoDoor(), new AutoFish(), new AntiObsidian(), new AbortBreaking(), new AntiTabComplete(), new AntiCactus(), new AntiDebuff(), new AntiDesync(), new FlagRemover(), new AntiAFK(), new PotionSaver(), new GhostHand(), new InvCleaner(), new NoFall(), new Eagle(), new ArrowDodge(), new GhostHit(), new PlayerFinder(), new Phase(), new Blink(), new FastUse(), new FastDrop(), new Respawn(), new Bob(), new AntiAim(), new AutoEat(), new AntiAim2());
        this.regMods(new Animations(), new Path(), new Chams(), new ChestESP(), new ChinaHat(), new ClickGui(), new ESP(), new Breadcrumbs(), new BlockOverlay(), new Arrow(), new PacketGraph(), new Compass(), new FreeCam(), new FullBright(), new RainbowRect(), new Crosshair(), new EmoJiMask(), new FPSHurtCam(), new FPSBoost(), new EnchantEffect(), new DMGParticle(), new HUD(), new InventoryHUD(), new SessionInfo(), new Health(), new Keyrender(), new Cape(), new ModuleIndicator(), new ItemESP(), new NameProtect(), new NameTags(), new NoHurtCam(), new Projectiles(), new Skeltal(), new Radar(), new Rotate(), new PlayerSize(), new EntityHurtColor(), new NoPumpkinHead(), new MoreParticles(), new Atmosphere(), new MotionBlur(), new Wings(), new SetScoreboard(), new PlayerList(), new TargetHUD(), new PlayerInfo(), new StaffCheck(), new Tracers(), new ViewClip(), new Xray(), new WillFallIn(), new TNTTag(), new Trails());
        this.regMods(new NoRotate(), new BedFucker(), new AutoBreak(), new ChestStealer(), new WindowChestHack(), new ChestAura(), new FastBreak(), new FastPlace(), new Scaffold(), new GodMode(), new GameSpeed(), new WorldTime(), new MemoryFix(), new PlayerPositions(), new AntiInvisibility(), new NoWeather(), new TP2Bed());
        this.regMods(new MCF(), new NoCommand(), new PingSpoof(), new Lightningtracker(), new HackerDetect(), new MultiActions(), new AntiSpam(), new ServerSwitcher(), new ServerCrasher(), new AutoHypixel(), new Disabler(), new LagBackCheck(), new Spammer(), new Teams(), new HitSound(), new AntiExploit(), new AutoL(), new PacketMotior());
        this.regMods(new ChunkAnimator(), new Chat(), new ItemPhysic(), new LowFire(), new NoBackground(), new ContainerAnimations(), new AnimatedView(), new MouseDelayFix(), new ASyncScreenShot(), new VoidFlickFix(), new ChatCopy(), new ChatTranslator());
        this.regMods(new Example());
        this.sortModules();
        this.readSettings();
        for (Module m : this.modules) {
            m.makeCommand();
            EventManager.runAllTime(m);
        }
        EventManager.register(this);
    }

    public void regMods(Module ... mods) {
        Collections.addAll(this.modules, mods);
    }

    public void sortModules() {
        this.modules.sort(Comparator.comparing(Module::getName));
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : this.modules) {
            if (m.getClass() != cls) continue;
            return m;
        }
        return null;
    }

    public Module getModuleByName(String name) {
        for (Module m : this.modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    public Module getAlias(String name) {
        for (Module f : this.modules) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
            for (String s : f.getAlias()) {
                if (!s.equalsIgnoreCase(name)) continue;
                return f;
            }
        }
        return null;
    }

    public List<Module> getModulesInType(ModuleType category) {
        ArrayList<Module> modList = new ArrayList<Module>();
        for (Module mod : this.modules) {
            if (mod.getType() != category) continue;
            modList.add(mod);
        }
        modList.sort(Comparator.comparing(Module::getName));
        return modList;
    }

    @EventHandler
    private void onKeyPress(EventKey e) {
        for (Module m : this.modules) {
            if (m.getKey() != e.getKey()) continue;
            m.setEnabled(!m.isEnabled());
        }
    }

    @EventHandler
    public void onTick(EventTick e) {
        if (Client.instance.isPretend() && !this.isModulePretend) {
            for (Module module : this.modules) {
                if (!module.isEnabled()) continue;
                this.modulesIsPretending.add(module);
                module.setEnabledWithoutNotification(false);
            }
            this.modulesUnAllowed.addAll(this.modules);
            this.modules.clear();
            this.isModulePretend = true;
        } else if (!Client.instance.isPretend() && this.isModulePretend) {
            this.modules.addAll(this.modulesUnAllowed);
            for (Module module : this.modulesIsPretending) {
                module.setEnabledWithoutNotification(true);
            }
            this.modulesIsPretending.clear();
            this.isModulePretend = false;
        }
    }

    @EventHandler
    private void on2DRender(EventRender2D e) {
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module m : this.modules) {
                if (!m.enabledOnStartup) continue;
                m.setEnabledWithoutNotification(true);
            }
        }
    }

    private void readSettings() {
        Module m;
        String name;
        for (String v : FileManager.read("Binds.txt")) {
            name = v.split(":")[0];
            String bind = v.split(":")[1];
            m = this.getModuleByName(name);
            if (m == null) continue;
            m.setKey(Keyboard.getKeyIndex((String)bind.toUpperCase()));
        }
        for (String v : FileManager.read("Enabled.txt")) {
            Module m2 = this.getModuleByName(v);
            if (m2 == null) continue;
            m2.enabledOnStartup = true;
        }
        for (String v : FileManager.read("Values.txt")) {
            name = v.split(":")[0];
            String values = v.split(":")[1];
            m = this.getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[2]);
            }
        }
        List<String> names = FileManager.read("CustomName.txt");
        for (String v : names) {
            String name2 = v.split(":")[0];
            String cusname = v.split(":")[1];
            Module m4 = this.getModuleByName(name2);
            if (m4 == null) continue;
            m4.setCustomName(cusname);
        }
        for (String h : FileManager.read("Hidden.txt")) {
            Module m3 = this.getModuleByName(h);
            if (m3 == null) continue;
            m3.setRemoved(true);
        }
    }
}

