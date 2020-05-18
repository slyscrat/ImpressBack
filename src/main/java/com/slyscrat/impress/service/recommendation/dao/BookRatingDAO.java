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

public class BookRatingDAO implements EventDAO {
    private final JDBCRatingDAO jdbcDao;
    private transient volatile EventCollectionDAO cache;

    public BookRatingDAO() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/impress_db?user=postgres&password=postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        jdbcDao = new JDBCRatingDAO(connection, new BookSqlStatementFactory());
    }

    private void ensureRatingCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = new EventCollectionDAO(Cursors.makeList(jdbcDao.streamEvents()));
                }
            }
        }
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
