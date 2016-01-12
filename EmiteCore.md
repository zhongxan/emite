## Core module ##

The core module register the following components:
  * A xmpp connection component (com.calclab.emite.core.client.conn.XmppConnection)
  * A xmpp session component (com.calclab.emite.core.client.xmpp.XmppSession)

### Connection component ###

You need to configure the connection component before anything else. The connection object
require two parameters:
  * The httpBase: the url of the xmpp bosh service (remember: due the same origin policy of browsers, the url should be relative of the page where the client is loaded from)
  * The server domain name

(You can skip this configuration process if you use the EmiteBrowser module -recommended-)

Here is an example of how to configure the session component:
```
  XmppConnection connection = ginjector.create(XmppConnection.class);
  connection.setSettings(new ConnectionSettings("http://myapp.com/xmpp-proxy", "mydomain.com"));
```

The proxy url is the url where Emite will send the xmpp bosh requests. The domain is the Xmpp server domain.

### Session component ###

The session is the main component of emite. You can send and receive stanzas (xmpp valid xml fragments),
login, logout, pause/resume the connection, track the session state...

The session component is the class XmppSession (Session class is the deprecated old emite api). See javadocs for details