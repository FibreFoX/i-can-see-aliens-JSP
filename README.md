I can see Aliens (JSP)
====================

After having the opportunity to finally get my tests running in one of my projects, i thought by myself:
    why not put all together in one testing-example-project having all kind of tests i find useful.

The result is what you are reading right now.

## Short-description of the project ##
I created a small billboard, where users can register themselves and add comments to the given billboard-entries. There is NO rights-management. Users can delete their accounts and delete their billboard-entries (this will delete the comments too). There is no authentification or anti-spam-technology included, its focused on "how to put tests into a project using a lot of Arquillian".


## How does it work ##
I had to fiddle a lot with the dependencies (you can see my [cdi-sessionscoped-login](github.com/FibreFoX/cdi-sessionscoped-login)-project from the last year), had to learn how maven-profiles are working (never created them first-hand), mostly because Arquillian brings no equal support for testing-frameworks, using JUnit seems to be the more stable than TestNG, but i really like TestNG, so i had to mix the testing-frameworks.
There was another problem: you can only have ONE "testing-container" in classpath for Arquillian, so i had to separate them into maven-profiles for better dependency-handling.
But there comes a downside: my current IDE (Netbeans) goes a little crazy, because when not selecting the right profile i see red lights on my test-packages, so you can get confused by thinking "oh my gosh, my tests are all broken...".


## What tests are included ##
Testing a software includes a lot of pain, when not having the right tools or not having enough knowledge how to write testable code. For having testable code, i tried to utilize CDI as much as possible and using producers for my scoped instances. Maybe there are a lot of reasons not to use producers like i did, but it works out for me. I tried to create the following types of tests:
* unit-tests _(using TestNG)_
* integration-tests _(using TestNG and Arquillian)_
* functional-tests _(using JUnit, Arquillian and Arquillian Drone/Graphene)_
* graybox-tests _(using JUnit, Arquillian, Arquillian Drone/Graphene and Arquillian WARP)_


## Used frameworks/software (buzzword-bingo) ##
* Maven 3.1
* HTML 5
* Twitter Bootstrap (for being hipster and responsive)
* CDI (only one case where i used @EJB-annotation, because of unclear CDI-standard)
* JavaEE 6
* JPQL
* EclipseLink (JPA 2)
* TomEE
* Arquillian
* Arquillian Drone/Graphene2
* Arquillian Warp
* JaCoCo
* ShrinkWrap
* PhantomJS _(as browser for easy project-sharing and CI-systems like Jenkins)_
* SonarQube


## How to use ##
First of all, you have to download the binaries i used for testing: http://phantomjs.org/download.html
Put that corresponding files into "tools/phantom/YOUR-ARCH/" and check the pom.xml, if the paths are matching.
The default `mvn clean install` will only compile the project and will produce a fine WAR-file, but does not execute any test (because they are separated in respective profiles). I put a small helper (for windows-users) called `createAndAllTests.bat` which does nothing more than this

    mvn clean install
    mvn test -Punit
    mvn integration-test -Pintegration
    mvn integration-test -Pfunctional
    mvn integration-test -Pgraybox


## Plans for the future ##
Having to develop in teams often brings the problem with binaries, so i have chosen to put that binaries in a separated tools-folder, haven't found them on mvnrepository, but maybe in the future there will be a reliable source.