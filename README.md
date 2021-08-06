# FamilyMapServer
A server that I built from scratch using skills I learned in CS 240.
This program starts a server on port 8080 and can handle various commands:

## From the Database-
`/clear` This API will clear ALL data from the database, including users and all generated data. This API can be run from a browser by simply typing it in the address bar or by clicking this link followed by pressing the Submit button below. No authorization authtoken is required.

`/load` This API will load the server's database with data provided by json text in the response body. The json text must contain an array of users as defined in the register details in addition to a personID, an array of persons, and an array of events. WARNING: all data from the database is wiped when this is called. The json file will be specified in the body of the request. A example.json file is provided to get you started with loading specific data. No authorization authtoken is required.

`/fill/[username]` This API will fill the server's database with fake data for the specified user name. The "username" parameter is required and must be a user already registered with the server. It can be any user name you choose. If there is any data in the database associated with the given user name, it is erased. This API can be run from a browser by simply typing it in the address bar (or by pressing the link, filling in the details, and clicking submit). The base person generated should be a person representing the user. No authorization authtoken is required.

`/fill/[username]/{generations}` This API will fill the server's database with fake data for the specified user name. The "username" parameter is required and must be a user already registered with the server. All the ancestor data associated with this user is erased. This API can be run from a browser by simply typing it in the address bar (or by pressing the link, filling in the details, and clicking submit). The base person generated should be a person representing the user. No authorization authtoken is required.

## From the User-
`/user/login` Use this to log in a user. A request body must be supplied specifying the username and password. If login succeeds, an authorization authtoken will be returned. Use this authtoken on other API calls that require authorization. The returned JSON object contains "Authorization" (the authorization authtoken) and "username" (the username that was just logged in). No authorization authtoken is required.

`/user/register` Use this to register a user. An authorization authtoken is returned. Use it just as you would a authtoken from login. Returns the same Json object as log in. It should be noted that when you register a user the database will automatically be filled.(Meaning you do not need to call the /fill API noted above). No authorization authtoken is required.

`/event` This API will return ALL events for ALL family members of the current user. The current user is determined from the provided authorization authtoken (which is required for this call). The returned JSON object contains "data" which is an array of event objects. Authorization authtoken is required.

`/event/[eventID]` This API will return the single event with the specified ID. The event must belong to a relative of the user associated with the authorization authtoken. The returned JSON contains the requested event object. Authorization authtoken is required.

`/person` This API will return ALL people associated with the current user. The current user is determined from the provided a uthorization authtoken (which is required for this call). The returned JSON object contains "data" which is an array of person objects. Authorization authtoken is required.

`/person/[personID]` This API will return the single person with the specified ID. The person must be related to the user associated with the authorization authtoken. The returned JSON contains the requested person object. Authorization authtoken is required.

## Future Applications

This Server will eventually be fully integrated into the Android app I am programming for my final project.
