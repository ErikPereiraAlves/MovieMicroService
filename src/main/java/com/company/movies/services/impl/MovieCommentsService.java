package com.company.movies.services.impl;

import com.company.movies.dao.MovieDAO;
import com.company.movies.exceptions.ServiceException;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
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
public class MovieCommentsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieCommentsService.class);

    @Autowired
    private MovieDAO movieDao;


    public HttpStatus persistComments(String jsonAsString, UserCredentialsDTO user) throws ServiceException {


        Collection<MovieCommentsDTO> collection = JsonUtil.deserializeMovieComments(jsonAsString);

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
    public Future<HttpStatus> save(Collection<MovieCommentsDTO> collection) {
        HttpStatus status = HttpStatus.OK;

        try {
            boolean success = movieDao.persistMovieComments(collection);
            if (!success) {
                LOGGER.error("Not all data might have been persisted. for: Log(s) received={}", collection);
                status = HttpStatus.OK;
            }
        } catch (ServiceException e) {
            LOGGER.error("Error occurred while persisting movie comments for: Log(s) received={}", collection, e);
        }
        return new AsyncResult<>(status);

    }

}
