Emite its a pure gwt library, so no server code is involved.
You may ask yourself "[why do you I need to install a proxy?](FAQ.md)"


## Prerequisites ##

You need a running xmpp server with Http Binding connection enabled. See our [Server and Client Compatibility Matrix](http://code.google.com/p/emite/wiki/ServerAndClientsCompatibility)

## Environment setup ##

  1. install maven2 version 2.0.7 or over (http://maven.apache.org)
  1. **remove previous configuration in settings.xml in ~/.m2 folder if you used emite and maven2-gwt-plugin version < 2.0 before**
  1. configure your server Http Binding Service to listen at `http://localhost:5280/http-bind/` or tweak the web.xml file of emite to change this values

## Run the example from the sources ##

  1. check out the code: `svn checkout http://emite.googlecode.com/svn/trunk/emite emite`
  1. inside the emite directory type `mvn -P emiteui gwt-maven:gwt` to run emite  UI (a gwt-ext emite front end) using the GWTShell ...


## Using Eclipse ##

  1. run ` mvn eclipse:eclipse `
  1. import the project from eclipse

## Use emite in your own project ##

There are other ways, but the fastest is to use maven2:

  1. create a new maven project if needed: `mvn archetype:create -DgroupId=com.mycompany.app -DartifactId=my-webapp -DarchetypeArtifactId=maven-archetype-webapp`
  1. Add this to the resulting pom.xml:
```
<?xml version="1.0" encoding="UTF-8"?>
<project>
 ...

  <dependencies>
    <dependency>
      <groupId>com.calclab</groupId>
      <artifactId>emite</artifactId>
      <version>0.4.7</version>
    </dependency>
  </dependencies>
</project>
```

See Appendix C for more pom.xml configuration options.

  1. Create a new gwt project with the current gwt module dependencies:
```
<!-- library: required -->
<inherits name="com.calclab.emite.Emite" />
<!-- if you want to include the emite ui in your application -->
<inherits name="com.calclab.emiteuimodule.EmiteUIModule" />
<!-- to make GWTShell use the java proxy -->
<servlet path="/proxy" class="de.spieleck.servlets.ProxyServlet"/>
```

Also, if you want **to use the ui you have to configure the connection settings in your app's html file**. Take a look at http://emite.googlecode.com/svn/trunk/emite/src/main/java/com/calclab/emiteui/public/EmiteUI.html to see an example.


## Appendix A: Configuration of maven ##

**IMPORTANT!! THIS IS NOT NEEDED WITH gwt-maven 2.0RC**

Example of maven .m2/settings.xml to work with gwt-maven plugin:

```
<?xml version="1.0" encoding="UTF-8"?>
<settings>
   <profiles>
     <profile>
       <id>gwt-1.4.61</id>
       <properties>
         <google.webtoolkit.home>/usr/local/lib/gwt/gwt-linux-1.4.61</google.webtoolkit.home>
         <google.webtoolkit.extrajvmargs>-Xmx512M</google.webtoolkit.extrajvmargs>
         <!-- XstartOnFirstThread needed only on the mac -->
         <!-- <google.webtoolkit.extrajvmargs>-XstartOnFirstThread</google.webtoolkit.extrajvmargs> -->
      </properties>
     </profile>
     <profile>
       <id>gwt-1.5.3</id>
       <properties>
         <google.webtoolkit.home>/usr/local/lib/gwt/gwt-linux-1.5.3</google.webtoolkit.home>
         <google.webtoolkit.extrajvmargs>-Xmx512M</google.webtoolkit.extrajvmargs>
         <!-- XstartOnFirstThread needed only on the mac -->
         <!-- <google.webtoolkit.extrajvmargs>-XstartOnFirstThread</google.webtoolkit.extrajvmargs> -->
      </properties>
     </profile>
   </profiles>
   <activeProfiles>
     <activeProfile>gwt-1.5.3</activeProfile>
   </activeProfiles>
</settings>
```

## Appendix B: jar and war generation ##

To generate a jar from the sources type `mvn package` , or `mvn install` to install it in your current maven repository.

If you're planning to install it in your own web server, you can generate a war with: `mvn compile war:war`

## Appendix C: more pom.xml options ##

We usually use [gwt-maven](http://code.google.com/p/gwt-maven/) plugin to compile and run the gwt applications, and jetty to run the proxies. You can configure them adding this lines to pom.xml
```

<?xml version="1.0" encoding="UTF-8"?>
<project>
  
 <!-- more stuff here -->

  <build>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.5</source>
          <target>1.5</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>com.totsp.gwt</groupId>
        <artifactId>maven-googlewebtoolkit2-plugin</artifactId>
        <version>2.0-beta14</version>
        <configuration>
          <style>OBF</style>
          <runTarget>
       <!-- [[ YOUR APP HTML FILE HERE ]] -->
          </runTarget>
          <compileTarget>
            <param></param>
          </compileTarget>
          <compileTargets>
            <compileTarget>
        <!-- [[ YOUR APP MODULE NAME HERE ]] -->
            </compileTarget>
          </compileTargets>
          <generatorRootClasses></generatorRootClasses>
          <generatorDestinationPackage>
       <!-- [[ YOUR APP MODULE NAME HERE ]] -->
          </generatorDestinationPackage>
          <generateGettersAndSetters>
            false
          </generateGettersAndSetters>
          <generatePropertyChangeSupport>
            false
          </generatePropertyChangeSupport>
        </configuration>
        <executions>
          <execution>
            <goals>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>maven-jetty-plugin</artifactId>
        <version>6.1.5</version>
        <configuration>
          <contextPath>/</contextPath>
          <scanIntervalSeconds>0</scanIntervalSeconds>
          <connectors>
            <connector
              implementation="org.mortbay.jetty.nio.SelectChannelConnector">
              <port>4444</port>
              <maxIdleTime>60000</maxIdleTime>
            </connector>
          </connectors>
        </configuration>
      </plugin>
    </plugins>
  </build>

</project>


```

## Appendix D: Apache configuration ##

First, copy the emite compilation output into your web server.
Some orientative apache configuration for emite (using a proxy for both html/js and bosh). Work in progress:

```
<VirtualHost YOURIP>
  ServerAdmin ADMINEMAIL
  ServerName YOUREMITEDOMAIN

  ExpiresActive on
  ExpiresDefault "now plus 2 hours"
  # No working in apache (debian- sarge), try to uncomment in other apache versions
  # Header append Cache-Control public
  # SetOutputFilter DEFLATE

  <Files ext-*.js>
   ExpiresDefault "now plus 1 year"
  </Files>

  <Files ext-*.css>
   ExpiresDefault "now plus 1 year"
  </Files>

  <Files *.cache.*>
   ExpiresDefault "now plus 1 year"
  </Files>

  <Files *.nocache.*>
   ExpiresDefault "now plus 2 minutes"
  </Files>

<IfModule mod_proxy.c>
  ProxyPass /com.calclab.emiteui.EmiteUI/proxy http://YOURXMPPSERVER:5280/http-bind/
  ProxyPassReverse /com.calclab.emiteui.EmiteUI/proxy http://YOURXMPPSERVER:5280/http-bind/
  # This part is optional (only if you are using a internal web server por emite pages):
  ProxyPass / http://YOUREMITEINNERINSTALLATION/
  ProxyPassReverse / http://YOUREMITEINNERINSTALLATION/
</IfModule>

</VirtualHost>
```

You can change also the 5280 port with your xmpp server bosh port.

Apache applies its `Timeout` directive to BOSH/http-bind connections made through mod\_proxy and kills such connections after the Timeout expires. In order to prevent this please remove the Timeout directive from your httpd.conf file, or use a higger value.