# spring-batch-demo
Spring batch demo

1. My Dev enviroment 👍
   - Fedora 42
   - VSCode
   		- Plugins
   			- Spring Tools 
   			- Java
   			- Project Lombok
   - JDK 21
   - Maven  3.9.9
   - git 
   - Postman

2. Install if necessary git, follow the instruction on the link below.
	- ```  
	   https://git-scm.com/downloads 
	  ```
	- After install run the command below in the terminal
		- ``` 
		   git config --global core.autocrlf true
		  ```

3. Install if necessary gh, follow the instruction on the link below.
	- ``` https://cli.github.com/manual/installation ```

4. try to access the link below
	- ``` https://github.com/pontalti/spring-batch-demo ```

5. Clone the repository
	- ``` git clone git@github.com:pontalti/spring-batch-demo.git ```


6. If necessary install the JDK 21, download it on the link below
	- ``` https://www.oracle.com/java/technologies/downloads/ ```
	- Choose your distribution and install the JDK
	- Create the Java Home
		- Windows -> ``` JAVA_HOME = [YOUR_PATCH]\jdk-21 ```
		- Linux -> ``` JAVA_HOME = [YOUR_PATCH]/jdk-21 ```
	- Put the JAVA_HOME on the System Patch
		- For Windows -> ``` %JAVA_HOME%\bin ```
		- For Linux -> ``` export PATH=$JAVA_HOME/bin:$PATH ```
	- Test JDK on command line
		- ``` java -version ```		

7. If necessary install Maven, download it on the link below
	- ``` https://maven.apache.org/download.cgi ```
	- Extract compressed file in your prefered tool folder.
	- Create the M2_HOME
		- Windows -> ``` M2_HOME = [YOUR_PATCH]\apache-maven-3.6.3 ```
		- Linux -> ``` M2_HOME = [YOUR_PATCH]/apache-maven-3.6.3 ```
	- Put the Maven on the System Patch
		- For Windows -> ``` %M2_HOME%\bin ```
		- For Linux -> ``` export PATH=$M2_HOME/bin:$PATH ```
	- Test Maven on command line
		- ``` mvn --version ```

8. If necessary install your favorite IDE with support to JDK 21.

9. if necessary Install the project Lombok on your IDE, follow the instruction on the link below.
	- ``` https://projectlombok.org/setup/overview ```

10. Open the project in favotite IDE

11. Replace the path below on the application.yml
	- ``` temp-storage: ${user.home}/apps/spring-batch-demo/temp_storage_spring_batch/ ``` 

12. To build please.
	- Go to the project root folder.
	- Run the command below.
		- ``` mvn -U clean install package spring-boot:repackage ```

13. To run the SpringBoot application in localhost.
	- Go to the project root folder.
	- Run the command below.
		- ``` mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:8085" ```
	- OR
		- ``` java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8085 -jar target/batch_demo.jar ```
	- To use the Spring dev tools features please configure the -> ``` Spring Boot Remote ```
		- Remote URL -> ``` http://localhost:8080/ ```
		- Remote Secret -> ``` teste ```

14. if necessary install POSTMAN, follow the instructions in the link below.
	- ``` https://learning.postman.com/docs/getting-started/installation-and-updates/ ```


15. On the postman folder in the project source root, load the service schema on the Postman.
	- On the endpoint ``` http://localhost:8080/instrument ```
		- load the file ``` instruments.csv ```
	- On the endpoint ``` http://localhost:8080/customer ```
		- load the file ``` customer.csv ```
	- To retrieve the data loaded, use the endpoints below.
		- ``` http://localhost:8080/instrument ```
		- ``` http://localhost:8080/customer ```

19. To access the OpenAPI definition, please use the link below
	- ``` http://localhost:8080/swagger-ui/index.html ``` 