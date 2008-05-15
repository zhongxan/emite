/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuiplugin.client.params;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emiteuiplugin.client.UserChatOptions;

public class MultiChatCreationParam {

    private final BoshOptions boshOptions;
    private final UserChatOptions userChatOptions;
    private final I18nTranslationService i18nService;
    private final String roomHost;
    private final String chatDialogTitle;
    private final AvatarProvider avatarProvider;

    public MultiChatCreationParam(final String chatDialogTitle, final BoshOptions boshOptions, final String roomHost,
	    final I18nTranslationService i18nService, final AvatarProvider avatarProvider,
	    final UserChatOptions userChatOptions) {
	this.chatDialogTitle = chatDialogTitle;
	this.boshOptions = boshOptions;
	this.roomHost = roomHost;
	this.i18nService = i18nService;
	this.avatarProvider = avatarProvider;
	this.userChatOptions = userChatOptions;
    }

    public AvatarProvider getAvatarProvider() {
	return avatarProvider;
    }

    public BoshOptions getBoshOptions() {
	return boshOptions;
    }

    public String getChatDialogTitle() {
	return chatDialogTitle;
    }

    public I18nTranslationService getI18nService() {
	return i18nService;
    }

    public String getRoomHost() {
	return roomHost;
    }

    public UserChatOptions getUserChatOptions() {
	return userChatOptions;
    }

}