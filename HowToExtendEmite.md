Emite has a highly modular architectured (based on [Suco](http://suco.googlecode.com)), so add new functionallity is an easy task.

Here is an example of how to build an Echo module (you can find this example in emite source code)

### Write the component/s ###
Components are simple POJOS with dependencies in the constructor. A simple Echo component would be like this:
```
/**
 * Echo respond to any incoming message with the same message body.
 */
public class Echo {
    public Echo(final Session session) {
	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message incoming) {
		session.send(new Message(incoming.getBody(), incoming.getFrom()));
	    }
	});
    }
}
```

### Write a suco module ###
A suco module is a place where one or more components are registered. We can use the $ method to retrieve the dependencies.

Normally the modules also implements the gwt's EntryPoint interface to ensure the suco module is loaded when the gwt module is loaded.

```
public class EchoModule extends AbstractModule implements EntryPoint {
    /**
     * Called by GWT when this module is loaded in a browser
     */
    public void onModuleLoad() {
	// install this module in suco
	Suco.install(this);
    }

    /**
     * Called by Suco when this module is installed in Suco
     */
    @Override
    protected void onInstall() {
	// The SessionComponent decorator take care of calling the factories
	// when a Session is created (see Suco for more info)
	register(SessionComponent.class, new Factory<Echo>(Echo.class) {
	    @Override
	    public Echo create() {
		return new Echo($(Session.class));
	    }
	});
    }
}
```


### Testing the component/s ###
Testing is a important part of emite, so we've created the MockedSession object that allows easily testing of our emite modules. It provides a couple of handy methods:
  1. `receives` method to simulate an incoming stanza
  1. `verifySent` method to verify an outcoming stanza

All the testing infraestrcture is based on JUnit and Mockito, so you can use all tools they provide. Emite library have hundred of test; they are one of the fastest way to learn how emite works. Here is how could be the test for the Echo component:
```
public class EchoTests {
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	new Echo(session);
    }

    @Test
    public void shouldEcho() {
	// simulates a reception
	session.receives("<message from='someone@domain'><body>Hello!</body></message>");
	// verifies the expected output stanza
	session.verifySent("<message to='someone@domain'><body>Hello!</body></message>");
    }
}
```