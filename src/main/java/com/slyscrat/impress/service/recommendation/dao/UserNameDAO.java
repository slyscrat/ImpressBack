package com.slyscrat.impress.service.recommendation.dao;

import org.grouplens.lenskit.data.dao.UserDAO;

public interface UserNameDAO extends UserDAO {
    public long getUserByName(String name);
}
