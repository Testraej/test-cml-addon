package com.physicsparticles.bbs.forms;

import mchorse.bbs_mod.forms.ITickable;
import mchorse.bbs_mod.forms.entities.IEntity;
import mchorse.bbs_mod.forms.renderers.FormRenderer;
import mchorse.bbs_mod.forms.renderers.FormRenderingContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.lang.reflect.Method;

/**
 * Client-side renderer for PhysicsParticleForm.
 *
 * Physics Mod Pro is called via reflection so this addon compiles without
 * needing the Physics Mod jar at build time. At runtime Physics Mod must
 * be present in the mods folder (which it already is for the user).
 */
@Environment(EnvType.CLIENT)
public class PhysicsParticleFormRenderer extends FormRenderer<PhysicsParticleForm> implements ITickable
{
    // -----------------------------------------------------------------------
    // Reflection cache for Physics Mod's ParticleSpawner
    // -----------------------------------------------------------------------

    /** Cached reference to ParticleSpawner.spawnSprintingPhysicsParticle */
    private static Method SPAWN_METHOD = null;
    private static boolean SPAWN_LOOKUP_DONE = false;

    /**
     * Lazily looks up the Physics Mod spawn method via reflection.
     * Returns null (and logs a warning once) if Physics Mod is not installed.
     */
    private static Method getSpawnMethod()
    {
        if (SPAWN_LOOKUP_DONE) return SPAWN_METHOD;
        SPAWN_LOOKUP_DONE = true;

        try
        {
            Class<?> spawner = Class.forName("net.diebuddies.minecraft.ParticleSpawner");
            SPAWN_METHOD = spawner.getMethod(
                "spawnSprintingPhysicsParticle",
                BlockState.class,   // block state (texture source)
                BlockPos.class,     // world position for lighting/context
                World.class,        // the world
                double.class,       // x
                double.class,       // y
                double.class        // z
            );
            System.out.println("[PhysicsParticlesBBSAddon] Successfully linked to Physics Mod ParticleSpawner.");
        }
        catch (Exception e)
        {
            System.err.println("[PhysicsParticlesBBSAddon] WARNING: Could not find Physics Mod Pro. " +
                "Make sure physics-mod-pro is in your mods folder! Error: " + e.getMessage());
            SPAWN_METHOD = null;
        }

        return SPAWN_METHOD;
    }

    // -----------------------------------------------------------------------
    // State
    // -----------------------------------------------------------------------

    /** World-space position extracted from the model matrix each frame */
    private double worldX, worldY, worldZ;

    /** Tick counter for frequency gating */
    private int tickCounter = 0;

    /** Reusable vector to avoid allocation per frame */
    private final Vector3f tempTranslation = new Vector3f();

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public PhysicsParticleFormRenderer(PhysicsParticleForm form)
    {
        super(form);
    }

    // -----------------------------------------------------------------------
    // ITickable — called every game tick by BBS
    // -----------------------------------------------------------------------

    @Override
    public void tick(IEntity entity)
    {
        if (!(this.form instanceof PhysicsParticleForm form)) return;
        if ((Boolean) form.paused.get()) return;

        // Frequency gate
        int freq = (Integer) form.frequency.get();
        if (freq <= 0) freq = 1;
        this.tickCounter++;
        if (this.tickCounter % freq != 0) return;

        // Get the Physics Mod spawn method (cached after first call)
        Method spawnMethod = getSpawnMethod();
        if (spawnMethod == null) return;   // Physics Mod not installed — skip silently

        try
        {
            World world = entity.getWorld();
            if (world == null) return;

            // Resolve block from registry ID
            String id = (String) form.blockId.get();
            if (id == null || id.isEmpty()) return;

            Identifier identifier = new Identifier(id);
            if (!Registries.BLOCK.containsId(identifier)) return;

            Block block = Registries.BLOCK.get(identifier);
            BlockState state = block.getDefaultState();

            // Read settings
            float spread = (Float) form.spread.get();
            float ox     = (Float) form.offsetX.get();
            float oy     = (Float) form.offsetY.get();
            float oz     = (Float) form.offsetZ.get();
            int   count  = (Integer) form.count.get();
            if (count <= 0) count = 1;

            // Emission centre
            double cx = this.worldX + ox;
            double cy = this.worldY + oy;
            double cz = this.worldZ + oz;

            BlockPos blockPos = BlockPos.ofFloored(cx, cy, cz);

            for (int i = 0; i < count; i++)
            {
                double sx = cx + (world.random.nextDouble() - 0.5) * spread;
                double sy = cy + (world.random.nextDouble() - 0.5) * spread;
                double sz = cz + (world.random.nextDouble() - 0.5) * spread;

                // Invoke Physics Mod via reflection
                spawnMethod.invoke(null, state, blockPos, world, sx, sy, sz);
            }
        }
        catch (Exception ignored)
        {
            // Silently swallow: bad block ID, world issues, etc.
            // Never crash BBS or a recording over a particle error.
        }
    }

    // -----------------------------------------------------------------------
    // render3D — cache world-space position from the model matrix
    // -----------------------------------------------------------------------

    @Override
    public void render3D(FormRenderingContext context)
    {
        try
        {
            Matrix4f matrix = context.stack.peek().getPositionMatrix();
            matrix.getTranslation(this.tempTranslation);

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.gameRenderer != null)
            {
                net.minecraft.client.render.Camera camera = mc.gameRenderer.getCamera();
                net.minecraft.util.math.Vec3d camPos = camera.getPos();

                this.worldX = camPos.x + this.tempTranslation.x;
                this.worldY = camPos.y + this.tempTranslation.y;
                this.worldZ = camPos.z + this.tempTranslation.z;
            }
        }
        catch (Exception ignored) { /* render state not ready yet */ }

        // No visible geometry — only particles — so do NOT call super.render3D()
    }
}
