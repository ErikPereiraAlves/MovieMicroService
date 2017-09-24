package com.company.movies;

import com.company.movies.exceptions.ServiceException;
import com.company.movies.services.impl.MovieCommentsService;
import com.company.movies.services.impl.MovieDetailsService;
import com.company.movies.transferobjects.impl.UserCredentialsDTO;
import com.company.movies.utils.JsonUtil;
import com.company.movies.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Base64;

import static com.company.movies.utils.ServicesUtil.*;
import static com.company.movies.utils.ServicesUtil.ServicesAvailable.COMMENTS_PERSIST;

@RestController
@RequestMapping(value = BASE_URI + VERSION_URI)
public class ServiceController {


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);
    @Autowired
    private MovieCommentsService movieCommentsService;
    @Autowired
    private MovieDetailsService movieDetailsService;

    /*
     Service calls.
     */

    @RequestMapping(value = FETCH + DETAILS_URI, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> fetchMovieDetails(final HttpServletRequest request) {

        String movieId = request.getParameter("id");
        if (null == movieId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Movie id was not found in parameters.");
        }

        return call(ServicesAvailable.DETAILS_FETCH, request, movieId);

    }


    @RequestMapping(value = PERSIST + DETAILS_URI, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> persistMovieDetails(final HttpServletRequest request) {

        return call(ServicesAvailable.DETAILS_PERSIST, request, null);

    }

    @RequestMapping(value = PERSIST + COMMENTS_URI, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> persistMovieComments(final HttpServletRequest request) {

        return call(COMMENTS_PERSIST, request, null);

    }


    private ResponseEntity<Object> call(ServicesAvailable serviceName, HttpServletRequest request, String movieId) {

        String[] basicAuth = getBasicAuthData(request);

        if (null == basicAuth || basicAuth.length != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User credentials are not found, please check user Id and/or password.");
        }

        final String loggedInUser = basicAuth[0].trim();
        final String loggedInUserPwd = basicAuth[1].trim();

        ResponseEntity<Object> responseEntity;

        if (UserUtil.userCredentialsMissing(loggedInUser, loggedInUserPwd)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User credentials are not found.");
        }

        UserCredentialsDTO user = UserUtil.validateUserCredentials(loggedInUser, loggedInUserPwd);

        if (null == user) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User [" + loggedInUser + "] credentials are not authenticated, please check.");
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String result;
        switch (serviceName) {
            case ALL_DETAILS_FETCH:

                result = movieDetailsService.retrieveAllMovies(user);
                if (null != result) {
                    responseEntity = ResponseEntity.status(HttpStatus.OK).body(result);
                } else {
                    responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movies database not found.");
                }

                break;

            case DETAILS_FETCH:
                String movieJson = movieDetailsService.retrieveSingleMovie(user, Integer.parseInt(movieId));
                if (null != movieJson) {
                    responseEntity = ResponseEntity.status(HttpStatus.OK).body(movieJson);
                } else {
                    responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with following id" + movieId + " was not found in the system.");
                }

                break;

            case DETAILS_PERSIST:

                if (user.getRole().equals(UserUtil.UserRoles.ADMIN)) {
                    try {
                        status = movieDetailsService.persistMovies(JsonUtil.readJsonFromRequest(request), user);
                        if (status == HttpStatus.OK) {
                            responseEntity = ResponseEntity.status(HttpStatus.OK).body("Movie(s) persisted successfully.");
                        } else {
                            responseEntity = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("One of more movies failed to be persisted.");
                        }
                    } catch (ServiceException e) {
                        responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                    }

                } else {
                    responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN users have authority to persist data.");
                }

                break;

            case COMMENTS_PERSIST:

                if (user.getRole().equals(UserUtil.UserRoles.ADMIN)) {
                    try {
                        status = movieCommentsService.persistComments(JsonUtil.readJsonFromRequest(request), user);

                        if (status == HttpStatus.OK) {
                            responseEntity = ResponseEntity.status(HttpStatus.OK).body("All comments inserted successfully");
                        } else {
                            responseEntity = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("One of more comments failed to be persisted.");
                        }

                    } catch (ServiceException e) {
                        responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

                    }
                } else {
                    responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN users have authority to persist data.");
                }


                break;

            default:
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service requested was not found.");

        }

        return responseEntity;
    }

    private String[] getBasicAuthData(HttpServletRequest request) {

        String[] values = null;
        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            values = credentials.split(":", 2);
        }

        return values;
    }
}