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
package com.calclab.emiteuiplugin.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface ChatIcons extends ImageBundle {

    public static class App {
        private static ChatIcons ourInstance = null;

        public static synchronized ChatIcons getInstance() {
            if (ourInstance == null) {
                ourInstance = (ChatIcons) GWT.create(ChatIcons.class);
            }
            return ourInstance;
        }
    }

    /**
     * @gwt.resource away.png
     */
    AbstractImagePrototype away();

    /**
     * @gwt.resource busy.png
     */
    AbstractImagePrototype busy();

    /**
     * @gwt.resource chat.png
     */
    AbstractImagePrototype chat();

    /**
     * @gwt.resource group-chat.png
     */
    AbstractImagePrototype groupChat();

    /**
     * @gwt.resource info.png
     */
    AbstractImagePrototype info();

    /**
     * @gwt.resource info-lamp.png
     */
    AbstractImagePrototype infoLamp();

    /**
     * @gwt.resource invisible.png
     */
    AbstractImagePrototype invisible();

    /**
     * @gwt.resource message.png
     */
    AbstractImagePrototype message();

    /**
     * @gwt.resource new-chat.png
     */
    AbstractImagePrototype newChat();

    /**
     * @gwt.resource new-email.png
     */
    AbstractImagePrototype newEmail();

    /**
     * @gwt.resource new-message.png
     */
    AbstractImagePrototype newMessage();

    /**
     * @gwt.resource not-authorized.png
     */
    AbstractImagePrototype notAuthorized();

    /**
     * @gwt.resource offline.png
     */
    AbstractImagePrototype offline();

    /**
     * @gwt.resource online.png
     */
    AbstractImagePrototype online();

    /**
     * @gwt.resource question.png
     */
    AbstractImagePrototype question();

    /**
     * @gwt.resource unavailable.png
     */
    AbstractImagePrototype unavailable();

    /**
     * @gwt.resource user_add.png
     */
    AbstractImagePrototype userAdd();

    /**
     * @gwt.resource xa.png
     */
    AbstractImagePrototype xa();

}
