package com.company.movies.dto.impl;

import com.company.movies.dto.MovieDTO;


public class MovieCommentsDTO implements MovieDTO {



    private int movieId;

    private int movieCommentId;

    private String movieComment;

    private int userId;

    public MovieCommentsDTO(int movieId, int movieCommentId, String movieComment, int userId) {
        this.movieId = movieId;
        this.movieCommentId = movieCommentId;
        this.movieComment = movieComment;
        this.userId = userId;
    }


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieCommentId() {
        return movieCommentId;
    }

    public void setMovieCommentId(int movieCommentId) {
        this.movieCommentId = movieCommentId;
    }

    public String getMovieComment() {
        return movieComment;
    }

    public void setMovieComment(String movieComment) {
        this.movieComment = movieComment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
