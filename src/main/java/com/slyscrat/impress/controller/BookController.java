package com.slyscrat.impress.controller;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.service.business.book.BookService;
import com.slyscrat.impress.service.crud.UserCrudService;
import com.slyscrat.impress.service.crud.book.BookCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookCrudService bookCrudService;
    private final UserCrudService userCrudService;

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> bookInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getById(id, 1));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BookDto>> bookList(@RequestParam(name = "p") Optional<Integer> page,
                                                     @RequestParam(name = "s") Optional<Integer> sort) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(bookService.getAllList(page.orElse(0), sort.orElse(1), userId));
    }

    @GetMapping("/list/search")
    public ResponseEntity<List<BookDto>> bookSearchList(@RequestParam(name = "p") Optional<Integer> page,
                                                    @RequestParam(name = "q") Optional<String> name) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(bookService.getByNameList(page.orElse(0), name.orElse(null), userId));
    }

    @GetMapping("/list/future")
    public ResponseEntity<List<BookDto>> bookFutureList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(bookService.getFutureList(page.orElse(0), userId));
    }

    @GetMapping("/list/rated")
    public ResponseEntity<List<BookDto>> bookRatedList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(bookService.getRatedList(page.orElse(0), userId));
    }

    @GetMapping("/list/recommended")
    public ResponseEntity<List<BookDto>> bookRecommendedList(@RequestParam(name = "p") Optional<Integer> page) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        return ResponseEntity.ok(bookService.getRecommendedList(page.orElse(0), userId));
    }

    @PostMapping("/{id}/note")
    public ResponseEntity<ItemRateDto> bookNote(@PathVariable Integer id, @RequestBody String note) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        if (note.length() > 255) note = note.substring(0, 255);
        ItemRateDto result = bookService.note(id, note, userId);
        if (result == null) return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/note/del")
    public ResponseEntity<String> bookNoteDel(@PathVariable Integer id) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        bookService.noteDel(id, userId);
        return ResponseEntity.ok("done");
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<ItemRateDto> bookRate(@PathVariable Integer id, @RequestBody Short rate) {
        if (rate < 0 || rate > 5) return ResponseEntity.badRequest().body(null);
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        ItemRateDto result = bookService.rate(id, rate, userId);
        if (result == null) return ResponseEntity.badRequest().body(null);
        if (result.getUser().equals(0)) return ResponseEntity.ok().body(null);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<BookDto> bookEdit(@RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookCrudService.update(bookDto));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> bookDelete(@PathVariable Integer id) {
        bookCrudService.delete(id);
        return ResponseEntity.ok("done");
    }
}
