Read logs first project
=======================

Besides being the sentence that is most commonly used to reply to someone complaining about an issue in a piece of software, Read Logs First is a project aimed in developing a flexible, extensible and multilanguage log miner.

What do I mean by flexible log miner? I mean that you will be able to use Read Logs First as an API, as well as through the UI, as well as in hybrid mode: you will be able to configure your log set and to visualize the result in the UI (netbeans based) and from there you will run log analysis defined through scripts.

What do I mean by multi-language? Read Logs First API is developed in java but it is designed to be usable through JVM based scripting languages exploiting their comfortable and elegant data analysis functionalities (like python's map and reduce or Scala for loops). At the moment A set of log files can be loaded through the Java API inside a Jython script and the set of log files can be browser through a python iterator.

What do I mean by extensible? Read Logs First is designed such that if you want to write your own log reader that takes log files from your cloud platform (let's say your cloudfoundry platform through vmc) you can and it will be transparently treated as other log readers. The same is valid for log parsers and other log miner components.

Status
======

After the marketing-like statements above, where does this project stand:

Up to now roughly half of the API is developed. Querying capabilities are still missing as well as caching log readers.

UI (based on Netbeans platform) is in early development stages

You can find here the [JavaDoc](http://fpacifici.github.com/readlogsfirst/javadoc/)

Downloads
=========

Here you can find the API in the current version:

[readLogsFirst-0.1-SNAPSHOT.jar](http://fpacifici.github.com/readlogsfirst/releases/readLogsFirst-0.1-SNAPSHOT.jar)