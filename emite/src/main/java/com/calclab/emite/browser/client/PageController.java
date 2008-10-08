package com.calclab.emite.browser.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;

public class PageController {

    private static final String PARAM_HOST = "emite.host";
    private static final String PARAM_HTTPBASE = "emite.httpBase";
    private static final String PARAM_PASSWORD = "emite.password";
    private static final String PARAM_JID = "emite.user";
    private static final String PARAM_CLOSE = "emite.onClose";
    private static final String PARAM_OPEN = "emite.onOpen";
    private static final String PARAM_PAUSE = "emite.pause";
    private final DomAssist assist;
    private final Connection connection;
    private final Session session;

    public PageController(final Connection connection, final Session session, final DomAssist assist) {
	this.connection = connection;
	this.session = session;
	this.assist = assist;
    }

    public void init() {
	Log.debug("PageController - initializing...");
	final String onCloseAction = assist.getMeta(PARAM_CLOSE, "pause");
	prepareOnCloseAction(onCloseAction);
	configureConnection();
	final String onOpenAction = assist.getMeta(PARAM_OPEN, "resume");
	prepareOnOpenAction(onOpenAction);
	Log.debug("PageController - done.");
    }

    private void configureConnection() {
	Log.debug("PageController - configuring connection...");
	final String httpBase = assist.getMeta(PARAM_HTTPBASE, true);
	final String host = assist.getMeta(PARAM_HOST, true);
	Log.debug("CONNECTION PARAMS: " + httpBase + ", " + host);
	connection.setSettings(new BoshSettings(httpBase, host));
    }

    private void pauseConnection() {
	Log.debug("Pausing connection...");
	final StreamSettings stream = session.pause();
	if (stream != null) {
	    final String user = session.getCurrentUser().toString();
	    final SerializableMap map = new SerializableMap();
	    map.put("rid", "" + stream.rid);
	    map.put("sid", stream.sid);
	    map.put("wait", stream.wait);
	    map.put("inactivity", stream.inactivity);
	    map.put("maxPause", stream.maxPause);
	    map.put("user", user);
	    final String serialized = map.serialize();
	    Log.debug("Pausing session: " + serialized);
	    Cookies.setCookie(PARAM_PAUSE, serialized);
	}
    }

    private void prepareOnCloseAction(final String onCloseAction) {
	Log.debug("PageController - configuring close action...");
	final boolean shouldPause = "pause".equals(onCloseAction);
	Window.addWindowCloseListener(new WindowCloseListener() {
	    public void onWindowClosed() {
		if (shouldPause) {
		    pauseConnection();
		} else {
		    session.logout();
		    Log.debug("Logged out!");
		}
	    }

	    public String onWindowClosing() {
		return null;
	    }
	});
    }

    private void prepareOnOpenAction(final String onOpenAction) {
	Log.debug("PageController - configuring opening action...");
	if ("resume".equals(onOpenAction)) {
	    tryToResume();
	} else if ("login".equals(onOpenAction)) {
	    tryToLogin();
	}
    }

    private void tryToLogin() {
	final String userJID = assist.getMeta(PARAM_JID, false);
	final String password = assist.getMeta(PARAM_PASSWORD, false);
	if (userJID != null) {
	    Log.debug("Loging in...");
	    if ("anonymous".equals(userJID.toLowerCase())) {
		session.login(Session.ANONYMOUS, null);
	    } else {
		final XmppURI jid = uri(userJID);
		session.login(jid, password);
	    }
	} else {
	    Log.debug("No action perfomer on open.");
	}
    }

    private void tryToResume() {
	final String pause = Cookies.getCookie(PARAM_PAUSE);
	if (pause != null) {
	    Log.debug("Resume session: " + pause);
	    Cookies.removeCookie(PARAM_PAUSE);
	    final SerializableMap map = SerializableMap.restore(pause);
	    final StreamSettings stream = new StreamSettings();
	    stream.rid = Integer.parseInt(map.get("rid"));
	    stream.sid = map.get("sid");
	    stream.wait = map.get("wait");
	    stream.inactivity = map.get("inactivity");
	    stream.maxPause = map.get("maxPause");
	    final XmppURI user = uri(map.get("user"));
	    session.resume(user, stream);
	}
    }
}