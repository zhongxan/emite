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
package com.calclab.emite.client.core.services.gwt;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.ScheduledAction;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.client.core.services.ServicesModule;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleBuilder;

public class GWTServicesModule extends ServicesModule implements Services, Module {

    public long getCurrentTime() {
	return GWTScheduler.getCurrentTime();
    }

    public void onLoad(final ModuleBuilder builder) {
	ServicesModule.setServices(builder, new GWTServicesModule());
    }

    public void schedule(final int msecs, final ScheduledAction action) {
	GWTScheduler.schedule(msecs, action);
    }

    public void send(final String httpBase, final String request, final ConnectorCallback callback)
	    throws ConnectorException {
	GWTConnector.send(httpBase, request, callback);
    }

    public String toString(final IPacket packet) {
	return GWTXMLService.toString(packet);
    }

    public IPacket toXML(final String xml) {
	return GWTXMLService.toXML(xml);
    }

}