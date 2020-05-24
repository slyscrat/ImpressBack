package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.entity.BookEntity;
import com.slyscrat.impress.model.entity.BookRateEntity;
import com.slyscrat.impress.model.entity.UserEntity;
import com.slyscrat.impress.model.repository.UserRepository;
import com.slyscrat.impress.model.repository.book.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BookRateMapper extends AbstractMapper<BookRateEntity, ItemRateDto> {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookRateMapper(BookRepository bookRepository,
                          ModelMapper modelMapper,
                          UserRepository userRepository) {
        super(modelMapper);
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> {
                    m.skip(BookRateEntity::setId);
                    m.skip(BookRateEntity::setBook);
                    m.skip(BookRateEntity::setUser);
                })
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .addMappings(m -> {
                    m.skip(ItemRateDto::setItem);
                    m.skip(ItemRateDto::setUser);
                })
                .setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(BookRateEntity source, ItemRateDto destination) {
        destination.setItem(source.getBook().getId());
        destination.setUser(source.getUser().getId());
    }

    @Override
    protected void mapSpecificFields(ItemRateDto source, BookRateEntity destination) {
        BookEntity bookEntity = bookRepository.findById(source.getItem())
                .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, source.getItem()));
        UserEntity userEntity = userRepository.findById(source.getUser())
                .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, source.getUser()));

        bookEntity.getRated().add(destination);
        userEntity.getBookRates().add(destination);


        destination.setBook(bookEntity);
        destination.setUser(userEntity);
    }
}

