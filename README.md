# Zooplus Selenium Coding Challenge

This assignment is implemented to enhance the quality assurance process by automating test cases for a shopping cart application. Leveraging the powerful combination of Java, Selenium, and Cucumber, this project aims to streamline the testing process and ensure the reliability of the shopping cart functionality.

### Tech-stack:

- Java: The core programming language used for developing robust and maintainable test scripts.
- Selenium: A trusted and widely-used tool for browser automation, enabling precise control over web interactions.
- Cucumber: Chosen as the testing framework for its ability to bridge the gap between technical and non-technical stakeholders. Cucumber's natural language approach ensures that anyone interested in evaluating the test cases can do so with ease, regardless of their technical background.

## Getting started

1. Make sure to have Java 11 installed in your machine.
2. In the profile file of your use (i.e. ~/bashrc or ~/.zshrc), include the following environment variable and source it to the project. This way, the authenticated credentials are stored in your local and not shared.

```shell
echo "export AUTH_COOKIE=name-surname-test" >> ~/.bashrc
```

```shell
echo "export AUTH_COOKIE=name-surname-test" >> ~/.zshrc
```

3. Install dependencies by running:

```shell
mvn clean install
```

## Running the tests

There are two options to run these tests:

1. From the terminal, located in the root folder of this project, run the following command:

```shell
mvn clean test
```

2. From the runner, go to selenium-coding-challenge/test/java/qa/runners and execute the file CucumberRunnerTests from your IDE.

