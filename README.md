# Problem

Your task is to implement RESTful facade backend API with two mock up services:
-          Movie details service (movie: id, title, description)
-          Movie comments service (comment: movieId, username, message)

Movie details service and Movie comments service are simple standalone services providing mock up data. You can use any mock server library.
Requirements for the facade:
-          Endpoint for providing combined movie details and movie comments by movie id (in JSON)
-          Dummy endpoint for creating movie details. No communication with Movie details service. Endpoint should be authorized with ROLE_ADMIN
-          Dummy endpoint for creating Comment. No communication with Comments service. Endpoint should be authorized with ROLE_USER
-          Basic authentication
-          No user registration required. You can store mocked user data in memory.
-          Calls to the backend services should be implemented asynchronously
-          Movie details service responses should be cached with Least Frequently Used strategy
-          Comments service responses should also be cached but only as fallback. When the backend service is down, comments should be taken from cache.
-          Implemented with JAVA 8
-          Should start as a normal java application with embedded servlet container

Things that will be evaluated:
-   Implementation
-   REST principles, proper status codes, headers, error responses
-   Tests
-   Clean code
-   Commits

You can use any open source library or framework you wish.
Please create an open repository on github and send us the link with your final implementation.



# Solution

Microservice implemented using Spring boot, java 8 and Maven.

## How to test it:

* One may use postman for testing it. Microservice uses Using POST. GET has not been implemented. Request uses JSON.  Responses either returns JSON or HTTP status code (for persist data calls)



### Retrieve an specific movie by id (along with its comments):

Post url:  http://localhost:8080/movie/v1/fetch/details?id=1

Basic authorization: There are 2 users created in the microservice:  Jose (password of 123abc) which is a regular user, AND Maria (password of  xyz321) which has the role of ADMIN.



### Persist a new movie:

Post url: Post url:  http://localhost:8080/movie/v1/persist/details
Content-Type : application/json
body (raw): Place the json one wants to persist.

Basic authorization: There are 2 users created in the microservice:  Jose (password of 123abc) which is a regular user (can't persist data), AND Maria (password of  xyz321) which has the role of ADMIN with persist authority.

Json example: Movie with ID =3 and comments by user Jose and Maria.

[{"movieId":3,"movieTitle":"Matrix","movieDescription":"Action movie","movieComments":[{"movieId":3,"movieCommentId":1,"movieComment":"Futuristic movie","userId":1},{"movieId":3,"movieCommentId":2,"movieComment":"I liked it.","userId":2}]}]

### Persist a movie's comments:

Post url: Post url:  http://localhost:8080/movie/v1/persist/comments
Content-Type : application/json
body (raw): Place the json one wants to persist.

Basic authorization: There are 2 users created in the microservice:  Jose (password of 123abc) which is a regular user (can't persist data), AND Maria (password of  xyz321) which has the role of ADMIN with persist authority.

Json example: Movie with ID =1 and comments by user Maria.

[{"movieId":1,"movieCommentId":1,"movieComment":"Great movie.","userId":2}]
"# MovieStoreMicroService" 
