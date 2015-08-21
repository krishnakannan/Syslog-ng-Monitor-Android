# Syslog-ng Monitor for Android

## About ##

Syslog-ng is an open source implementation of the Syslog protocol for Unix and Unix-like systems. It extends the original syslogd model with content-based filtering, rich filtering capabilities, flexible configuration options and adds important features to syslog, like using TCP for transport.

Syslog-ng Monitor for Android is a monitoring application and with this application, you can execute various commands to know whether the monitored Syslog-ng is alive, get current statistics of Syslog-ng(s), and to view the configuration of Syslog-ng. In this application you can add/store details of Syslog-ng(s) to monitor with ease. You can also edit and delete the details listed after storing. After storing the details you can perform the above mentioned commands on single click.

## Configuration ##

After installing the application, You can start the configuring the application by adding the details of Syslog-ng Servers.

Syslog-ng Name : Start by giving the Syslog-ng that you want to monitor a name.
Hostname : Specify the hostname of Syslog-ng Server.
Port number : Specify the port number of the Syslog-ng Server.

### Optional ###

If you want to include a client certificate to be sent along with the request.
Please import the certificate in the application.

Upon import, the certificate will be available to be included while configuration happens.

To include certificate 

* Click on Include Client Certificate.
* Select Certificate.
* Enter the Certificate Password.

## Monitoring ##

After the successful configuration, start monitoring the configured Syslog-ng

### Commands ###

Currently the application supports 3 commands. They are

* stats
* is_alive
* show_config

The configured Syslog-ng(s) can be edited and deleted easily.