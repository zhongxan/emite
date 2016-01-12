# Introduction #

In order to remove the (bosh) configuration from the source code and to reduce the boiler plate code, some of the parameters and behaviours of the Emite library can be specified using tags inside files.

# 1. Installation #
Simply add this line:
```
 <inherits name="com.calclab.emite.browser.EmiteBrowser" />
```

To your module's .gwt.xml file
# 2. Usage #
## 2.1 Bosh configuration ##
You can configure your bosh configuration settings adding this lines inside the head tag of your html file:
```
 <meta id="emite.httpBase" content="proxy" />
 <meta id="emite.host" content="localhost" />
```

This is completly equivalent to write this configuration code:
> Suco.get(Connection.class).setSettings(new BoshSettings("proxy", "localhost"));

So, if you install this module and add those meta tags inside the html file you don't longer need to writa that configuration code anymore.

## 2.2 Page close behaviour ##
You can configure what is the behaviour os emite when the user enter or leaves the page. The default behavour is close the session (session.logout();) but you can change to pause the session with the following line:
```
 <meta id="emite.onBeforeUnload" content="pause" />
```

## 2.3 Page open behaviour ##
With the BrowserModule installed, emite will automatically try to resume the session if found that it was previously paused. Also it will try to login automatically if this meta tags are found:
```
 <meta id="emite.user" content="test2@localhost" />
 <meta id="emite.password" content="test2" />
```

**WARNING: this is work in progress and currently COMPLETLY UNSECURE. We will implement the ability to encode the passwords... but is NOT currently implemented. But...**
You can force an ANONYMOUS login (much more secure, but usually not enough) with the following line:
```
 <meta id="emite.user" content="anonymous" />
```

Remember that emite won't autologin if a session was previously paused.