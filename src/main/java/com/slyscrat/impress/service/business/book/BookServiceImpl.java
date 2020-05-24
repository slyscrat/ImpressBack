package com.slyscrat.impress.service.business.book;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.model.dto.book.BookFullDto;
import com.slyscrat.impress.model.entity.BookEntity;
import com.slyscrat.impress.model.mapper.BookMapper;
import com.slyscrat.impress.model.repository.book.BookRepository;
import com.slyscrat.impress.service.business.AbstractBusinessService;
import com.slyscrat.impress.service.business.SortEnum;
import com.slyscrat.impress.service.crud.book.BookCrudService;
import com.slyscrat.impress.service.crud.book.BookRateCrudService;
import com.slyscrat.impress.service.recommendation.RecommendationSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl extends AbstractBusinessService implements BookService {
    private final BookCrudService bookCrudService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookRateCrudService bookRateCrudService;
    private final RecommendationSystem recommendationSystem;

    private static final String RELEASE_DATE = "publicationDate";

    @Override
    public void noteDel(Integer bookId, Integer userId) {
        if (!isBookPresent(bookId)) return;
        ItemRateDto itemRateDto = bookRateCrudService.findByUserAndBook(userId, bookId);
        if (itemRateDto != null) {
            itemRateDto.setNote("");
            bookRateCrudService.update(itemRateDto);
        }
    }

    @Override
    public ItemRateDto rate(Integer bookId, Short rate, Integer userId) {
        if (!isBookPresent(bookId)) return null;
        ItemRateDto itemRateDto = bookRateCrudService.findByUserAndBook(userId, bookId);
        if (itemRateDto == null) {
            itemRateDto = new ItemRateDto();
            itemRateDto.setItem(bookId);
            itemRateDto.setUser(userId);
            itemRateDto.setRate(rate);
        }
        else if (itemRateDto.getRate().equals(rate)) {
            if (itemRateDto.getNote().length() == 0) {
                bookRateCrudService.delete(userId, bookId);
                itemRateDto.setUser(0);
                return itemRateDto;
            }
            itemRateDto.setRate((short) 0);
        }
        else itemRateDto.setRate(rate);
        return bookRateCrudService.update(itemRateDto);
    }

    @Override
    public ItemRateDto note(Integer bookId, String note, Integer userId) {
        if (!isBookPresent(bookId)) return null;
        ItemRateDto itemRateDto = bookRateCrudService.findByUserAndBook(userId, bookId);
        if (itemRateDto == null) {
            itemRateDto = new ItemRateDto();
            itemRateDto.setItem(bookId);
            itemRateDto.setUser(userId);
        }
        itemRateDto.setNote(note);
        return bookRateCrudService.update(itemRateDto);
    }

    private boolean isBookPresent(Integer bookId) {
        return bookRepository.findById(bookId).isPresent();
    }

    @Override
    public List<BookDto> getRecommendedList(Integer page, Integer userId) {
        List<Integer> ids = recommendationSystem.recommendBook(userId);
        int startIndex = page < 0 ? 0 : page * pageSize;
        int endIndex = page < 1 ? pageSize : (page + 1) * pageSize;
        ids = ids.subList(startIndex, endIndex);
        List<BookEntity> books = bookRepository.findAllByIdIn(ids);
        List<Integer> finalIds = ids;
        books.sort(Comparator.comparing(book -> finalIds.indexOf(book.getId())));
        return books
                .stream()
                .map(bookMapper::map)
                .map(this::mapToShort)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getFutureList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        List<BookDto> res = new ArrayList<>();
        bookRepository.findAllByRated_User_Id_AndRated_Rate(userId, (short) 0, paging)
                .forEach(bookEntity -> res.add(mapToFuture(bookMapper.map(bookEntity))));
        return res;
    }

    @Override
    public List<BookDto> getRatedList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        List<BookDto> res = new ArrayList<>();
        bookRepository.findAllByRated_User_Id_AndRated_RateGreaterThan(userId, (short) 0, paging)
                .forEach(bookEntity -> res.add(mapToShort(bookMapper.map(bookEntity), userId)));
        return res;
    }

    @Override
    public List<BookDto> getAllList(Integer page, Integer sort, Integer userId) {
        Pageable paging;
        switch (checkSort(sort)) {
            case DateASC:
                paging = PageRequest.of(page, pageSize, Sort.by(RELEASE_DATE).ascending());
                break;
            case NameASC:
                paging = PageRequest.of(page, pageSize, Sort.by("name").ascending());
                break;
            case NameDSC:
                paging = PageRequest.of(page, pageSize, Sort.by("name").descending());
                break;
            default:
                paging = PageRequest.of(page, pageSize, Sort.by(RELEASE_DATE).descending());
                break;
        }
        return bookCrudService.findAll(paging).stream()
                    .map(item -> mapToShort(item, userId))
                    .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getByNameList(Integer page, String name, Integer userId) {
        if (name == null) return getAllList(page, 0, userId);
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(RELEASE_DATE).descending());
        return bookRepository.findAllByNameContainsIgnoreCase(name, paging).toList().stream()
                .map(bookMapper::map)
                .map(bookDto -> mapToShort(bookDto, userId))
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getById(Integer id, Integer userId) {
        return mapToShort(bookCrudService.findById(id), userId);
    }

    private BookFullDto mapToShort(BookDto bookDto, Integer userId) {
        BookFullDto bookShortDto = new BookFullDto(bookDto);
        ItemRateDto rate = null;
        if (userId != 0)
            rate = bookRateCrudService.findByUserAndBook(userId,bookDto.getId());
        if (rate != null) {
            bookShortDto.setRate(rate.getRate());
            bookShortDto.setFutured(rate.getRate() == 0);
        }
        return bookShortDto;
    }

    private BookFullDto mapToShort(BookDto bookDto) {
        return new BookFullDto(bookDto);
    }

    private BookFullDto mapToFuture(BookDto bookDto) {
        BookFullDto bookShortDto = mapToShort(bookDto);
        bookShortDto.setRate((short) 0);
        bookShortDto.setFutured(true);
        return bookShortDto;
    }

    @Override
    protected Set<Integer> checkFilters(Set<Integer> inTags) { return null;}

    @Override
    protected SortEnum checkSort(Integer sortMethod) {
        return SortEnum.findByKey(sortMethod);
    }
}
