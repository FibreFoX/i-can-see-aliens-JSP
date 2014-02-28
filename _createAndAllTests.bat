call mvn clean install
call mvn test -Punit
call mvn integration-test -Pintegration
call mvn integration-test -Pfunctional
call mvn integration-test -Pgraybox
pause