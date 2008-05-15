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
package com.calclab.emite.client.extra.avatar;

import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;

public class AvatarModule implements Module {
    private static final Class<AvatarManager> COMPONENTS_MANAGER = AvatarManager.class;

    public static AvatarManager getAvatarManager(final Container components) {
	return components.getInstance(COMPONENTS_MANAGER);
    }

    public void onLoad(final Container container) {
	final Emite emite = CoreModule.getEmite(container);
	final AvatarManager avatar = new AvatarManager(emite);
	container.registerSingletonInstance(COMPONENTS_MANAGER, avatar);
    }
}