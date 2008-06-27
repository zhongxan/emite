package com.calclab.emiteuimodule.client.sound;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.roster.RosterUIPresenter;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleBuilder;

public class SoundModule implements Module {

    public Class<SoundModule> getType() {
        return SoundModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
        builder.registerProvider(SoundManager.class, new Provider<SoundManager>() {
            public SoundManager get() {
                I18nTranslationService i18n = builder.getInstance(I18nTranslationService.class);
                // Waiting for RosterUIModule:
                RosterUIPresenter rosterUIPresenter = null;
                RoomUIManager roomUIManager = builder.getInstance(RoomUIManager.class);
                StatusUI statusUI = builder.getInstance(StatusUI.class);
                SoundManager soundManager = new SoundManager(rosterUIPresenter, roomUIManager);
                SoundPanel panel = new SoundPanel(soundManager, i18n, statusUI);
                soundManager.init(panel);
                return soundManager;
            }
        });
    }
}