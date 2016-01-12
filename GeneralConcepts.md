## Basic concepts ##

### Emite modules ###
Emite is divided in modules (using the gin IoC library: see http://code.google.com/p/google-gin/ for more details).

You can access the any registered component creating a Ginjector that inherits the module(s) you need:
```
public interface ExampleXmppSessionGinjector extends CoreGinjector, BrowserGinjector {
  // you don't have to add nothing here
}
```
and then getting the instances from the Ginjector (in a standard Gin's way):
```
ExampleXmppSessionGinjector ginjector = GWT.create(ExampleXmppSessionGinjector.class);
final XmppSession session = ginjector.getXmppSession();
```


The only thing you have to do is inherit the modules you need in your 

&lt;AppName&gt;

.gwt.xml file:
```
<module>
  <inherits name="com.calclab.emite.core.EmiteCore" />
  <inherits name="com.calclab.emite.browser.EmiteBrowser" />
  ...
</module>
```

See [Emite's module list](ModuleList.md) for a list of available modules.

### Asynchronous ###
Because the asynchronous nature of xmpp communications, you usually perform actions in two stages:
first you send a request and then you listen for that request to complete. To attach a listener to a component use the addXXXListener methods of that component.


For example, to perform a login you can write this code:
```
  XmppSession session = ginjector.getXmppSession();

	session.addSessionStateChangedHandler(true, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
	        if (event.is(SessionStates.loggedIn)) {
	            log("We are now online");
	            sendHelloWorldMessage(session);
	        } else if (event.is(SessionStates.disconnected)) {
	            log("We are now offline");
	        } else {
	            log("Current state: " + event.getState());
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
	XmppSession session = ginjector.getXmppSession();
  session.login(XmppURI.uri("me@domain", "myPassword");
  Message message = new Message("send this");
  session.send(message);
```
