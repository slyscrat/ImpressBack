package com.slyscrat.impress.service.scheduled.movie;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:sources.properties")
public class MovieDBServiceImpl implements MovieDBService {

    @Value("${steam.api.games.list.url2}")
    private String key;

    @Override
    public void checkNewMovies() throws MovieDbException {
        TheMovieDbApi api = new TheMovieDbApi(key);
        // https://api.themoviedb.org/3/genre/movie/list?api_key=2dd3d9cff2342fedda1c98c621ec1544&language=ru
        //api.getGenreMovieList()
    }
}
