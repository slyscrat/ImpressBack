package com.slyscrat.impress.controller;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.service.business.game.GameService;
import com.slyscrat.impress.service.crud.UserCrudService;
import com.slyscrat.impress.service.crud.game.GameCrudService;
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
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final GameCrudService gameCrudService;
    private final UserCrudService userCrudService;

    @GetMapping("/{id}")
    public ResponseEntity<GameDto> gameInfo(@PathVariable Integer id) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(gameService.getById(id, userId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<GameDto>> gameList(@RequestParam(name = "p") Optional<Integer> page,
                                                     @RequestParam(name = "s") Optional<Integer> sort,
                                                     @RequestParam(name = "g") Optional<Set<Integer>> genres) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(gameService.getAllList(page.orElse(0), sort.orElse(1), genres.orElse(null), userId));
    }

    @GetMapping("/list/search")
    public ResponseEntity<List<GameDto>> gameSearchList(@RequestParam(name = "p") Optional<Integer> page,
                                                    @RequestParam(name = "q") Optional<String> name) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(gameService.getByNameList(page.orElse(0), name.orElse(null), userId));
    }

    @GetMapping("/list/future")
    public ResponseEntity<List<GameDto>> gameFutureList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(gameService.getFutureList(page.orElse(0), userId));
    }

    @GetMapping("/list/rated")
    public ResponseEntity<List<GameDto>> gameRatedList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(gameService.getRatedList(page.orElse(0), userId));
    }

    @GetMapping("/list/recommended")
    public ResponseEntity<List<GameDto>> gameRecommendedList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        if (userId.equals(0) || gameService.getRatedList(0,userId).isEmpty()) return ResponseEntity.ok(null);
        return ResponseEntity.ok(gameService.getRecommendedList(page.orElse(0), userId));
    }

    @PostMapping("/{id}/note")
    public ResponseEntity<ItemRateDto> gameNote(@PathVariable Integer id, @RequestBody String note) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        if (note.length() == 0) return ResponseEntity.badRequest().body(null);
        try {
            note = URLDecoder.decode(note.substring(0, note.length() - 1), StandardCharsets.UTF_8.name());
            if (note.length() > 255) note = note.substring(0, 255);
            ItemRateDto result = gameService.note(id, note, userId);
            if (result == null) return ResponseEntity.badRequest().body(null);
            return ResponseEntity.ok(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/{id}/note/del")
    public ResponseEntity<String> gameNoteDel(@PathVariable Integer id) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        gameService.noteDel(id, userId);
        return ResponseEntity.ok("done");
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<ItemRateDto> gameRate(@PathVariable Integer id, @RequestBody String rate) {
        Short points = Short.valueOf(rate.substring(0,1));
        if (points < 0 || points > 5) return ResponseEntity.badRequest().body(null);
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        ItemRateDto result = gameService.rate(id, points, userId);
        if (result == null) return ResponseEntity.badRequest().body(null);
        if (result.getUser().equals(0)) return ResponseEntity.ok().body(null);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<GameDto> gameEdit(@RequestBody GameDto gameDto) {
        return ResponseEntity.ok(gameCrudService.update(gameDto));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> gameDelete(@PathVariable Integer id) {
        gameCrudService.delete(id);
        return ResponseEntity.ok("done");
    }

    @GetMapping("/list/genres")
    public ResponseEntity<List<GameGenreDto>> movieGenreInfo() {
        return ResponseEntity.ok(gameService.getGenres());
    }
}
