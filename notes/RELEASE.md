# Current release workflow
 - Remove `-SNAPSHOT` from maven version number (on develop)
 - Run `mvn clean install` to make sure all junit tests work
 - Merge into main and tag commit
 - Back on develop, increase version number and append `-SNAPSHOT`