# 1. Basic example #
You can find this example in the emite source code (com.calclab.examples.emite.basics)


## 1.1 The gwt module ##
Here we define the emite modules we're goint to use and, in development mode, the proxy

```
<module>
  <inherits name="com.google.gwt.user.User" />
  <inherits name="com.calclab.emite.core.EmiteCore" />
  <inherits name="com.calclab.emite.im.EmiteIM" />

  <entry-point
    class="com.calclab.examples.emite.basics.client.EmiteBasicsEntryPoint" />

  <servlet path="/proxy" class="de.spieleck.servlets.ProxyServlet"/>

</module>
```

## 1.2 The code ##

```
public class EmiteBasicsEntryPoint implements EntryPoint {

    private TextArea area;

    public void onModuleLoad() {
	initOutput();

	// Suco is a facade that give access to every emite component we need
	// ******** 0. Configure connection settings *********
	final Connection connection = Suco.get(Connection.class);
	connection.setSettings(new BoshSettings("proxy", "localhost"));
	// ...but there's a module, BrowserModule, that allows to configure
	// the connections settings in the html directly

	// ******** 1. Session *********
	// Session is the emite component that allows us to login/logout among
	// other things
	final Session session = Suco.get(Session.class);

	// Session.onStateChanged allows us to know the state of the session
	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		print("Session state: " + state);
	    }
	});

	// Session.login and Session.logout are our xmpp entrance and exit
	session.login(uri("test1@localhost"), "test1");

	// After login, we can send messages ...
	session.send(new Message("Hello", uri("test2@localhost")));
	// ... or receive messages ...
	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message message) {
		print("Message arrived: " + message.getBody());
	    }
	});

	// ******** 2. ChatManager *********
	// ... but probably you prefer to use the a powerful abstraction: Chat
	final ChatManager chatManager = Suco.get(ChatManager.class);
	final Conversation chat = chatManager.openChat(uri("test2@localhost"), null, null);
	// with chats you don't have to specify the recipient
	chat.send(new Message("Hello test2"));
	// and you only receive messages from the entity you specified
	chat.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		print("Message from test2 arrived: " + message.getBody());
	    }
	});

	// ******** 3. Roster *********
	// As always, Suco is our friend...
	final Roster roster = Suco.get(Roster.class);
	// ... we're in asynchronous world... use listeners
	// onRosterRetrieved is fired when... surprise! the roster is retrieved
	roster.onRosterRetrieved(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		print("We have the roster");
		for (final RosterItem item : items) {
		    print("Roster item: " + item);
		}
	    }
	});
	// we can track changes in roster items (i.e. roster presence changes)
	// using Roster.onItemUpdated
	roster.onItemUpdated(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		print("Roster item changed:" + item);
	    }
	});
    }

    private void initOutput() {
	area = new TextArea();
	RootPanel.get().add(area);
	print("Welcome to emite basics example.");
    }

    /**
     * a helper method to output messages
     * 
     * @param message
     */
    private void print(final String message) {
	area.setText(area.getText() + "\n" + message);
    }

}
```

## 1.3 The html ##
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>Emite Basics example</title>
        <meta name="gwt:property" content="locale=en">

        <style>
          textarea {width: 80%; margin: 2em auto; height: 20em; }
        </style>


    </head>
    <body>
        <script type="text/javascript" language="javascript"
            src="com.calclab.examples.emite.basics.Basics.nocache.js"></script>
    </body>
</html>
```

# 2. Use the BrowserModule #
The BrowserModule can improve the example in two ways: auto-configure the connection settings using html, and gracefully logout before the page closes.

## 2.1 The gwt module ##
We add the emite Browser module:
```
<module>
  <inherits name="com.google.gwt.user.User" />
  <inherits name="com.calclab.emite.core.EmiteCore" />
  <inherits name="com.calclab.emite.im.EmiteIM" />
  <inherits name="com.calclab.emite.browser.EmiteBrowser" />

  <entry-point
    class="com.calclab.examples.emite.basics.client.EmiteBasicsEntryPoint" />

  <servlet path="/proxy" class="de.spieleck.servlets.ProxyServlet"/>

</module>
```

## 2.2 The code ##
We just have to remove the two lines that configures the connection.

## 2.3 The html ##
Now we specify the connection params and the onClose policy.
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>Emite Basics example</title>
        <meta name="gwt:property" content="locale=en">
        <meta id="emite.httpBase" content="proxy" />
        <meta id="emite.host" content="localhost" />
        <meta id="emite.onClose" content="logout" />

        <style>
          textarea {width: 80%; margin: 2em auto; height: 20em; }
        </style>


    </head>
    <body>
        <script type="text/javascript" language="javascript"
            src="com.calclab.examples.emite.basics.Basics.nocache.js"></script>
    </body>
</html>

```