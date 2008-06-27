package com.calclab.emite.client.core.bosh2;

import com.calclab.emite.client.core.packet.Packet;

public class B2Stream {

    private Packet body;
    private String sid;

    public Packet getBody() {
	return body;
    }

    public void init(final String domain, final String version, final String wait, final String hold) {
	sid = null;

	body = new Packet("body", "http://jabber.org/protocol/httpbind");
	body.With("xmpp:version", version).With("xmlns:xmpp", "urn:xmpp:xbosh");
	body.With("content", "text/xml; charset=utf-8").With("xml:lang", "en");
	body.With("to", domain).With("secure", "true").With("wait", wait).With("ack", "1").With("hold", hold);

    }

    public void prepareToRespond() {

    }

    public void setAttributes(final String sid, final int poll, final int requests) {
	this.sid = sid;
    }

}