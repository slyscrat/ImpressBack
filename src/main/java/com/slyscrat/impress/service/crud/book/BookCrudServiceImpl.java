package com.slyscrat.impress.service.crud.book;

import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.model.entity.BookEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.book.BookRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookCrudServiceImpl
        extends AbstractCrudService<BookEntity, BookDto, BookRepository>
        implements BookCrudService {

    @PersistenceContext
    EntityManager em;
    
    @Autowired
    public BookCrudServiceImpl(BookRepository repository,
                               Mapper<BookEntity, BookDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public Set<Integer> getIdsSet() {
        return repository.getIdsSet();
    }

    @Override
    public BookDto create(BookDto dto) {
        BookEntity bookEntity = new BookEntity();
        mapper.map(dto, bookEntity);
        return mapper.map(repository.save(bookEntity));
    }

    @Override
    public BookDto update(BookDto dto) {
        BookEntity bookEntity = repository.findById(dto.getId())
                .orElse(new BookEntity());
        mapper.map(dto, bookEntity);
        return mapper.map(repository.save(bookEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public List<BookDto> findAll(Pageable paging) {
        return repository.findAll(paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> findAllByGenre(Integer genreId, Pageable paging) {
        return repository.findAllByTags_IdEquals(genreId, paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> findAllByGenresIds(Set<Integer> ids, Pageable paging) {
        Set<Integer> bookIds = getBooksIdsByGenres(ids);
        bookIds.forEach(System.out::println);
        return repository.findAllByIdIn(bookIds, paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    private Set<Integer> getBooksIdsByGenres(Set<Integer> genresIds) {
        StringBuilder sb = new StringBuilder("select\n" +
                "  books.id\n" +
                "from books\n" +
                "  left outer join tag_to_book ON books.id = tag_to_book.book_id\n" +
                "where\n" +
                "  true ");

        genresIds.forEach(genre -> {
            sb.append("and EXISTS (SELECT book_id FROM tag_to_book gm WHERE books.id = gm.book_id AND gm.tag_id = ");
            sb.append(genre.toString());
            sb.append(") ");
        });
        sb.append("group by books.id");

        return new HashSet<>(em.createNativeQuery(sb.toString()).getResultList());
    }
}
