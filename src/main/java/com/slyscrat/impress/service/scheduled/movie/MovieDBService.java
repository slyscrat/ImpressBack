package com.slyscrat.impress.service.scheduled.movie;

import com.omertron.themoviedbapi.MovieDbException;

// TODO : getGenres + getList of info
public interface MovieDBService {
    void checkNewMovies() throws MovieDbException;
}
