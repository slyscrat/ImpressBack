package com.slyscrat.impress.service.scheduled.movie;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.credits.MediaCreditCast;
import com.omertron.themoviedbapi.model.credits.MediaCreditCrew;
import com.omertron.themoviedbapi.model.media.MediaCreditList;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.slyscrat.impress.model.dto.movie.CrewDto;
import com.slyscrat.impress.model.dto.movie.MovieCrewDto;
import com.slyscrat.impress.model.dto.movie.MovieDto;
import com.slyscrat.impress.model.dto.movie.MovieGenreDto;
import com.slyscrat.impress.service.crud.movie.CrewCrudService;
import com.slyscrat.impress.service.crud.movie.MovieCrudService;
import com.slyscrat.impress.service.crud.movie.MovieGenreCrudService;
import com.slyscrat.impress.service.scheduled.AbstractApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:sources.properties")
public class MovieDBServiceImpl extends AbstractApi implements MovieDBService {
    @Value("${moviedb.api.key}")
    private String key;

    @Value("${language}")
    private String language;

    private final MovieGenreCrudService movieGenreCrudService;
    private final MovieCrudService movieCrudService;
    private final CrewCrudService crewCrudService;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    //@Scheduled(cron = "0 0 5 * * ?")
    public void checkNewMovies() throws MovieDbException {
        Set<Integer> existedMovies = new HashSet<>(movieCrudService.getIdsSet());
        Set<Integer> movies = new HashSet<>();
        int movieId;
        try (
                FileReader fr = new FileReader(getClass().getClassLoader().getResource("movie_ids_05_01_2020.json").getFile());
                BufferedReader reader = new BufferedReader(fr))
        {
            String line;
            double popularity;
            while ((line = reader.readLine()) != null) {
                popularity = Double.parseDouble(line.substring(line.indexOf("\"popularity\"") + 13, line.indexOf(",\"v")));
                if (containsHanScript(line) || popularity < 10) continue;
                movieId = Integer.parseInt(line.substring(line.indexOf("id") + 4, line.indexOf(",\"o")));
                movies.add(movieId);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        movies.removeAll(existedMovies);

        TheMovieDbApi api = new TheMovieDbApi(key);
        movies.forEach(movie -> {
            try {
                addMovieWithCast(api, movie);
            } catch (MovieDbException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveNewGenres() throws MovieDbException {
        TheMovieDbApi api = new TheMovieDbApi(key);
        List<Genre> genres = api.getGenreMovieList(language).getResults();
        genres.forEach(genre -> {
            MovieGenreDto genreDto = new MovieGenreDto();
            genreDto.setId(genre.getId());
            genreDto.setName(genre.getName());
            movieGenreCrudService.create(genreDto);
        });
    }

    private void addMovieWithCast(TheMovieDbApi api, int movieId) throws MovieDbException {
        MovieInfo movie = api.getMovieInfo(movieId, language);
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setGenres(movie.getGenres().stream().map(genre -> {
            MovieGenreDto genreDto = new MovieGenreDto();
            genreDto.setId(genre.getId());
            genreDto.setName(genre.getName());
            return genreDto;
        }).collect(Collectors.toSet()));
        movieDto.setDescription(movie.getOverview());
        movieDto.setDuration((short)movie.getRuntime());
        movieDto.setPoster(movie.getPosterPath());
        if (movieDto.getPoster() == null) return;
        movieDto.setName(movie.getTitle());
        try {
            movieDto.setReleaseDate(dateFormat.parse(movie.getReleaseDate()));
        } catch (ParseException e) {
            return;
        }

        MediaCreditList creditList = api.getMovieCredits(movieId);
        int castCounter = 5;
        int crewCounter = 3;
        CrewDto crewDto;
        MovieCrewDto movieCrewDto;
        Set<MovieCrewDto> movieCrewDtoSet = new HashSet<>(castCounter + crewCounter);
        for (MediaCreditCast cast : creditList.getCast()) {
            if (castCounter == 0) break;
            crewDto = new CrewDto();
            movieCrewDto = new MovieCrewDto();
            movieCrewDto.setMovieId(movieId);
            movieCrewDto.setCrewId(cast.getId());
            movieCrewDto.setPost("Crew");
            movieCrewDtoSet.add(movieCrewDto);
            crewDto.setId(cast.getId());
            crewDto.setName(cast.getName());
            crewCrudService.create(crewDto);
            castCounter--;
        }

        for (MediaCreditCrew crew : creditList.getCrew()) {
            if (crewCounter == 0) break;
            if (crew.getJob().equals("Director") || crew.getJob().equals("Producer") || crew.getJob().equals("Writer")) {
                crewDto = new CrewDto();
                crewDto.setId(crew.getId());
                crewDto.setName(crew.getName());
                movieCrewDto = new MovieCrewDto();
                movieCrewDto.setMovieId(movieId);
                movieCrewDto.setCrewId(crew.getId());
                movieCrewDto.setPost(crew.getJob());
                movieCrewDtoSet.add(movieCrewDto);
                crewCrudService.create(crewDto);
                crewCounter--;
            }
        }
        movieDto.setCrew(movieCrewDtoSet);
        movieCrudService.create(movieDto);
    }
}
