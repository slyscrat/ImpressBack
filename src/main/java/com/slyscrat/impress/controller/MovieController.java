package com.slyscrat.impress.controller;

import com.slyscrat.impress.model.dto.movie.MovieDto;
import com.slyscrat.impress.service.business.movie.MovieService;
import com.slyscrat.impress.service.crud.movie.MovieCrudService;
import com.slyscrat.impress.service.crud.movie.MovieRateCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieRateCrudService movieRateService;
    private final MovieService movieService;
    private final MovieCrudService movieCrudService;

    @GetMapping("/list")
    public ResponseEntity<String> movie(@RequestParam(name = "p") Optional<Integer> page,
                                        @RequestParam(name = "s") Optional<Integer> sort,
                                        @RequestParam(name = "g") Optional<Set<Integer>> genres) {
        List<MovieDto> result = movieService.getAll(page.orElse(1), sort.orElse(1), genres.orElse(null));
        result.forEach(System.out::println);
        System.out.println(result.size());
        return ResponseEntity.ok("Nice");
    }
}
