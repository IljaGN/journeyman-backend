Maven Code Style
------------------------------------------------------------

Стиль оформления `pom.xml` файлов опирается на стандарты описанные
в [Maven Code Style And Code Conventions](https://maven.apache.org/developers/conventions/code.html)
раздел [Generic Code Style And Convention](https://maven.apache.org/developers/conventions/code.html#generic-code-style-and-convention),
раздел [XML](https://maven.apache.org/developers/conventions/code.html#xml). Со следующим измененным
порядком тегов для _pom_ файлов:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion/>
  <parent/>

  <groupId/>
  <artifactId/>
  <version/>
  <packaging/>

  <name/>
  <description/>
  <url/>
  <inceptionYear/>
  <organization/>
  <licenses/>

  <developers/>
  <contributors/>

  <mailingLists/>

  <prerequisites/>

  <properties/>

  <modules/>

  <dependencyManagement/>
  <dependencies/>

  <profiles/>

  <build/>

  <scm/>
  <issueManagement/>
  <ciManagement/>
  <distributionManagement/>

  <repositories/>
  <pluginRepositories/>

  <reporting/>
</project>
```