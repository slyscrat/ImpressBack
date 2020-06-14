package com.slyscrat.impress.controller;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.movie.MovieDto;
import com.slyscrat.impress.model.dto.movie.MovieGenreDto;
import com.slyscrat.impress.service.business.movie.MovieService;
import com.slyscrat.impress.service.crud.UserCrudService;
import com.slyscrat.impress.service.crud.movie.MovieCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieCrudService movieCrudService;
    private final UserCrudService userCrudService;

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> movieInfo(@PathVariable Integer id) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(movieService.getById(id, userId));
    }

    @GetMapping("/list/genres")
    public ResponseEntity<List<MovieGenreDto>> movieGenreInfo() {
        return ResponseEntity.ok(movieService.getGenres());
    }

    @GetMapping("/list")
    public ResponseEntity<List<MovieDto>> movieList(@RequestParam(name = "p") Optional<Integer> page,
                                                     @RequestParam(name = "s") Optional<Integer> sort,
                                                     @RequestParam(name = "g") Optional<Set<Integer>> genres) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(movieService.getAllList(page.orElse(0), sort.orElse(1), genres.orElse(null), userId));
    }

    @GetMapping("/list/search")
    public ResponseEntity<List<MovieDto>> movieSearchList(@RequestParam(name = "p") Optional<Integer> page,
                                                    @RequestParam(name = "q") Optional<String> name) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(movieService.getByNameList(page.orElse(0), name.orElse(null), userId));
    }

    @GetMapping("/list/future")
    public ResponseEntity<List<MovieDto>> movieFutureList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(movieService.getFutureList(page.orElse(0), userId));
    }

    @GetMapping("/list/rated")
    public ResponseEntity<List<MovieDto>> movieRatedList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(movieService.getRatedList(page.orElse(0), userId));
    }

    @GetMapping("/list/recommended")
    public ResponseEntity<List<MovieDto>> movieRecommendedList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        if (userId.equals(0) || movieService.getRatedList(0,userId).isEmpty()) return ResponseEntity.ok(null);
        return ResponseEntity.ok(movieService.getRecommendedList(page.orElse(0), userId));
    }

    @PostMapping("/{id}/note")
    public ResponseEntity<ItemRateDto> movieNote(@PathVariable Integer id, @RequestBody String note) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        if (note.length() == 0) return ResponseEntity.badRequest().body(null);
        try {
            note = URLDecoder.decode(note.substring(0, note.length() - 1), StandardCharsets.UTF_8.name());
            if (note.length() > 255) note = note.substring(0, 255);
            ItemRateDto result = movieService.note(id, note, userId);
            if (result == null) return ResponseEntity.badRequest().body(null);
            return ResponseEntity.ok(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/{id}/note/del")
    public ResponseEntity<String> movieNoteDel(@PathVariable Integer id) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        movieService.noteDel(id, userId);
        return ResponseEntity.ok("done");
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<ItemRateDto> movieRate(@PathVariable Integer id, @RequestBody String rate) {
        Short points = Short.valueOf(rate.substring(0,1));
        if (points < 0 || points > 5) return ResponseEntity.badRequest().body(null);
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        ItemRateDto result = movieService.rate(id, points, userId);
        if (result == null) return ResponseEntity.badRequest().body(null);
        if (result.getUser().equals(0)) return ResponseEntity.ok().body(null);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<MovieDto> movieEdit(@RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieCrudService.update(movieDto));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> movieDelete(@PathVariable Integer id) {
        movieCrudService.delete(id);
        return ResponseEntity.ok("done");
    }
}
