package com.company.movies.services.impl;

import com.company.movies.dao.impl.MovieDAOImpl;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class MovieCommentsServiceTest {


    private MovieDAOImpl dao;

    @Before
    public void Setup() throws Exception {

        dao = new MovieDAOImpl();
    }


    @Test
    public void persistComments() throws Exception {

        Collection<MovieCommentsDTO> collection = new ArrayList<>();
        collection.add(new MovieCommentsDTO(1, 1, "Great movie - junit.", 1));
        boolean success = dao.persistMovieComments(collection);

        assertThat(success, is(true));

    }

}