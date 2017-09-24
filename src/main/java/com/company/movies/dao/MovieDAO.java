package com.company.movies.dao;

import com.company.movies.exceptions.ServiceException;
import com.company.movies.transferobjects.impl.MovieCommentsDTO;
import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface MovieDAO {


    public boolean persistMovieDetails(Collection<MovieDetailsDTO> collection) throws ServiceException;

    public boolean persistMovieComments(Collection<MovieCommentsDTO> collection) throws ServiceException;

    public String fetchMoviesDetails() throws ServiceException;

    public String fetchMovieDetails(int movieId) throws ServiceException;
}
