package com.physicsparticles.bbs.ui;

import com.physicsparticles.bbs.forms.PhysicsParticleForm;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.ui.UIKeys;
import mchorse.bbs_mod.ui.forms.editors.forms.UIForm;
import mchorse.bbs_mod.ui.forms.editors.panels.UIFormPanel;
import mchorse.bbs_mod.ui.framework.elements.UIScrollView;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIToggle;
import mchorse.bbs_mod.ui.framework.elements.input.UITrackpad;
import mchorse.bbs_mod.ui.framework.elements.input.text.UITextbox;
import mchorse.bbs_mod.ui.utils.UI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Edit panel for PhysicsParticleForm.
 *
 * Layout (top to bottom, scrollable):
 *   [Block ID textbox]   — e.g. "minecraft:grass_block"
 *   [Paused toggle]
 *   ── Emission ──
 *   [Count]  [Frequency]
 *   ── Scatter ──
 *   [Spread]
 *   ── Velocity ──
 *   [Vel X]  [Vel Y]  [Vel Z]
 *   ── Offset ──
 *   [Off X]  [Off Y]  [Off Z]
 */
@Environment(EnvType.CLIENT)
public class UIPhysicsParticleFormPanel extends UIFormPanel
{
    // UI controls
    public UITextbox blockId;
    public UIToggle  paused;
    public UITrackpad count;
    public UITrackpad frequency;
    public UITrackpad spread;
    public UITrackpad velocityX;
    public UITrackpad velocityY;
    public UITrackpad velocityZ;
    public UITrackpad offsetX;
    public UITrackpad offsetY;
    public UITrackpad offsetZ;

    // Reference to the form currently being edited
    private PhysicsParticleForm currentForm;

    public UIPhysicsParticleFormPanel(UIForm editor)
    {
        super(editor);

        // ── Block ID ──────────────────────────────────────────────────────────
        this.blockId = new UITextbox(256, (value) -> {
            if (this.currentForm != null) this.currentForm.blockId.set(value);
        });

        // ── Paused toggle ────────────────────────────────────────────────────
        this.paused = new UIToggle(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_PAUSED, (toggle) -> {
            if (this.currentForm != null) this.currentForm.paused.set(toggle.getValue());
        });

        // ── Count & Frequency ────────────────────────────────────────────────
        this.count = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.count.set(value.intValue());
        }).integer().tooltip(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_COUNT);

        this.frequency = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.frequency.set(value.intValue());
        }).integer().tooltip(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_FREQUENCY);

        // ── Spread ──────────────────────────────────────────────────────────
        this.spread = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.spread.set(value.floatValue());
        }).tooltip(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_HORIZONTAL); // reuse "Horizontal scatter" label

        // ── Velocity ────────────────────────────────────────────────────────
        this.velocityX = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.velocityX.set(value.floatValue());
        }).tooltip(UIKeys.GENERAL_X);

        this.velocityY = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.velocityY.set(value.floatValue());
        }).tooltip(UIKeys.GENERAL_Y);

        this.velocityZ = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.velocityZ.set(value.floatValue());
        }).tooltip(UIKeys.GENERAL_Z);

        // ── Offset ──────────────────────────────────────────────────────────
        this.offsetX = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.offsetX.set(value.floatValue());
        }).tooltip(UIKeys.GENERAL_X);

        this.offsetY = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.offsetY.set(value.floatValue());
        }).tooltip(UIKeys.GENERAL_Y);

        this.offsetZ = new UITrackpad((value) -> {
            if (this.currentForm != null) this.currentForm.offsetZ.set(value.floatValue());
        }).tooltip(UIKeys.GENERAL_Z);

        // ── Layout ──────────────────────────────────────────────────────────
        UIScrollView options = new UIScrollView();

        options.add(
            UI.label(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_TITLE),  // "Block ID"
            this.blockId,
            this.paused,

            UI.label(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_EMISSION).marginTop(8),
            this.count.marginTop(4),
            this.frequency.marginTop(4),

            UI.label(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_SCATTER).marginTop(8),
            this.spread.marginTop(4),

            UI.label(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_VELOCITY).marginTop(8),
            this.velocityX.marginTop(4),
            this.velocityY.marginTop(4),
            this.velocityZ.marginTop(4),

            UI.label(UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_OFFSET).marginTop(8),
            this.offsetX.marginTop(4),
            this.offsetY.marginTop(4),
            this.offsetZ.marginTop(4)
        );

        this.add(options.full());
    }

    // -----------------------------------------------------------------------
    // Panel lifecycle
    // -----------------------------------------------------------------------

    /**
     * Called by UIPhysicsParticleForm when the user opens / switches to this
     * form in the BBS editor.  Populate all UI controls with the form's data.
     */
    public void startEdit(PhysicsParticleForm form)
    {
        this.currentForm = form;

        this.blockId.setText((String) form.blockId.get());
        this.paused.setValue((Boolean) form.paused.get());
        this.count.setValue((double) (Integer) form.count.get());
        this.frequency.setValue((double) (Integer) form.frequency.get());
        this.spread.setValue((double) (Float) form.spread.get());
        this.velocityX.setValue((double) (Float) form.velocityX.get());
        this.velocityY.setValue((double) (Float) form.velocityY.get());
        this.velocityZ.setValue((double) (Float) form.velocityZ.get());
        this.offsetX.setValue((double) (Float) form.offsetX.get());
        this.offsetY.setValue((double) (Float) form.offsetY.get());
        this.offsetZ.setValue((double) (Float) form.offsetZ.get());
    }

    /** Called by the generic UIFormPanel machinery */
    @Override
    public void startEdit(Form form)
    {
        if (form instanceof PhysicsParticleForm physForm)
        {
            this.startEdit(physForm);
        }
    }
}
