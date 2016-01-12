# Introduction #



# Details #

Install ejabberd
```
sudo apt-get install ejabberd
```

Modify the ejabberd.cfg configuration file (sudo vim /etc/ejabberd/ejabberd.cfg) to add the following text to the middle of the "modules" section:
```
{mod_http_bind, []},
```
Modify the ejabberd.cfg configuration file to modify the grouping of lines that begin "{5280, ejabberd\_http" with the following text (near the end of the "listen" section):
```
{5280, ejabberd_http, [
     http_poll,
     web_admin,
     {request_handlers, [
         {["http-bind"], mod_http_bind}
     ]}
  ]}
```
Start (or stop and start again) the ejabberd server so that the configuration modifications take effect.
```
sudo /etc/init.d/ejabberd stop
sudo /etc/init.d/ejabberd start
```
Register the test1 and test2 users on your ejabberd server with the following 2 commands:
```
ejabberdctl register test1 localhost test1
ejabberdctl register test2 localhost test2
```
Add the following lines to your web.xml file:
```
	<servlet>
		<servlet-name>Proxy</servlet-name>
		<servlet-class>de.spieleck.servlets.ProxyServlet</servlet-class>
		<init-param>
			<param-name>remotePath</param-name>
			<param-value>/http-bind/</param-value>
		</init-param>
		<init-param>
			<param-name>remoteServer</param-name>
			<param-value>localhost</param-value>
		</init-param>
		<init-param>
			<param-name>remotePort</param-name>
			<param-value>5280</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>Proxy</servlet-name>
		<url-pattern>/proxy</url-pattern>
       </servlet-mapping>
```