package com.slyscrat.impress.service.scheduled.book;

import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.model.dto.book.BookTagDto;
import com.slyscrat.impress.service.crud.book.BookCrudService;
import com.slyscrat.impress.service.crud.book.BookTagCrudService;
import com.slyscrat.impress.service.scheduled.AbstractApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:sources.properties")
public class LibraryThingApiServiceImpl extends AbstractApi implements LibraryThingApiService {

    @Value("${librarything.api.books.list.url}")
    private String booksListUrl;

    private final BookCrudService bookCrudService;
    private final BookTagCrudService bookTagCrudService;
    private final LibraryThingJsonParser jsonParser;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    //@Scheduled(cron = "0 0 4 * * ?")
    public void checkNewBooks() {
        Set<Integer> existedBooksIds = new HashSet<>(bookCrudService.getIdsSet());
        Set<BookTagDto> existedTagsNames = new HashSet<>(bookTagCrudService.getTagsSet());
        String reqResult = sendRequest(booksListUrl)
                .replace("var widgetResults = ","")
                .replace(";LibraryThing.bookAPI.displayWidgetContents(widgetResults, \"LT_Content\");","");
        System.out.println(reqResult);

        Set<BookTagDto> newTags = jsonParser.getBookTagsSet(reqResult);
        newTags.removeAll(existedTagsNames);
        newTags.forEach(bookTagCrudService::create);
        existedTagsNames.addAll(bookTagCrudService.getTagsSet());

        Set<BookDto> newBooks = jsonParser.getBooksSet(reqResult, existedTagsNames);
        newBooks.removeIf(book -> existedBooksIds.contains(book.getId()));
        newBooks.forEach(bookCrudService::create);
    }

    private String sendRequest(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
