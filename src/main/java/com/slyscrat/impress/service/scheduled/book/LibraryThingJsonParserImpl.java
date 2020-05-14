package com.slyscrat.impress.service.scheduled.book;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.model.dto.book.BookTagDto;
import com.slyscrat.impress.service.scheduled.AbstractJsonParser;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryThingJsonParserImpl extends AbstractJsonParser implements LibraryThingJsonParser {

    @Override
    public Set<BookTagDto> getBookTagsSet(String json) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        JSONArray tags = JsonPath.read(document, "$.books.*.tags");
        return tags.stream()
                .flatMap(tagArray -> ((JSONArray) tagArray).stream())
                .map(tag -> {
                    BookTagDto bookTagDto = new BookTagDto();
                    bookTagDto.setName(tag.toString());
                    return bookTagDto;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Set<BookDto> getBooksSet(String json, Set<BookTagDto> tags) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        Set<BookDto> set = new LinkedHashSet<>();
        int maxTextLength = 255;
        int dateLength = 4;

        JSONArray books = ((JSONArray) JsonPath.read(document, "$.books.*"));
        for (Object bookJson : books) {
            try {
                String data;
                BookDto bookDto = new BookDto();
                bookDto.setId(Integer.parseInt(JsonPath.read(bookJson, "$.book_id")));

                data = ((String) JsonPath.read(bookJson, "$.cover")).replace("\\", "");
                if (!isValidLength(data, maxTextLength)) continue;
                bookDto.setIcon(data);

                data = ((String) JsonPath.read(bookJson, "$.title")).replaceAll("[^\\p{ASCII}]", "");
                data = isValidLength(data, maxTextLength) ? data: data.substring(0, maxTextLength);
                bookDto.setName(data);

                data = JsonPath.read(bookJson, "$.author_fl");
                data = isValidLength(data, maxTextLength) ? data: data.substring(0, maxTextLength);
                bookDto.setAuthor(data);

                data = (String) JsonPath.read(bookJson, "$.publicationdate");
                data = isValidLength(data, dateLength) ? data: data.substring(0, dateLength);
                bookDto.setPublicationDate(data);

                try {
                    JSONArray newTags = JsonPath.read(bookJson, "$.tags");
                    if (!newTags.isEmpty()) {
                        Set<BookTagDto> newSet = new HashSet<>();
                        for (Object tag : newTags) {
                            for (BookTagDto bookTagDto : tags) {
                                if (bookTagDto.getName().equals(tag))
                                    newSet.add(bookTagDto);
                            }
                        }
                        bookDto.setTags(newSet);
                    }
                } catch (PathNotFoundException ex) {
                }
                if (bookDto.getId() == null || bookDto.getName() == null
                        || bookDto.getIcon() == null || bookDto.getAuthor() == null) continue;
                set.add(bookDto);
            } catch(NullPointerException ex){}
        }

        return set;
    }

    private boolean isValidLength(String data, int length) {
        return data.length() <= length;
    }
}
