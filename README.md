# nuxeo-script-listing-permissions

nuxeo-script-listing-permissions is a script list all the nuxeo folders that have different rights from the root folder passed as a parameter.


== Simple use ==

 mvn install
 cd target
 java -cp NuxeoDocuments-0.0.1-SNAPSHOT-jar-with-dependencies.jar nuxeo.document.display.permission.DisplayNuxeoDocPermission  url 'password' path >documentPermissions.csv
 
 
where url is the nuxeo server url
      password is the password of nuxeo server
      path is the path of the nuxeo document.
