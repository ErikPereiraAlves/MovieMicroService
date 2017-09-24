package com.company.movies.datatases.impl;

import com.company.movies.dao.impl.MovieDAOImpl;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.company.movies.transferobjects.impl.UserCredentialsDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class InMemoryDatabaseTest {

    private MovieDAOImpl dao;

    @Before
    public void Setup() throws Exception {

        dao = new MovieDAOImpl();
    }


    @Test
    public void getMovieData() throws Exception {

        Collection<MovieDetailsDTO> collection = new ArrayList<>();
        Set<MovieCommentsDTO> movieComments = new LinkedHashSet<>();
        movieComments.add(new MovieCommentsDTO(100, 1, "foo", 2));
        movieComments.add(new MovieCommentsDTO(100, 2, "boo", 2));
        collection.add(new MovieDetailsDTO(1000, "Endless", "This movie has no end", movieComments));
        boolean success = dao.persistMovieDetails(collection);

        assertThat(success, is(true));

        MovieDetailsDTO movie = database.getMovieData(1000);

        assertThat(movie.getMovieId(), is(1000));

    }

    InMemoryDatabase database = InMemoryDatabase.getInstance();

    @Test
    public void shouldGetUserData() throws Exception {

        ConcurrentHashMap<Integer, UserCredentialsDTO> users = database.getUserData();

        Assert.assertTrue(users.size() > 0);

    }

    @Test
    public void shouldGetMovieData() throws Exception {

        ConcurrentHashMap<Integer, MovieDetailsDTO> movies = database.getMovieData();

        Assert.assertTrue(movies.size() > 0);


    }

}