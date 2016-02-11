#twtr - The New Twitter

twtr is a case study for learning how to do web programming using Grails/Groovy for SENG5199-002 Web Programming.

# Assignment 1
Create an application that works like a simplified Twitter. The initial requirements will include storage of data to the model required to support accounts, messages, and account following.

Messages are posted by accounts. An account can follow another account.

**Due Date: 2/12/2016**

##Assignment Overview
- Implement a social media platform similar to Twitter
- Implement the domain model and persistence of the model using Grails Domain classes
- Write Grails unit and integration tests to verify that you've implemented the requirements

##Requirements

###Account Requirements

> A1. Saving an account with a valid handle, email, password and name will succeed (unit test)
> 
> A2. Saving an account missing any of the required values of handle email, password and name will fail (data-driven unit test)
> 
> A3. Saving an account with an invalid password will fail. Passwords must be 8-16 characters and have at least 1 number, at least one lower-case letter, at least 1 upper-case letter (data-driven unit test)
> 
> A4. Saving account with a non-unique email or handle address must fail (integration test)


###Message Requirements 
> M1. Saving a message with a valid account and message text will succeed (unit test)
> 
> M2. Message text is required to be non-blank and 40 characters or less (data-driven unit test)

###Follow Requirements
You may choose to implement the follow functionality how you choose. Prove that you can save data with your domain model that supports the following requirements:

> F1. An account may have multiple followers (integration test)
> 
> F2. Two accounts may follow each other (integration test)

##Testing
`grails test-app` will run all of the tests.  (use `grails test-app -clean` to remove previous test runs.)

`grails test-app -unit` runs all of the unit tests.

`grails test-app -integration` runs all of the integration tests.