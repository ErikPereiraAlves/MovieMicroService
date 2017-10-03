package com.company.movies.dao.impl;

import com.company.movies.dao.MovieDAO;
import com.company.movies.datatases.impl.InMemoryDatabase;
import com.company.movies.exceptions.ServiceException;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.company.movies.utils.LFUCache;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MovieDAOImpl implements MovieDAO {


    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDAOImpl.class);
    private boolean movieDetailsPersistSuccess = false;
    private int countCommentsPersisted;
    private int countDetailsPersisted;
    private String json;

    @Override
    public boolean persistMovieDetails(Collection<MovieDetailsDTO> collection) throws ServiceException {

        /* accessing our mock database */
        ConcurrentHashMap<Integer, MovieDetailsDTO> moviesMap = InMemoryDatabase.getInstance().getMovieData();
        countDetailsPersisted = 0;

        collection.forEach(
                (obj) -> {
                    if (moviesMap.containsKey(obj.getMovieId())) {
                        MovieDetailsDTO dto = moviesMap.get(obj.getMovieId());
                        dto.update(obj.getMovieTitle(), obj.getMovieDescription());
                        countDetailsPersisted++;
                    } else {
                        moviesMap.put(obj.getMovieId(), obj);
                        countDetailsPersisted++;
                        updateLFUCache(obj.getMovieId(), obj);
                    }
                }
        );

        return countDetailsPersisted == collection.size();
    }

    @Override
    public boolean persistMovieComments(Collection<MovieCommentsDTO> collection) throws ServiceException {

        /* accessing our mock database */
        ConcurrentHashMap<Integer, MovieDetailsDTO> moviesMap = InMemoryDatabase.getInstance().getMovieData();
        countCommentsPersisted = 0;

        collection.forEach(
                (obj) -> {
                    if (moviesMap.containsKey(obj.getMovieId())) {
                        MovieDetailsDTO dto = moviesMap.get(obj.getMovieId());
                        dto.addComment(obj);
                        countCommentsPersisted++;
                    }

                }
        );

        return countCommentsPersisted == collection.size();

    }

    @Override
    public String fetchMovieDetails(int movieId) throws ServiceException {

        MovieDetailsDTO movie = tryGetFromLFUCache(movieId);

        if (null == movie) {
            movie = InMemoryDatabase.getInstance().getMovieData(movieId);

            if (null != movie) {
                updateLFUCache(movie.getMovieId(), movie);
                return new Gson().toJson(movie);
            } else {
                return null;
            }
        } else {
            return new Gson().toJson(movie);
        }


    }

    @Override
    public String fetchMoviesDetails() throws ServiceException {

        ConcurrentHashMap<Integer, MovieDetailsDTO> moviesMap = InMemoryDatabase.getInstance().getMovieData();

        if (null != moviesMap) {
            return new Gson().toJson(moviesMap);
        } else {
            return null;
        }


    }


    private MovieDetailsDTO tryGetFromLFUCache(int movieId) {

        return LFUCache.getInstance().getCacheEntry(movieId);
    }

    private void updateLFUCache(int key, MovieDetailsDTO movie) {

        LFUCache.getInstance().addCacheEntry(key, movie);

    }

}