# Emite Troubleshooting #

## I'm using firefox in debian/ubuntu and I can't hear sounds when a message arrives to emiteui, what can I do? ##

apt-get install mozilla-mplayer # it works for us...

## I get a 502 error using emite with Apache and mod\_proxy ##

Apache applies its `Timeout` directive to BOSH/http-bind connections made through mod\_proxy and kills such connections after the Timeout expires. In order to prevent this please remove the Timeout directive from your httpd.conf file, or use a higger value.

## I get a 503 error when trying to use MUC (rooms) with emite and ejabberd ##
Some possibilities:
  1. check that your room domain resolve correctly.
  1. If you are using the configuration and values of our samples, take into account than we normally use "rooms.localhost" instead of "conference.localhost". Also you have to edit ejabberd.cnf and remove the comments (%%) in the muc section.
This is an example of a valid configuration snippet (using 'rooms' instead of the traditional 'conference' for the MUC domain)
```
  {mod_muc,      [
                  {host, "rooms.@HOST@"},
                  {access, muc},
                  {access_create, muc},
                  {access_persistent, muc},
                  {access_admin, muc_admin}
                 ]},
  
```