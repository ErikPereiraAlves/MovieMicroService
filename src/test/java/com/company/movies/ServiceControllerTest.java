package com.company.movies;

import com.company.movies.dao.MovieDAO;
import com.company.movies.dao.impl.MovieDAOImpl;
import com.company.movies.services.impl.MovieCommentsService;
import com.company.movies.services.impl.MovieDetailsService;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.company.movies.transferobjects.impl.UserCredentialsDTO;
import com.company.movies.utils.JsonUtil;
import com.company.movies.utils.LFUCache;
import com.company.movies.utils.UserUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {

    private static final String detailsJson = "[{\"movieId\":100,\"movieTitle\":\"Fast and Ferious\",\"movieDescription\":\"Action movie\",\"movieComments\":[{\"movieId\":100,\"movieCommentId\":1,\"movieComment\":\"Great movie.\",\"userId\":1},{\"movieId\":100,\"movieCommentId\":2,\"movieComment\":\"I did not like it, it was too violent\",\"userId\":2},{\"movieId\":100,\"movieCommentId\":100,\"movieComment\":\"Great movie.\",\"userId\":1}]}]";
    private static final String commentsJson = "[{\"movieId\":100,\"movieCommentId\":100,\"movieComment\":\"Great movie.\",\"userId\":1}]";
    private MockHttpServletRequest request;
    private LFUCache cache;
    private UserCredentialsDTO user;
    private MovieDAO movieDao;

    @Before
    public void Setup() throws Exception {
        request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setMethod("POST");
        cache = LFUCache.getInstance();
        user = new UserCredentialsDTO("Mario", 5, "abc", UserUtil.UserRoles.ADMIN);
        MovieDetailsService movieDetailsService = new MovieDetailsService();
        MovieCommentsService movieCommentsService = new MovieCommentsService();
        movieDao = new MovieDAOImpl();
    }

    @Test
    public void shouldFetchMovieDetails() throws Exception {

        SetupDetailsFetchRequest();

        String jsonString = buildStringThatRepresentJsonFromPOSTPayload();
        assertThat(jsonString.trim(), is(detailsJson));


    }

    @Test
    public void shouldPersistMovieDetails() throws Exception {

        SetupDetailsPersistRequest();

        String jsonAsString = buildStringThatRepresentJsonFromPOSTPayload();
        Collection<MovieDetailsDTO> collection = JsonUtil.deserializeMovieDetails(jsonAsString);
        boolean success = movieDao.persistMovieDetails(collection);
        assertThat(success, is(true));

        MovieDetailsDTO movie = cache.getCacheEntry(100);

        assertThat(movie.getMovieTitle(), is("Fast and Ferious"));

    }

    @Test
    public void shouldPersistMovieComments() throws Exception {

        SetupCommentsPersistRequest();

        String jsonAsString = buildStringThatRepresentJsonFromPOSTPayload();

        Collection<MovieCommentsDTO> collection = JsonUtil.deserializeMovieComments(jsonAsString);

        boolean success = movieDao.persistMovieComments(collection);

        assertThat(success, is(true));

        MovieDetailsDTO movie = cache.getCacheEntry(100);

        Set<MovieCommentsDTO> comments = movie.getMovieComments();

        assertThat(comments.isEmpty(), is(false));

    }

    public void SetupDetailsFetchRequest() throws Exception {
        request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setMethod("POST");
        request.setRequestURI("/movie/v1/fetch/details?id=1");
        try {
            request.setContent((detailsJson)
                    .getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void SetupDetailsPersistRequest() throws Exception {
        request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setMethod("POST");
        request.setRequestURI("/movie/v1/persist/details");
        try {
            request.setContent((detailsJson)
                    .getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void SetupCommentsPersistRequest() throws Exception {
        request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setMethod("POST");
        request.setRequestURI("/movie/v1/persist/comments");
        try {
            request.setContent((commentsJson)
                    .getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String buildStringThatRepresentJsonFromPOSTPayload() {
        String jsonString = "";
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            jsonString = sb.toString();
        } catch (IOException ignored) {
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return jsonString;
    }


}