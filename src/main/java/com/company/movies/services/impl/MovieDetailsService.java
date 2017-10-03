package com.company.movies.services.impl;

import com.company.movies.dao.MovieDAO;
import com.company.movies.exceptions.ServiceException;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.company.movies.transferobjects.impl.UserCredentialsDTO;
import com.company.movies.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.Future;

@Component
public class MovieDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDetailsService.class);

    @Autowired
    private MovieDAO movieDao;

    public String retrieveAllMovies(UserCredentialsDTO user) {

        try {
            Future<String> future = fetchAllMovies();
            return future.get();
        } catch (Exception e) {
            LOGGER.error("Unable to fetch movie details for User ID={} ", user.getUserId(), e);
            return "Unable to fetch movie details";
        }

    }

    public String retrieveSingleMovie(UserCredentialsDTO user, int movieId) {

        try {
            Future<String> future = fetchMovie(movieId);
            return future.get();
        } catch (Exception e) {
            LOGGER.error("Unable to fetch movie details for User ID={} ", user.getUserId(), e);
            return "Unable to fetch movie details";
        }

    }

    public HttpStatus persistMovies(String jsonAsString, UserCredentialsDTO user) throws ServiceException {


        Collection<MovieDetailsDTO> collection = JsonUtil.deserializeMovieDetails(jsonAsString);


        if (collection.size() == 0) {
            LOGGER.error("Invalid Json. Unable to deserialize: User ID={}, json(s) received={}", user.getUserId(), jsonAsString);
            return HttpStatus.BAD_REQUEST;
        }

        try {
            Future<HttpStatus> future = save(collection);
            return future.get();
        } catch (Exception e) {
            LOGGER.error("Unable to fetch movie details for for:Log(s) received={} ", jsonAsString, e);
            return HttpStatus.BAD_REQUEST;
        }

    }


    @Async
    private Future<HttpStatus> save(Collection<MovieDetailsDTO> collection) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try {
            boolean success = movieDao.persistMovieDetails(collection);
            if (!success) {
                LOGGER.error("Not all data might have been persisted. for: Log(s) received={}", collection);
            }
            status = HttpStatus.OK;
        } catch (ServiceException e) {
            LOGGER.error("Error occurred while persisting movie details for: Log(s) received={}", collection, e);
        }
        return new AsyncResult<>(status);

    }

    @Async
    private Future<String> fetchMovie(int movieId) {

        String movieDetails = null;
        try {
            movieDetails = movieDao.fetchMovieDetails(movieId);

        } catch (ServiceException e) {
            LOGGER.error("Error occurred while fetching movie details for: Log(s) received={}", e);
        }
        return new AsyncResult<>(movieDetails);

    }

    @Async
    private Future<String> fetchAllMovies() {

        String movieDetails = null;
        try {
            movieDetails = movieDao.fetchMoviesDetails();

        } catch (ServiceException e) {
            LOGGER.error("Error occurred while fetching movie details for: Log(s) received={}", e);
        }
        return new AsyncResult<>(movieDetails);

    }


}
