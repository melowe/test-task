# test-assessment

Installation requirements:

* JDK ver. 11 and above
* Gradle ver. 5 and above
* Docker (for the postgres container)

To run the service, execute the command `gradle run`.
Run the postgres container with the use of the Makefile.

In this template project, we prepared Spring Java and Scala templates.
To interact with HTTP APIs, we have set up [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client). 
To work with databases, we have set up Spring NamedParameterJdbcTemplate.
You can choose any other framework you prefer, but you have to prepare all corresponding assets prior to the beginning of the assessment.

During the online assessment, you will create a small service interacting with an external API, storing some data in a database and providing HTTP endpoints to interact with the service itself.
You will implement this service step by step during the online session. 
**Before** this session, you should set up your environment (IDE, docker, etc). You should also share your screen during the test assessment, so we can discuss and review your actions. 
