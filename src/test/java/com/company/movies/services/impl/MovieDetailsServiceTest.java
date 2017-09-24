package com.company.movies.services.impl;

import com.company.movies.dao.impl.MovieDAOImpl;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class MovieDetailsServiceTest {

    private MovieDAOImpl dao;

    @Before
    public void Setup() throws Exception {

        dao = new MovieDAOImpl();
    }

    @Test
    public void retrieveSingleMovie() throws Exception {

        Collection<MovieDetailsDTO> collection = new ArrayList<>();
        Set<MovieCommentsDTO> movieComments = new LinkedHashSet<>();
        movieComments.add(new MovieCommentsDTO(100, 1, "foo", 2));
        movieComments.add(new MovieCommentsDTO(100, 2, "boo", 2));
        collection.add(new MovieDetailsDTO(1000, "Endless", "This movie has no end", movieComments));
        boolean success = dao.persistMovieDetails(collection);

        assertThat(success, is(true));

    }

    @Test
    public void persistMovies() throws Exception {

        Collection<MovieDetailsDTO> collection = new ArrayList<>();
        Set<MovieCommentsDTO> movieComments = new LinkedHashSet<>();
        movieComments.add(new MovieCommentsDTO(100, 1, "foo", 2));
        movieComments.add(new MovieCommentsDTO(100, 2, "boo", 2));
        collection.add(new MovieDetailsDTO(1000, "Endless", "This movie has no end", movieComments));
        collection.add(new MovieDetailsDTO(2000, "Endless", "This movie has no end", movieComments));
        collection.add(new MovieDetailsDTO(3000, "Endless", "This movie has no end", movieComments));
        boolean success = dao.persistMovieDetails(collection);

        assertThat(success, is(true));

    }

}