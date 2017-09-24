package com.company.movies.utils;

import com.company.movies.exceptions.ServiceException;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;


public class JsonUtil {

    public static final Type MOVIE_DETAILS_COLLECTION_TYPE_TOKEN = new TypeToken<Collection<MovieDetailsDTO>>() {
    }.getType();

    public static final Type MOVIE_COMMENTS_COLLECTION_TYPE_TOKEN = new TypeToken<Collection<MovieCommentsDTO>>() {
    }.getType();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final Gson GSON = new Gson();

    public static String readJsonFromRequest(HttpServletRequest request) throws ServiceException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            LOGGER.error("Unable to read Json from Request!", e);
            throw new ServiceException("Error while parsing json from request, please check format. Error Message:" + e.getMessage());

        }
    }


    public static Collection<MovieDetailsDTO> deserializeMovieDetails(String jsonAsString) throws ServiceException {
        if (StringUtils.isNotBlank(jsonAsString)) {
            String trimmedJsonLogs = jsonAsString.trim();
            try {
                return GSON.fromJson(trimmedJsonLogs, MOVIE_DETAILS_COLLECTION_TYPE_TOKEN);
            } catch (Exception e) {
                LOGGER.error("Error while parsing movie detail(s) [{}]", trimmedJsonLogs, e);
                throw new ServiceException("Error while parsing json: " + trimmedJsonLogs + " Error Message:" + e.getMessage());
            }
        }
        return Collections.emptyList();
    }


    public static Collection<MovieCommentsDTO> deserializeMovieComments(String jsonAsString) throws ServiceException {
        if (StringUtils.isNotBlank(jsonAsString)) {
            String trimmedJsonLogs = jsonAsString.trim();
            try {
                return GSON.fromJson(trimmedJsonLogs, MOVIE_COMMENTS_COLLECTION_TYPE_TOKEN);
            } catch (Exception e) {
                LOGGER.error("Error while parsing comment(s) [{}]", trimmedJsonLogs, e);
                throw new ServiceException("Error while parsing json: " + trimmedJsonLogs + " Error Message:" + e.getMessage());
            }
        }
        return Collections.emptyList();
    }


}
