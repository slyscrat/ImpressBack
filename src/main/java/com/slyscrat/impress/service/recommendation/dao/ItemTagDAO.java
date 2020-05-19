package com.slyscrat.impress.service.recommendation.dao;

import org.grouplens.lenskit.data.dao.ItemDAO;

import java.util.List;
import java.util.Set;

public interface ItemTagDAO extends ItemDAO {
    List<String> getItemTags(long item);
    Set<String> getTagVocabulary();
}
