package com.company.movies.utils;

import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.google.gson.Gson;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import static org.junit.Assert.assertTrue;


public class JsonUtilTest {

    private static final String detailsJson = "[{\"movieId\":111,\"movieTitle\":\"Fast and Ferious\",\"movieDescription\":\"Action movie\",\"movieComments\":[{\"movieId\":111,\"movieCommentId\":1,\"movieComment\":\"Great movie.\",\"userId\":1},{\"movieId\":111,\"movieCommentId\":2,\"movieComment\":\"I did not like it, it was too violent\",\"userId\":2},{\"movieId\":111,\"movieCommentId\":100,\"movieComment\":\"Great movie.\",\"userId\":1}]}]";
    private static final String commentsJson = "[{\"movieId\":111,\"movieCommentId\":100,\"movieComment\":\"Great movie.\",\"userId\":1}]";
    private static final Gson GSON = new Gson();
    private MockHttpServletRequest request;

    private static boolean isJSONValid(String jsonInString) {
        try {
            GSON.fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    @Before
    public void Setup() throws Exception {
        request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setMethod("POST");
    }

    @Test
    public void shouldPersistDetailsJsonFromRequest() throws Exception {
        SetupDetailsPersistRequest();
        Assert.assertTrue(isJSONValid(buildStringThatRepresentJsonFromPOSTPayload()));
    }

    @Test
    public void shouldPersistMessageJsonFromRequest() throws Exception {
        SetupCommentsPersistRequest();
        Assert.assertTrue(isJSONValid(buildStringThatRepresentJsonFromPOSTPayload()));
    }

    @Test
    public void shouldCorrectlyDeserializeMovieDetails() throws Exception {


        Collection<MovieDetailsDTO> collection = JsonUtil.deserializeMovieDetails(detailsJson);
        assertTrue(null != collection && collection.size() > 0);

    }

    @Test
    public void shouldCorrectlyDeserializeMovieComments() throws Exception {
        Collection<MovieCommentsDTO> collection = JsonUtil.deserializeMovieComments(commentsJson);
        assertTrue(null != collection && collection.size() > 0);
    }

    private void SetupDetailsPersistRequest() throws Exception {
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

    private void SetupCommentsPersistRequest() throws Exception {
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