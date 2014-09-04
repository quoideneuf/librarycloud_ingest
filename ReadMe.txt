The Library Commons pipeline (alpha) is an application to ingest metadata, transform, enrich and load for use by the Harvard Library Cloud Item api (v2). 
It is built using the Apache Camel Spring framework, and is meant to be used in conjunction to AWS sqs (amazon web services simple queueing service), though other queueing systems can be substituted).

This application is currently under development. To date, the app handles ingesting marc bibliographic records (in marc21 communications format), converts to marcxml and then to mods. 
More steps for this particular workflow are being developed; additional workflows will be added to handle visual materials and EAD (encoded archival description) archival finding aid component-level data.

Install:
1. check out from github
2. modify src/main/resources/META-INF/spring/camel-context.xml as needed to suit your application (see comments)
3. for use with aws sqs, add the appropriate values in src/main/resources/aws.properties
4. run mvn clean install (maven required)

To run application (standalone, using maven):

1. do mvn camel:run
2. add the .mrc file to the directory specified in camel-context (default is /temp/aleph)
3. add the corresponding .xml file to the same directory (sample message file is at src/test/resources/marcingest.xml)

On the way:

Sample marcingest.mrc file to use for testing with marcingest.xml message
More documentation;
Configuration files and info for running in a servlet container (Tomcat, etc).;
Additional steps for marc pipeline, and new pipelines for other data formats

