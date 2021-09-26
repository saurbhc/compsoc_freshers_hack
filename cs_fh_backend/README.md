# compsoc_freshers_hack
CompSoc Society - Fresher's Hack Backend Repository - CrashAndBurn()

Pre Requisites -
1. Java 11
2. Maven

Steps to scale the service -
1. Update bootstrap.yml (Add application.yml file path)
2. Update application.yml (Add postgres credentials and google coud json auth file path)
3. Run the below commands on CLI -
    mvn clean install
    mvn spring-boot:run
4. Service would be scaled up.