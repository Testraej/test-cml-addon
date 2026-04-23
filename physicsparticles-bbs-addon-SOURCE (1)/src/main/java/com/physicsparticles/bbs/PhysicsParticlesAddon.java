package com.physicsparticles.bbs;

import com.physicsparticles.bbs.forms.PhysicsParticleForm;
import mchorse.bbs_mod.events.BBSAddonMod;
import mchorse.bbs_mod.events.Subscribe;
import mchorse.bbs_mod.events.register.RegisterFormsEvent;
import mchorse.bbs_mod.resources.Link;

/**
 * Server-side (common) BBS addon entrypoint.
 *
 * Registered under the "bbs-addon" key in fabric.mod.json.
 * This runs on BOTH client and server, so it must contain only
 * non-client code.  We only register the Form data class here;
 * renderer and UI registration happen in PhysicsParticlesClientAddon.
 */
public class PhysicsParticlesAddon implements BBSAddonMod
{
    /**
     * Called by BBS during initialisation to register all form types.
     *
     * The key "physics_particles" is what BBS uses to serialise/deserialise
     * this form in .json files.  It must be unique across all installed addons.
     */
    @Subscribe
    public void onRegisterForms(RegisterFormsEvent event)
    {
        event.getForms().register(
            Link.create("physics_particles"),   // unique form key
            PhysicsParticleForm.class,           // data class
            null                                 // no extra data (same as VanillaParticleForm)
        );
    }
}
