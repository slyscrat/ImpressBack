package com.slyscrat.impress.service.recommendation.dao;

import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.sql.SQLStatementFactory;

public class GameSqlStatementFactory implements SQLStatementFactory {
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
        return "select id, name from games;";
    }

    @Override
    public String prepareItemCount() {
        return "select count(*) from games;";
    }

    @Override
    public String prepareEvents(SortOrder sortOrder) {
        return "select user_id, game_id, rate from game_rates;";
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
