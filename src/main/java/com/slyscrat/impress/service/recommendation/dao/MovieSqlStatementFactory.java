package com.slyscrat.impress.service.recommendation.dao;

import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.sql.SQLStatementFactory;

public class MovieSqlStatementFactory implements SQLStatementFactory {
    @Override
    public String prepareUsers() {
        return "select id, name from users;";
    }

    @Override
    public String prepareUserCount() {
        return "select count(*) from users;";
    }

    @Override
    public String prepareItems() {
        return "select id, name from movies;";
    }

    @Override
    public String prepareItemCount() {
        return "select count(*) from movies;";
    }

    @Override
    public String prepareEvents(SortOrder sortOrder) {
        return "select user_id, movie_id, rate from movie_rates where rate > 0;";
    }

    @Override
    public String prepareUserEvents() {
        return null;
    }

    @Override
    public String prepareItemEvents() {
        return null;
    }

    @Override
    public String prepareItemUsers() {
        return null;
    }
}
