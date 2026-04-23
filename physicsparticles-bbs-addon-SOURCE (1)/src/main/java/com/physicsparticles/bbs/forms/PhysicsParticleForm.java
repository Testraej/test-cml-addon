package com.physicsparticles.bbs.forms;

import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.settings.values.core.ValueString;
import mchorse.bbs_mod.settings.values.numeric.ValueBoolean;
import mchorse.bbs_mod.settings.values.numeric.ValueFloat;
import mchorse.bbs_mod.settings.values.numeric.ValueInt;

/**
 * Physics Particle Form - a BBS form that emits Physics Mod block-breaking
 * particles at its world position. Works just like VanillaParticleForm
 * but uses Physics Mod's 3D physics fragments instead of vanilla particles.
 */
public class PhysicsParticleForm extends Form
{
    /** Block ID to use for the particle texture, e.g. "minecraft:grass_block" */
    public ValueString blockId;

    /** When true, no particles are emitted */
    public ValueBoolean paused;

    /** How many physics fragments to spawn per emission */
    public ValueInt count;

    /** Emit particles every N ticks (1 = every tick, 20 = once per second) */
    public ValueInt frequency;

    /** How spread-out the fragments spawn around the center point */
    public ValueFloat spread;

    /** Initial velocity X of the fragments */
    public ValueFloat velocityX;

    /** Initial velocity Y of the fragments (positive = upward) */
    public ValueFloat velocityY;

    /** Initial velocity Z of the fragments */
    public ValueFloat velocityZ;

    /** Position offset X from the form's world position */
    public ValueFloat offsetX;

    /** Position offset Y from the form's world position */
    public ValueFloat offsetY;

    /** Position offset Z from the form's world position */
    public ValueFloat offsetZ;

    public PhysicsParticleForm()
    {
        this.blockId    = new ValueString("block_id", "minecraft:grass_block");
        this.paused     = new ValueBoolean("paused", false);
        this.count      = new ValueInt("count", Integer.valueOf(8));
        this.frequency  = new ValueInt("frequency", Integer.valueOf(1));
        this.spread     = new ValueFloat("spread", Float.valueOf(0.3F));
        this.velocityX  = new ValueFloat("velocity_x", Float.valueOf(0.0F));
        this.velocityY  = new ValueFloat("velocity_y", Float.valueOf(0.2F));
        this.velocityZ  = new ValueFloat("velocity_z", Float.valueOf(0.0F));
        this.offsetX    = new ValueFloat("offset_x", Float.valueOf(0.0F));
        this.offsetY    = new ValueFloat("offset_y", Float.valueOf(0.0F));
        this.offsetZ    = new ValueFloat("offset_z", Float.valueOf(0.0F));

        /* Mark all as invisible so BBS serialises them but doesn't
           show generic value fields in the generic property list */
        this.add(this.blockId.invisible());
        this.add(this.paused.invisible());
        this.add(this.count.invisible());
        this.add(this.frequency.invisible());
        this.add(this.spread.invisible());
        this.add(this.velocityX.invisible());
        this.add(this.velocityY.invisible());
        this.add(this.velocityZ.invisible());
        this.add(this.offsetX.invisible());
        this.add(this.offsetY.invisible());
        this.add(this.offsetZ.invisible());
    }
}
