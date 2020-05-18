package com.slyscrat.impress.service.recommendation;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.grouplens.lenskit.core.Transient;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import com.slyscrat.impress.service.recommendation.dao.ItemTagDAO;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TFIDFModelBuilder implements Provider<TFIDFModel> {
    private final ItemTagDAO dao;

    @Inject
    public TFIDFModelBuilder(@Transient ItemTagDAO dao) {
        this.dao = dao;
    }

    @Override
    public TFIDFModel get() {
        Map<String, Long> tagIds = buildTagIdMap();

        MutableSparseVector docFreq = MutableSparseVector.create(tagIds.values());
        docFreq.fill(0);

        Map<Long,MutableSparseVector> itemVectors = Maps.newHashMap();
        MutableSparseVector work = MutableSparseVector.create(tagIds.values());

        LongSet items = dao.getItemIds();
        long counter = 0;
        for (long item: items) {
            counter++;
            work.clear();

            List<String> tags =  dao.getItemTags(item);
            for(String tag: tags){
                long tagId = tagIds.get(tag);

                try{
                    work.set(tagId, work.get(tagId)+1);
                }catch(IllegalArgumentException e){
                    work.set(tagId, 1);
                }
            }
            MutableSparseVector temp = MutableSparseVector.create(tagIds.values());

            for(String tag: tags){
                long tagId = tagIds.get(tag);
                try{
                    temp.get(tagId);
                    continue;
                }catch(IllegalArgumentException e){
                    temp.set(tagId, 1);
                    docFreq.set(tagId, docFreq.get(tagId)+1);
                }

            }
            itemVectors.put(item, work.shrinkDomain());
        }

        for (VectorEntry e: docFreq.fast()) {
            long tagId = e.getKey();
            double idf = e.getValue();
            double log_idf = Math.log10(counter/idf);
            docFreq.set(tagId, log_idf);
        }

        Map<Long,SparseVector> modelData = Maps.newHashMap();
        for (Map.Entry<Long,MutableSparseVector> entry: itemVectors.entrySet()) {
            MutableSparseVector tv = entry.getValue();
            for(VectorEntry v: tv.fast()){
                double tf = v.getValue();
                double idf = docFreq.get(v.getKey());
                tv.set(v.getKey(), tf*idf);
            }

            double len = tv.norm();
            for(VectorEntry v: tv.fast()){
                tv.set(v.getKey(), v.getValue()/len);
            }

            modelData.put(entry.getKey(), tv.freeze());
        }

        return new TFIDFModel(tagIds, modelData);
    }

    private Map<String,Long> buildTagIdMap() {
        Set<String> tags = dao.getTagVocabulary();
        Map<String,Long> tagIds = Maps.newHashMap();

        for (String tag: tags) {
            tagIds.put(tag, tagIds.size() + 1L);
        }
        return tagIds;
    }
}
