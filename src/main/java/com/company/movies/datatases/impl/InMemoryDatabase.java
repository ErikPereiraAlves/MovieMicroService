package com.company.movies.datatases.impl;

import com.company.movies.datatases.Database;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.company.movies.transferobjects.impl.UserCredentialsDTO;
import com.company.movies.utils.LFUCache;
import com.company.movies.utils.UserUtil;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
    This works as , mocks, a database. Each ConcurrentHashMap would be a DB table.
 */
public class InMemoryDatabase implements Database {


    /* Single instance of the database */
    private static InMemoryDatabase instance = null;
    /* Movie table*/
    private ConcurrentHashMap<Integer, MovieDetailsDTO> movieData;
    /* User table */
    private ConcurrentHashMap<Integer, UserCredentialsDTO> userData;

    protected InMemoryDatabase() {

    }

    /* I am a singleton */
    public static InMemoryDatabase getInstance() {
        if (instance == null) {
            instance = new InMemoryDatabase();
            instance.initDatabase();
        }
        return instance;
    }


    public ConcurrentHashMap<Integer, MovieDetailsDTO> getMovieData() {
        return movieData;
    }

    public MovieDetailsDTO getMovieData(int movieId) {

        Optional<MovieDetailsDTO> movieObject = movieData.entrySet()
                                                         .stream()
                                                         .filter(e -> e.getValue().getMovieId() == movieId)
                                                         .map(Map.Entry::getValue)
                                                         .findFirst();


        if (!movieObject.isPresent()) {
            return null;
        } else {
            return movieObject.get();
        }

    }

    public ConcurrentHashMap<Integer, UserCredentialsDTO> getUserData() {


        return userData;
    }

    public UserCredentialsDTO getUserData(String userName, String userPwd) {

        Optional<UserCredentialsDTO> userObject = userData.entrySet()
                                                          .stream()
                                                          .filter((e) -> (e.getValue().getUserName().equals(userName) && e.getValue().getUserPassword().equals(userPwd)))
                                                          .map(Map.Entry::getValue)
                                                          .findFirst();


        if (!userObject.isPresent()) {
            return null;
        } else {
            return userObject.get();
        }
    }

    /* mock to initialize movie and user data  */
    private void initDatabase() {


        if (null == userData) {
            userData = new ConcurrentHashMap<>();
            userData.put(1, new UserCredentialsDTO("Jose", 1, "123abc", UserUtil.UserRoles.USER));
            userData.put(2, new UserCredentialsDTO("Maria", 2, "xyz321", UserUtil.UserRoles.ADMIN));
        }

        if (null == movieData) {

            /* init LFU for caching movies */
            LFUCache instance = LFUCache.getInstance();

            movieData = new ConcurrentHashMap<>();
            Set<MovieCommentsDTO> movieComments = new LinkedHashSet<>();
            movieComments.add(new MovieCommentsDTO(1, 1, "Great movie.", 1));
            movieComments.add(new MovieCommentsDTO(1, 2, "I didn't like it, it was too violent", 2));
            MovieDetailsDTO movie = new MovieDetailsDTO(1, "Fast and Ferious", "Action movie", movieComments);
            movieData.put(1, movie);
            instance.addCacheEntry(1, movie);

            movieComments = new LinkedHashSet<>();
            movieComments.add(new MovieCommentsDTO(2, 1, "Not my type of movie. I like action.", 1));
            movieComments.add(new MovieCommentsDTO(2, 2, "I loved it, cried so much...", 2));
            movie = new MovieDetailsDTO(2, "Gone with the wind", "Drama movie", movieComments);
            movieData.put(2, movie);
            instance.addCacheEntry(2, movie);
        }


    }
}