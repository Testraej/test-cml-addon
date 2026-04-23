package com.physicsparticles.bbs;

import com.physicsparticles.bbs.forms.PhysicsParticleForm;
import com.physicsparticles.bbs.forms.PhysicsParticleFormRenderer;
import com.physicsparticles.bbs.ui.UIPhysicsParticleForm;
import mchorse.bbs_mod.events.BBSAddonMod;
import mchorse.bbs_mod.events.Subscribe;
import mchorse.bbs_mod.events.register.RegisterFormsRenderersEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Client-side BBS addon entrypoint.
 *
 * Registered under "bbs-addon-client" in fabric.mod.json.
 * Only runs on the client — safe to reference rendering classes here.
 *
 * Responsibilities:
 *  1. Register PhysicsParticleFormRenderer so BBS knows how to render the form.
 *  2. Register UIPhysicsParticleForm so BBS shows the correct editor panel.
 */
@Environment(EnvType.CLIENT)
public class PhysicsParticlesClientAddon implements BBSAddonMod
{
    @Subscribe
    public void onRegisterFormsRenderers(RegisterFormsRenderersEvent event)
    {
        // 1. Renderer: called every frame + every tick via ITickable
        event.registerRenderer(
            PhysicsParticleForm.class,
            PhysicsParticleFormRenderer::new   // factory: form -> renderer
        );

        // 2. Editor UI: the panel shown when the user opens this form in BBS
        event.registerPanel(
            PhysicsParticleForm.class,
            UIPhysicsParticleForm::new         // supplier: () -> new editor instance
        );
    }
}
