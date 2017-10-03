package com.company.movies.datatases;

import com.company.movies.transferobjects.impl.MovieDetailsDTO;
import com.company.movies.transferobjects.impl.UserCredentialsDTO;

import java.util.concurrent.ConcurrentHashMap;


public interface Database {

    ConcurrentHashMap<Integer, UserCredentialsDTO> getUserData();

    UserCredentialsDTO getUserData(String userName, String userPwd);

    ConcurrentHashMap<Integer, MovieDetailsDTO> getMovieData();


}
