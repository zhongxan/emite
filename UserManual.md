## Basic concepts ##

### Emite modules ###
Emite is divided in modules. Each module registers a series of components inside a container
(using the Suco library http://suco.googlecode.com).
You can access the any registered component using the Suco facade. For example, to retrieve
a xmpp session component you should use this code:
```
Session session = Suco.get(Session.class);
```

The only thing you have to do is inherit the modules you need in your 

&lt;AppName&gt;

.gwt.xml file:
```
<module>
  <inherits name="com.calclab.emite.core.EmiteCore" />
  <inherits name="com.calclab.emite.im.EmiteIM" />
  <inherits name="com.calclab.emite.xep.muc.EmiteMUC" />

  ...
</module>
```

Currently the following modules are completely developed:
  * Core: com.calclab.emite.core.EmiteCore
  * InstantMessaging: com.calclab.emite.im.EmiteIM
  * Multi-user chats: com.calclab.emite.xep.muc.EmiteMUC

### Asynchronous ###
Because the asynchronous nature of xmpp communications, you usually perform actions in two stages:
first you send a request and then you listen for that request to complete. To attach a listener to a component use the onXXX methods of that component.


For example, to perform a login you can write this code:
```
  Session session = Suco.get(Session.class);

  session.onStateChanged(new Listener<Session.State>() {
    public void onEvent(Session.State state) {
      if (state == Session.State.ready) {
        // now we're logged in
      }
    }
  });

  // request to login
  session.login(XmppURI.uri("userName@domain"), "password");
  // at this point, the login process is not completed!
```

### Packets ###
Xmpp communications are xml intensive. IPacket is a interface to query a xml response. Also you can
use the Packet class (or any of its child classes: Presence, Message, IQ) to create xml in a friendly way:

```
  Session session = Suco.get(Session.class);
  session.login(XmppURI.uri("me@domain", "myPassword");
  Message message = new Message("send this");
  session.send(message);
```



## Core module ##

The core module register the following components:
  * A xmpp connection component (com.calclab.emite.core.client.bosh.Connection)
  * A xmpp session component (com.calclab.emite.core.client.xmpp.Session)

### Connection component ###

You need to configure the connection component before anything else. The connection object
require two parameters:
  * The httpBase: the url of the xmpp bosh service (remember: due the same origin policy of browsers, the url should be relative of the page where the client is loaded from)
  * The server domain name

Using the emite browser module (not documented here :-( ) you can skip this configuration process.

Here is an example of how to configure the session component:
```
  Connection connection = Suco.get(Connnection.class);
  connection.setSettings(new Bosh3Settings("httpBase", "domain"));
```

### Session component ###

The session is the main component of emite. You can send and receive stanzas (xmpp valid xml fragments),
login, logout, pause/resume the connection, track the session state... see javadocs for more info.

You already seen a example of how to login into a session.

¿what examples do you want to see here? please, write us!

## InstantMessaging module ##

This module register the following components:
  * A ChatManager that helps to use different conversations at the same time
  * A Roster component to manage the user's contacts
  * A PresenceManager to change your own presence
  * A PresenceSubscriptionManager that handles (automatically or not, you can choose ;-) ) the subscription issues



## Help us ##

Obviously this documentation is not very exhaustive. ¿what do you want to see here?