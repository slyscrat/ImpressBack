package com.slyscrat.impress.service.scheduled.movie;

import com.omertron.themoviedbapi.MovieDbException;

public interface MovieDBService {
    void checkNewMovies() throws MovieDbException;
    void saveNewGenres() throws MovieDbException;
}
