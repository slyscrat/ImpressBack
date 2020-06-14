package com.slyscrat.impress.service.recommendation;

import com.slyscrat.impress.service.crud.movie.MovieCrudService;
import com.slyscrat.impress.service.recommendation.dao.*;
import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.scored.ScoredId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationSystem {
    private /*final*/ ItemRecommender movieRec;
    private /*final*/ ItemRecommender gameRec;
    private /*final*/ ItemRecommender bookRec;

    @Autowired
    public RecommendationSystem(MovieCrudService movieCrudService) throws RecommenderBuildException {
        /*
        this.movieRec = null;
        this.bookRec = null;
        this.gameRec = null;
        */

        this.movieRec = LenskitRecommender.build(configureMovieRecommender()).getItemRecommender();
        this.bookRec = LenskitRecommender.build(configureBookRecommender()).getItemRecommender();
        this.gameRec = LenskitRecommender.build(configureGameRecommender()).getItemRecommender();

    }

    public List<Integer> recommendMovie(long userId) {
        List<ScoredId> recs = movieRec.recommend(userId, 100);
        final RecommendationSystem rs = this;
        new Thread() {
            @Override
            public void run() {
                try {
                    rs.movieRec = LenskitRecommender.build(configureMovieRecommender()).getItemRecommender();
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                } catch (RecommenderBuildException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return recs.stream().filter(item -> item.getScore() > 0).map(item -> (int)item.getId()).collect(Collectors.toList());
    }

    public List<Integer> recommendGame(long userId) {
        List<ScoredId> recs = gameRec.recommend(userId, 100);
        return recs.stream().filter(item -> item.getScore() > 0).map(item -> (int)item.getId()).collect(Collectors.toList());
    }

    public List<Integer> recommendBook(long userId) {
        List<ScoredId> recs = bookRec.recommend(userId, 100);
        return recs.stream().filter(item -> item.getScore() > 0).map(item -> (int)item.getId()).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static LenskitConfiguration configureMovieRecommender() {
        LenskitConfiguration config = new LenskitConfiguration();
        config.bind(EventDAO.class)
                .to(MovieRatingDAO.class);
        config.set(RatingFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\ratings.csv"));

        config.bind(ItemDAO.class)
                .to(CSVItemTagDAO.class);
        config.set(TagFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\movie\\movie-tag-copy.csv"));
        config.set(TitleFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\movie\\data-id,name.csv"));

        config.bind(UserDAO.class)
                .to(MOOCUserDAO.class);
        config.set(UserFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\users.csv"));

        config.bind(ItemScorer.class)
                .to(TFIDFItemScorer.class);
        return config;
    }

    @SuppressWarnings("unchecked")
    private static LenskitConfiguration configureGameRecommender() {
        LenskitConfiguration config = new LenskitConfiguration();
        config.bind(EventDAO.class)
                .to(GameRatingDAO.class);
        config.set(RatingFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\ratings.csv"));

        config.bind(ItemDAO.class)
                .to(CSVItemTagDAO.class);
        config.set(TagFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\game\\data-game-id,tag.csv"));
        config.set(TitleFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\game\\data-game-id,name.csv"));

        config.bind(UserDAO.class)
                .to(MOOCUserDAO.class);
        config.set(UserFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\users.csv"));

        config.bind(ItemScorer.class)
                .to(TFIDFItemScorer.class);
        return config;
    }

    @SuppressWarnings("unchecked")
    private static LenskitConfiguration configureBookRecommender() {
        LenskitConfiguration config = new LenskitConfiguration();
        config.bind(EventDAO.class)
                .to(BookRatingDAO.class);
        config.set(RatingFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\ratings.csv"));

        config.bind(ItemDAO.class)
                .to(CSVItemTagDAO.class);
        config.set(TagFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\book\\data-book-id,tag.csv"));
        config.set(TitleFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\book\\data-book-id,name.csv"));

        config.bind(UserDAO.class)
                .to(MOOCUserDAO.class);
        config.set(UserFile.class)
                .to(new File("D:\\Projects\\Impress\\data\\users.csv"));

        config.bind(ItemScorer.class)
                .to(TFIDFItemScorer.class);
        return config;
    }

}
