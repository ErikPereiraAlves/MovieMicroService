package com.company.movies.dao.impl;

import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class MovieDAOImplTest {

    private MovieDAOImpl dao;

    @Before
    public void Setup() throws Exception {

        dao = new MovieDAOImpl();
    }

    @Test
    public void shouldPersistMovieDetails() throws Exception {

        Collection<MovieDetailsDTO> collection = new ArrayList<>();
        Set<MovieCommentsDTO> movieComments = new LinkedHashSet<>();
        movieComments.add(new MovieCommentsDTO(100, 1, "foo", 2));
        movieComments.add(new MovieCommentsDTO(100, 2, "boo", 2));
        collection.add(new MovieDetailsDTO(1000, "Endless", "This movie has no end", movieComments));
        boolean success = dao.persistMovieDetails(collection);

        assertThat(success, is(true));

    }


    @Test
    public void shouldPersistMovieComments() throws Exception {

        Collection<MovieCommentsDTO> collection = new ArrayList<>();
        collection.add(new MovieCommentsDTO(1, 1, "Great movie - junit.", 1));
        boolean success = dao.persistMovieComments(collection);

        assertThat(success, is(true));

    }

    @Test
    public void shouldFetchMovieDetails() throws Exception {

        Set<MovieCommentsDTO> movieComments = new HashSet<>();
        movieComments.add(new MovieCommentsDTO(5, 1, "Great movie - junit.", 1));
        Collection<MovieDetailsDTO> collection = new ArrayList<>();
        collection.add(new MovieDetailsDTO(5, "junit", "blablabla", movieComments));

        //check movie comment in memory

    }

}