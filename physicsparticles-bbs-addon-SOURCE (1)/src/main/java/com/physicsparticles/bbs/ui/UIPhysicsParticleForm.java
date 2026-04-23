package com.physicsparticles.bbs.ui;

import com.physicsparticles.bbs.forms.PhysicsParticleForm;
import mchorse.bbs_mod.ui.UIKeys;
import mchorse.bbs_mod.ui.forms.editors.forms.UIForm;
import mchorse.bbs_mod.ui.utils.icons.Icons;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Top-level UI editor class for PhysicsParticleForm.
 * Registers UIPhysicsParticleFormPanel as the default (and only) panel,
 * exactly mirroring how UIVanillaParticleForm works.
 */
@Environment(EnvType.CLIENT)
public class UIPhysicsParticleForm extends UIForm
{
    public UIPhysicsParticleFormPanel panel;

    public UIPhysicsParticleForm()
    {
        this.panel = new UIPhysicsParticleFormPanel(this);

        // Register the panel with a title and the PARTICLE icon so it shows
        // the correct icon in the BBS editor tab strip.
        this.registerPanel(this.panel, UIKeys.FORMS_EDITORS_VANILLA_PARTICLE_TITLE, Icons.PARTICLE);
    }

    @Override
    protected void registerDefaultPanels()
    {
        // Intentionally empty — our panel is already registered above.
        // (Mirrors the UIVanillaParticleForm pattern exactly.)
    }

    /**
     * Called by BBS when the user selects this form in the replay menu.
     * Cast to our concrete type and forward to the panel.
     */
    @Override
    public void startEdit(mchorse.bbs_mod.forms.forms.Form form)
    {
        if (form instanceof PhysicsParticleForm physForm)
        {
            this.panel.startEdit(physForm);
        }

        super.startEdit(form);
    }
}
