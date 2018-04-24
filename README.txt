The completed coding task is pushed to GitHub:
https://github.com/greghawkins/toy-robot-simulator

Installation instructions:
1) Clone project: git clone https://github.com/greghawkins/toy-robot-simulator.git
2) Open project in IDE as maven project (this should work in IntelliJ / Eclipse)
3) IDE should be clever enough to import maven dependencies. If not, open up shell, go into project root folder and run: mvn clean install
5) Run main class: com.onestoptech.ToyRobotSimulatorApi
6) This will start project as Spring Boot app (with h2 database so no need for running db scripts or anything)
7) Outside of IDE, navigate to (and double-click on) this file: /toy-robot-simulator/toy-robot-simulator.html
8) This will open the (very simple) webapp in a browser
9) Should be obvious how to test the application through this web page

Testing done:
1) The main tests for the application are in: com.onstoptech.service.ToyRobotSimulatorServiceTest. This test uses Mockito and jUnit in combination to test all business logic (and mock repository calls). It should ensure the app works 100%.
2) I manually ran all of the example input commands listed in the 'Constraints' section of the spec through the web browser (to set this up, see above section). This further confirmed that the application was working to spec
3) The above step (2) was only tested in Chrome/Firefox (if issues with Internet Explorer then try with one of those)

Extras:
1) I did this as a basic web application (Spring boot / jquery / html / bootstrap), while meeting all criteria of the task, as it was an opportunity to show what I could do while staying true to spec
2) When the app is deployed you can also go here to see the API docs (via swagger): http://localhost:8080/api/swagger-ui.html
3) I have introduced basic validation on the client-side to make the user experience better and less confusing as commands are submitted

If I had more time:
1) Would have converted web project into Angular app
2) Would have introduced e2e tests with Selenium/Cucumber to simulate commands being run against the app (via web browser)
3) Would have introduced api tests to ensure back-end integration successful