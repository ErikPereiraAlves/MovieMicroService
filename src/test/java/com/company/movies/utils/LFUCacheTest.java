package com.company.movies.utils;

import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;


public class LFUCacheTest {

    private LFUCache cache;
    private MovieDetailsDTO movie;

    @Before
    public void Setup() throws Exception {

        cache = LFUCache.getInstance();

        Set<MovieCommentsDTO> movieComments = new LinkedHashSet<>();
        movieComments.add(new MovieCommentsDTO(100, 1, "foo", 2));
        movieComments.add(new MovieCommentsDTO(100, 2, "boo", 2));
        movie = new MovieDetailsDTO(100, "Junit - the movie", "Should i stay or should i go", movieComments);

        cache.addCacheEntry(1, movie);
    }

    @Test
    public void shouldGetCacheEntry() throws Exception {

        MovieDetailsDTO movieFromCache = cache.getCacheEntry(1);
        Assert.assertThat(movieFromCache.getMovieId(), is(100));

    }


    @Test
    public void shouldGetLFUKey() throws Exception {

        int key = cache.getLFUKey();
        Assert.assertTrue(key != 0);
    }

    @Test
    public void shouldNotBeIsFull() throws Exception {

        boolean isFull = LFUCache.isFull();
        Assert.assertThat(isFull, is(false));

    }

    @Test
    public void shouldBeIsFull() throws Exception {

        cache.addCacheEntry(5, movie);
        cache.addCacheEntry(10, movie);
        cache.addCacheEntry(100, movie);
        cache.addCacheEntry(1000, movie);
        cache.addCacheEntry(10000, movie);
        cache.addCacheEntry(100000, movie);
        cache.addCacheEntry(1000000, movie);
        cache.addCacheEntry(10000000, movie);
        cache.addCacheEntry(100000000, movie);
        cache.addCacheEntry(2, movie);

        boolean isFull = LFUCache.isFull();
        Assert.assertThat(isFull, is(true));

    }

}