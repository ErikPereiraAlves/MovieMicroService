package com.company.movies.dto.impl;

import com.company.movies.dto.MovieDTO;

import java.util.Set;


public class MovieDetailsDTO implements MovieDTO {

    private int movieId;

    private String movieTitle;

    private String movieDescription;

    private Set<MovieCommentsDTO> movieComments;



    public MovieDetailsDTO(int movieId, String movieTitle, String movieDescription, Set<MovieCommentsDTO> movieComments) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieDescription = movieDescription;
        this.movieComments = movieComments;
    }

    public void update(String movieTitle, String movieDescription) {
        this.movieTitle = movieTitle;
        this.movieDescription = movieDescription;

    }

    public void addComment(MovieCommentsDTO movieComment){
        this.movieComments.add(movieComment);
    }


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public Set<MovieCommentsDTO> getMovieComments() {
        return movieComments;
    }

    public void setMovieComments(Set<MovieCommentsDTO> movieComments) {
        this.movieComments = movieComments;
    }
}
