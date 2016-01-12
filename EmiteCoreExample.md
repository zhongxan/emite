## A Emite Core hello world example ##

```

/**
 * A example of how to use the Emite Core funciontallity. A GWT program that
 * send a (surpise!) hello world message and receives messages and presence
 *
 */
public class EmiteCoreExample implements EntryPoint {

    public void onModuleLoad() {
    /*
     * First, we have to configure the server settings. Normally this is
     * done in the html (not in the code)... but this is only an example
     */
    Suco.get(Connection.class).setSettings(new BoshSettings("/myProxyURL", "myServerHostName"));

    /*
     * We get the Session object. The most important object of Emite Core
     * module.
     */
    final Session session = Suco.get(Session.class);

    /*
     * We track session state changes. We can only send messages when the
     * state == loggedIn.
     */
    session.onStateChanged(new Listener<Session.State>() {
        public void onEvent(final State state) {
            if (state == Session.State.loggedIn) {
                GWT.log("We are now online", null);
                sendHelloWorldMessage(session);
            } else if (state == Session.State.disconnected) {
                GWT.log("We are now offline", null);
            }
        }
    });

    /*
     * We show every incoming message in the GWT log console
     */
    session.onMessage(new Listener<Message>() {
        public void onEvent(final Message message) {
            GWT.log("Messaged received from " + message.getFrom() + ":" + message.getBody(), null);
        }
    });

    /*
     * We show (log) every incoming presence stanzas
     */
    session.onPresence(new Listener<Presence>() {
        public void onEvent(final Presence presence) {
            GWT.log("Presence received from " + presence.getFrom() + ": " + presence.toString(), null);
            }
        });

        session.login(uri("myJID@myDomain.org"), "myPassword");
    }

    /**
     * The simplest way to send a message using the Session object
     */
    private void sendHelloWorldMessage(final Session session) {
        final Message message = new Message("hello world!", uri("everybody@world.org"));
        session.send(message);
    }
}

```