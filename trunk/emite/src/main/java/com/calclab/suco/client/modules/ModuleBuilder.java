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
package com.calclab.suco.client.modules;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.DelegatedContainer;
import com.calclab.suco.client.container.HashContainer;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.scopes.Scope;
import com.calclab.suco.client.scopes.Scopes;

/**
 * A container with module installation support
 * 
 * @deprecated use AbstractModule
 */
@Deprecated
public class ModuleBuilder extends DelegatedContainer {
    private HashMap<Class<?>, Module> modules;

    public ModuleBuilder(final Container delegate) {
	super(delegate);
    }

    ModuleBuilder() {
	this(new HashContainer());
	this.modules = new HashMap<Class<?>, Module>();
    }

    /**
     * load the modules list into the container
     * 
     * @param module
     *            list
     */
    public void add(final Module... toAddModules) {
	for (final Module m : toAddModules) {
	    loadIfNeeded(m);
	}
    }

    @Override
    public <T> T getInstance(final Class<T> componentType) {
	Log.debug("Getting dependency: " + componentType);
	return super.getInstance(componentType);
    }

    public <C> Provider<C> registerProvider(final Class<C> type, final Provider<C> provider,
	    final Class<? extends Scope> scopeType) {
	final Provider<C> scoped = Scopes.get(scopeType).scope(type, provider);
	super.registerProvider(type, scoped);
	return scoped;
    }

    private void loadIfNeeded(final Module m) {
	final Class<?> type = m.getType();
	final Module oldModule = modules.get(type);
	if (oldModule == null) {
	    modules.put(type, m);
	    m.onLoad(this);
	}
    }
}