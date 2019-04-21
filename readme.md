# StudyScheduler - Procedure Scheduling Web Application 

## Running app locally
StudyScheduler is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:


```
git clone https://github.com/mike-adamenko/study-scheduler.git
cd study-scheduler
./mvnw package
java -jar target/*.jar
```

You can then access StudyScheduler here: http://localhost:8080/

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately:

```
./mvnw spring-boot:run
```

## Database configuration

StudyScheduler uses an in-memory database (H2) which
gets populated at startup with data.

## Working with StudyScheduler in your IDE

### Prerequisites
The following items should be installed in your system:
* Java 8 or newer.
* git command line tool (https://help.github.com/articles/set-up-git)
* Your prefered IDE 
  * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, just follow the install process here: https://www.eclipse.org/m2e/
  * [Spring Tools Suite](https://spring.io/tools) (STS)
  * IntelliJ IDEA

### Steps:

1) On the command line
```
git clone https://github.com/mike-adamenko/study-scheduler.git
```
2) Inside Eclipse or STS
```
File -> Import -> Maven -> Existing Maven project
```

Then either build on the command line `./mvnw generate-resources` or using the Eclipse launcher (right click on project and `Run As -> Maven install`) to generate the css. Run the application main method by right clicking on it and choosing `Run As -> Java Application`.

3) Inside IntelliJ IDEA

In the main menu, choose `File -> Open` and select the [pom.xml](pom.xml). Click on the `Open` button.

CSS files are generated from the Maven build. You can either build them on the command line `./mvnw generate-resources`
or right click on the `study-scheduler` project then `Maven -> Generates sources and Update Folders`.

A run configuration named `StudySchedulerApplication` should have been created for you if you're using a recent Ultimate
version. Otherwise, run the application by right clicking on the `StudySchedulerApplication` main class and choosing
`Run 'StudySchedulerApplication'`.

4) Navigate to StudyScheduler

Visit [http://localhost:8080](http://localhost:8080) in your browser.


# License

The StudyScheduler application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).

