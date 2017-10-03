package com.company.movies.dao;

import com.company.movies.exceptions.ServiceException;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface MovieDAO {


    boolean persistMovieDetails(Collection<MovieDetailsDTO> collection) throws ServiceException;

    boolean persistMovieComments(Collection<MovieCommentsDTO> collection) throws ServiceException;

    String fetchMoviesDetails() throws ServiceException;

    String fetchMovieDetails(int movieId) throws ServiceException;
}
