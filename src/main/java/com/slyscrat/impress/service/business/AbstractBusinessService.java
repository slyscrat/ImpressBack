package com.slyscrat.impress.service.business;

import java.util.Set;

public abstract class AbstractBusinessService {
    protected Integer pageSize = 20;
    protected abstract Set<Integer> checkFilters(Set<Integer> ids);
    protected abstract SortEnum checkSort(Integer sortMethod);
}
