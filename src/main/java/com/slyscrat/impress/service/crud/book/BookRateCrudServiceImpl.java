package com.slyscrat.impress.service.crud.book;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.entity.BookRateEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.book.BookRateRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class BookRateCrudServiceImpl
        extends AbstractCrudService<BookRateEntity, ItemRateDto, BookRateRepository>
        implements BookRateCrudService {

    @Autowired
    public BookRateCrudServiceImpl(BookRateRepository repository,
                                   Mapper<BookRateEntity, ItemRateDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public ItemRateDto create(ItemRateDto dto) {
        BookRateEntity bookRateEntity = new BookRateEntity();
        mapper.map(dto, bookRateEntity);
        return mapper.map(repository.save(bookRateEntity));
    }

    @Override
    public ItemRateDto update(ItemRateDto dto) {
        BookRateEntity bookRateEntity = repository.findByUser_IdAndBook_Id(dto.getUser(), dto.getItem()).orElse(new BookRateEntity());
        mapper.map(dto, bookRateEntity);
        return mapper.map(repository.save(bookRateEntity));
    }

    @Override
    public void delete(Integer id) { }

    @Override
    public void delete(Integer userId, Integer bookId) {
        repository.findByUser_IdAndBook_Id(userId, bookId).ifPresent(repository::delete);
    }

    @Override
    public ItemRateDto findByUserAndBook(Integer userId, Integer bookId) {
        BookRateEntity rateEntity = repository.findByUser_IdAndBook_Id(userId, bookId).orElse(null);
        if (rateEntity != null) return mapper.map(rateEntity);
        return null;
    }

    @Override
    public long count() {
        return repository.getCount();
    }
}
