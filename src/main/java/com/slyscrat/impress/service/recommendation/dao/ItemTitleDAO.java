package com.slyscrat.impress.service.recommendation.dao;

import org.grouplens.lenskit.data.dao.ItemDAO;

import javax.annotation.Nullable;

public interface ItemTitleDAO extends ItemDAO {
    @Nullable
    public String getItemTitle(long item);
}
