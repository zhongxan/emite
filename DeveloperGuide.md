_work in progress_

## how it works ##


This is a document to describe the internals of emite and to get used quickier with
the source code. It's not about how to use the library: for this see UserManual

### modules, container, providers and components ###

everything in emite belongs to a module. the module's task is register components in a component container. a component is a instance of any class. this allows to create a highly extensible environment.

the container design is heavily inspired (copied, as far as we can inside gwt envirorment: no reflection) by guice design.

the module register the components using a Provider interface. That allows to lazy-creating the components and the ability to create scopes (singleton, unscoped and session are the built-in scopes. more can be added). Here is an example:
```
public class EchoModule implements SucoModule {
    protected void onInstall(Container container) {
	container.register(Echo.class, new Provider<Echo>() {
	    public Echo get() {
		return new Echo(container.getInstance(Session.class));
	    }
	});
    }
}
```

The EchoModule is a module (implements SucoModule interface) that register a Echo(.class) component using a Provider

&lt;Echo&gt;

 interface. In this case, Echo component **depends** on Emite(.class) component.

### current modules ###

**emite library**
  * CoreModule: implements BOSH connection (including authentication) and xmpp stanzas specification.
  * InstantMessagingModule: implements instant messaging xmpp specification (sessions, presence, roster)

**library extensions**
  * MUCModule: implements XEP-0045: Multi-User Chat
  * ChatStateModule: implements XEP-0085: Chat State Notifications
  * AvatarModule and DiscoveryModule: in development

**emite user interfaces**
  * !EmiteUIModule: implements a full featured chat/rooms using gwt-ext
  * SwingModule: implements a basic featured swing client for testing purposes only
  * Comenta widget: a simple widget to join a room


### event/listeners ###

emite uses a event/listener system of Suco to implement the publisher/observer pattern. using listeners, objects can receive notifications from other objects; using events, objects can publish notifications to other objects.

every object that fire events, have some onXXXX methods that allows add listeners to this event. For example, Session objects have the following onXXX methods:
```
class Session {
  ...
  public void onStateChanged(...) {...}
  public void onPresence(...) {...}
  public void onMessage(...) {...}
  public void onLogin(...) {...}
}
```

if we want to write a component that knows when a message arrives we will do the following:
```
class MyComponent {
  public MyComponent(Session session) {
    session.onMessage(new Listener<Message>() {
       public void onEvent(Message message) {
          System.out.println ("A message arrived: " + message);
       }
    }
  }
}
```