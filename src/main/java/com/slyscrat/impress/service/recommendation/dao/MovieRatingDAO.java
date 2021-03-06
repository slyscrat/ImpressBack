package com.slyscrat.impress.service.recommendation.dao;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.cursors.Cursors;
import org.grouplens.lenskit.data.dao.EventCollectionDAO;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MovieRatingDAO implements EventDAO {
    //private final SimpleFileRatingDAO csvDao;
    private final JDBCRatingDAO jdbcDao;
    private transient volatile EventCollectionDAO cache;
    //private final MovieRateRepository repository;

    //@Inject
    public MovieRatingDAO() {
        Connection connection = null;
        //csvDao = new SimpleFileRatingDAO(new File("D:\\Projects\\Impress\\data\\ratings.csv"), ",");
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/impress_db?user=postgres&password=postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        jdbcDao = new JDBCRatingDAO(connection, new MovieSqlStatementFactory());
        //repository = ApplicationContextProvider.getApplicationContext().getBean(MovieRateRepository.class);
        //this.movieRateCrudService = movieRateCrudService;
    }

    private void ensureRatingCache() {
        cache = new EventCollectionDAO(Cursors.makeList(jdbcDao.streamEvents()));
        /*if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    //cache = new EventCollectionDAO(Cursors.makeList(csvDao.streamEvents()));

                    //cache = new EventCollectionDAO(Cursors.makeList(repository.findAll()));
                    // cache = new EventCollectionDAO(list);
                    // System.out.println(cache);
                }
            }
        }*/
    }

    @Override
    public Cursor<Event> streamEvents() {
        ensureRatingCache();
        return cache.streamEvents();
    }

    @Override
    public <E extends Event> Cursor<E> streamEvents(Class<E> type) {
        ensureRatingCache();
        return cache.streamEvents(type);
    }

    @Override
    public <E extends Event> Cursor<E> streamEvents(Class<E> type, SortOrder order) {
        ensureRatingCache();
        return cache.streamEvents(type, order);
    }
}
