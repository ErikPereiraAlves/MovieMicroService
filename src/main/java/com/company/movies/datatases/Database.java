package com.company.movies.datatases;

import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.company.movies.transferobjects.impl.UserCredentialsDTO;

import java.util.concurrent.ConcurrentHashMap;


public interface Database {

    public ConcurrentHashMap<Integer, UserCredentialsDTO> getUserData();

    public UserCredentialsDTO getUserData(String userName, String userPwd);

    public ConcurrentHashMap<Integer, MovieDetailsDTO> getMovieData();


}
