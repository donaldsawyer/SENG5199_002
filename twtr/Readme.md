#twtr - The New Twitter

twtr is a case study for learning how to do web programming using Grails/Groovy for SENG5199-002 Web Programming.

# REST Services
*twtr* exposes RESTful web services that can be used for actions to be taken in the system.

## Account Actions
Base URL: `/accounts/`

### Get Account(s)
Gets the details of an account or multiple accounts.  The values returned do not include the accounts that follow or are following the account(s) returned, rather the count of number of followers (followerCount) and number of accounts the account is following (followingCount).  The actual tweets posted by the user are not returned, rather the number of tweets sent (messageCount).

1. GET /accounts
2. GET /accounts/$id

#### Query Parameters
**max**: limits the number of records to return (default = 10)

**offset**: skips a number of records

#### Body
None

#### Samples
    GET /accounts
    GET /accounts?max=2
    GET /accounts?offset=5
    GET /accounts?max=5&offset=5    
    GET /accounts/1

#### Returned Data
Returning a single account returns the data for the account in individual fields.  Getting accounts without using the $id parameter will return each account as a separate item in a collection.  For each account returned, the following is returned.

1. id
2. handle
3. emailAddress
4. displayName
5. password
6. followerCount
7. followingCount
8. messageCount

### Add Account
Creates a new account with the values posted in the JSON body.  If the account parameters are not valid, a 422 error response is returned.

1. POST /accounts

#### Query Parameters
None

#### Body
**Required Fields**

1. handle
2. emailAddress
3. displayName
4. password

#### Returned Data
The created account is returned with the same format as used if `GET /accounts/$id` is used.

#### Samples
    POST /accounts
    {
      handle: "myHandle",
      emailAddress: "myHandle@something.com",
      displayName: "My Name",
      password: "abc123ABC"
    }

### Delete Account

### Update Account

## Tweeting / Messaging

## Followship & Feed

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

# Assignment 2 - REST + Functional Testing
* Use Grails Controllers to add HTTP request handling to twtr application
* Implement querying of data using GORM
* Implement functional tests that issue HTTP requests to your running server to verify requirements

## Account Requirements


## Message Requirements


## Follow Requirements