abels Featured,Phase-Deploy

**update: added a mini section for openfire configuration**

# Introduction #

The following is intended to describe a minimal number of steps for setting up a new development environment where one can develop, test, and debug new [GWT](http://code.google.com/webtoolkit/) applications which use the emite jarfile.  In particular, it uses [Eclipse](http://eclipse.org) v3.4 for a Java development environment, a local [ejabberd](http://www.ejabberd.im) v2.0.3 Jabber server, and the latest GWT development toolkit and emite project jarfiles.  This tutorial assumes some degree of familiarity with Java IDEs (especially Eclipse), high-level concepts around how the Jabber/XMPP protocol and Jabber servers work, and the concepts behind Google's GWT toolkit.

# Tutorial #

## Pre-Steps ##
  1. Download the latest and greatest emite jarfile (v0.4.6 at the time of this writing.)
  1. Download the latest and greatest [Suco project](http://code.google.com/p/suco/) jarfile (v0.6.0 at the time of this writing) as it is a key dependency of emite and is being developed in parallel by the same guys developing emite.

## Create a New GWT Project and Application Stubs ##
  1. The steps in this first section follow the pattern for creating stub files for new GWT projects for import into [Eclipse](http://eclipse.org).  See [Google's GWT tutorial](http://code.google.com/webtoolkit/gettingstarted.html) for more information on these steps.  Start by downloading and installing [GWT](http://code.google.com/webtoolkit/) version 1.5.3 or newer.  (Note that in this section, example commands are shown for a Linux environment and would need to be adjusted slightly for Windows or other environments.)
  1. Start by creating a temporary working directory which will be used as a temporary home for the stub files created by the GWT creator scripts.
```
gwt-linux-1.5.3> mkdir tmp/ ; cd tmp
```
  1. Use the GWT script to create a new project.  Note that the name of your project can be almost anything.  In this tutorial, we will name our project `MyApplication` and thus in this and later steps you may wish to replace references to `MyApplication` with the unique name for your project.
```
tmp> ../projectCreator -eclipse MyApplication
```
  1. Use the GWT script to create stub files for your new application. Pay special attention to the name for your package as its name will be used in whole or in part in later steps.  Also, you probably will want to replace references to `com.example.fresh.client.MyApplication` with your own unique package name.
```
tmp> ../applicationCreator -eclipse MyApplication com.example.fresh.client.MyApplication
```

## Configure for Eclipse 3.4 ##
  1. Download and install the latest release of the [Eclipse](http://eclipse.org) Java development environment (v3.4.1 at the time of this writing).
  1. Select Import -> Existing Projects into Workspace, then select directory containing the new GWT project (i.e. that tmp/ directory) and leave the checkbox on to copy files into your workspace.
  1. Go to the project's Properties -> Java Build Path -> Libraries -> Add External JARs and select the following libraries to be added (you will need to navigate to the appropriate directory to find these files):
```
emite-0.4.6.jar   # provides core emite functionality, but has dependencies on other jars
gwt-log-2.5.3.jar # referenced by emite jarfile for logging functionality
gwt-servlet.jar   # used in development for a proxy servlet, but you likely will not use this in production later
suco-0.6.0.jar    # referenced by emite jarfile providing an abstraction layer used by emite
```

## Adapt the Emite "Core" Example ##
  1. Copy-paste the contents of the EmiteCoreExample class's source code (contents only, not the class declaration as you probably want to keep the class declaration in your stub) into your new `MyApplication` class that implements `EntryPoint`.  You could replace your stub java file with the EmiteCoreExample java file, but you would then need to modify it to adhere to your project/application naming which can be a bit more confusing.
  1. Use the Eclipse IDE to resolve most missing dependencies with the appropriate import statements (which will largely pull from the emite-related jarfiles we added to the project in the prior section). With Eclipse's help, we added the following import statements:
```
import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;
```
  1. Manually add one additional import statement (the Eclipse IDE generally can't help us divine this one) to help define the uri() method:
```
import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
```
> > At this point the Eclipse IDE should have no remaining problems with the code (that is, auto-compilation indicates no problems.)  If there are remaining issues, it is likely something was missed in one of the prior steps.
  1. Modify this line in your GWT application's primary java file:
```
Suco.get(Connection.class).setSettings(new BoshSettings("/myProxyURL", "myServerHostName"));
```
> > to instead read as:
```
Suco.get(Connection.class).setSettings(new BoshSettings("proxy", "localhost"));
```
> > This will point your new widget at the proxy servlet, in its default configured location/url, that is helpfully included in the emite jarfile.  When running your GWT widget in hosted mode (desirable for debugging purposes), your widget will be hosted from your local system, hence the change to "localhost" here.
  1. Modify this line in your GWT application's primary java file:
```
session.login(uri("myJID@myDomain.org"), "myPassword");
```
> > to instead read as:
```
session.login(uri("test1@localhost"), "test1");
```
> > This is the username and password of a user account on your locally installed Jabber server (for which we use ejabberd as described further below.)
  1. Modify this line in your GWT application's primary java file:
```
final Message message = new Message("hello world!", uri("everybody@world.org"));
```
> > to instead read as:
```
final Message message = new Message("hello world!", uri("test2@localhost"));
```
> > This will result in the sending of a message to a `test2@localhost` user which will also be registered on your locally installed jabber server (seen in later steps).
  1. Insert the following 4 lines in your GWT application's gwt.xml file just after the "Other module inherits" comment line:
```
<inherits name='com.calclab.emite.core.EmiteCore'/>
<inherits name='com.calclab.emite.im.EmiteIM'/>
<inherits name='com.calclab.emite.browser.EmiteBrowser'/>
<servlet path="/proxy" class="de.spieleck.servlets.ProxyServlet" />
```
> > The first 3 lines tell GWT to make use of relevant parts of emite in the compiled application.  The 4th line instantiates the proxy servlet mentioned before which will, in its default configuration, redirect all incoming requests to its url (at 'proxy/' relative to the url where the compiled application itself is hosted, which in practice is a semi-randomly chosen port at execution time on localhost) to port 5280 on localhost.
  1. Insert the following 4 lines into your GWT application's public html file just below the `<title>` tag line:
```
<meta name="gwt:property" content="locale=en">
<meta id="emite.httpBase" content="proxy" />
<meta id="emite.host" content="localhost" />
<meta id="emite.onClose" content="logout" />
```
> > These lines, respectively, set the locale, the url for the proxy (remember the single origin rules for javascript code in most browsers), the name of the host (potentially also convenient to change here when deploying into production), and an instruction to log off from the jabber server upon closing of the browser window.

## Configure Local ejabberd Server ##
  1. Download and install [ejabberd](http://www.ejabberd.im) (v2.0.3 at the time of this writing) locally following instructions at the [ejabberd website](http://www.ejabberd.im) (and verify that your installation was successful before proceeding by connecting to it with your favorite jabber client software.)  There are many options for downloading binary or source code packages for ejabberd -- the choice is totally up to you.
  1. Modify the ejabberd.cfg configuration file (found in the directories where you installed ejabberd) to add the following text to the middle of the "modules" section:
```
  {mod_http_bind, []},
```
> > This loads the HTTP-Bind functionality that is built and included with ejabberd v2.0 and above, though it is not enabled by default. Emite will communicate to the jabber server via HTTP-Bind.
  1. Modify the ejabberd.cfg configuration file to modify the grouping of lines that begin "{5280, ejabberd\_http" with the following text (near the end of the "listen" section):
```
  {5280, ejabberd_http, [
     http_poll,
     web_admin,
     {request_handlers, [
         {["http-bind", "com.example.fresh.MyApplication", "proxy"], mod_http_bind},
         {["http-bind"], mod_http_bind}
     ]}
  ]}
```
> > Requests from the GWT + emite application will be proxied through the proxy server and sent to an address that includes the name of the application module.  This configuration setting tells the ejabberd server to map any incoming request to `/http-bind/com.example.fresh.MyApplication/proxy` to the mod\_http\_bind service that it provides.  This request pattern and how it is built is a somewhat subtle subject which must be handled correctly in order for your application to successfully talk to the jabber server. (Note the absence of the mention of "client" in the com.example.blahblahblah above.)
  1. Start (or stop and start again) the ejabberd server so that the configuration modifications take effect.
  1. Register the test1 and test2 users on your ejabberd server with the following 2 commands:
```
ejabberdctl register test1 localhost test1
ejabberdctl register test2 localhost test2
```


## (Alternative) Configure Openfire server ##

Enable "HTTP Bind Settings" (using the configuration console at localhost:9090) and check the port is not used by other service in your machine (we use 5280 as default, if not change the web.xml accordly)

## Run the Example App ##
  1. Start your favorite Jabber client application and log into your local ejabberd server as user `test2@localhost` (whose password was set as: _test2_).
  1. Run your new GWT application in hosted mode by hitting the green Run button in the Eclipse IDE.  This should launch the GWT Browser and run your application in hosted mode.  If all goes well, you will receive a message in your Jabber client sent from `test1@localhost` to `test2@localhost`.

# Further Thoughts #
  * Try the above with the BasicExample instead of the EmiteCoreExample to see a nicer looking UI.
  * Try activating the `mod_http_fileserver` module in the local ejabberd installation, then compile your GWT application into javascript, and deploy it as a web page served by ejabberd's little built-in httpd server.  This is not recommended for production deployments, but for further development and testing it can be very convenient.