package com.slyscrat.impress.service.recommendation;

import org.grouplens.grapht.annotation.DefaultProvider;
import org.grouplens.lenskit.core.Shareable;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;

import java.io.Serializable;
import java.util.Map;

@Shareable
@DefaultProvider(TFIDFModelBuilder.class)
public class TFIDFModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Long> tagIds;
    private final Map<Long, SparseVector> itemVectors;

    TFIDFModel(Map<String,Long> tagIds, Map<Long,SparseVector> itemVectors) {
        this.tagIds = tagIds;
        this.itemVectors = itemVectors;
    }

    public MutableSparseVector newTagVector() {
        return MutableSparseVector.create(tagIds.values());
    }

    public SparseVector getItemVector(long item) {
        SparseVector vec = itemVectors.get(item);
        if (vec == null) {
            return SparseVector.empty();
        } else {
            return vec;
        }
    }
}
