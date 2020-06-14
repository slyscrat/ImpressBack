package com.slyscrat.impress.service.business.game;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.model.dto.game.GameFullDto;
import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.model.entity.GameEntity;
import com.slyscrat.impress.model.mapper.GameMapper;
import com.slyscrat.impress.model.repository.game.GameRepository;
import com.slyscrat.impress.service.business.AbstractBusinessService;
import com.slyscrat.impress.service.business.SortEnum;
import com.slyscrat.impress.service.crud.game.GameCrudService;
import com.slyscrat.impress.service.crud.game.GameGenreCrudService;
import com.slyscrat.impress.service.crud.game.GameRateCrudService;
import com.slyscrat.impress.service.recommendation.RecommendationSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GameServiceImpl extends AbstractBusinessService implements GameService {
    private final GameCrudService gameCrudService;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final GameGenreCrudService genreCrudService;
    private final GameRateCrudService gameRateCrudService;
    private final RecommendationSystem recommendationSystem;

    private static final String DEVELOPER = "developer";

    @Override
    public void noteDel(Integer gameId, Integer userId) {
        if (!isGamePresent(gameId)) return;
        ItemRateDto itemRateDto = gameRateCrudService.findByUserAndGame(userId, gameId);
        if (itemRateDto != null) {
            itemRateDto.setNote("");
            gameRateCrudService.update(itemRateDto);
        }
    }

    @Override
    public ItemRateDto rate(Integer gameId, Short rate, Integer userId) {
        if (!isGamePresent(gameId)) return null;
        ItemRateDto itemRateDto = gameRateCrudService.findByUserAndGame(userId, gameId);
        if (itemRateDto == null) {
            itemRateDto = new ItemRateDto();
            itemRateDto.setItem(gameId);
            itemRateDto.setUser(userId);
            itemRateDto.setRate(rate);
        }
        else if (itemRateDto.getRate().equals(rate)) {
            if (itemRateDto.getNote().length() == 0) {
                gameRateCrudService.delete(userId, gameId);
                itemRateDto.setUser(0);
                return itemRateDto;
            }
            itemRateDto.setRate((short) 0);
        }
        else if (rate == 0 && itemRateDto.getRate() > 0) { }
        else itemRateDto.setRate(rate);
        return gameRateCrudService.update(itemRateDto);
    }

    @Override
    public ItemRateDto note(Integer gameId, String note, Integer userId) {
        if (!isGamePresent(gameId)) return null;
        ItemRateDto itemRateDto = gameRateCrudService.findByUserAndGame(userId, gameId);
        if (itemRateDto == null) {
            itemRateDto = new ItemRateDto();
            itemRateDto.setItem(gameId);
            itemRateDto.setUser(userId);
        }
        itemRateDto.setNote(note);
        return gameRateCrudService.update(itemRateDto);
    }

    @Override
    public List<GameGenreDto> getGenres() {
        return genreCrudService.getAll();
    }

    private boolean isGamePresent(Integer gameId) {
        return gameRepository.findById(gameId).isPresent();
    }

    @Override
    public List<GameDto> getRecommendedList(Integer page, Integer userId) {
        List<Integer> ids = recommendationSystem.recommendGame(userId);
        int startIndex = page < 0 ? 0 : page * pageSize;
        int endIndex = page < 1 ? pageSize : (page + 1) * pageSize;
        ids = ids.subList(startIndex, endIndex);
        List<GameEntity> games = gameRepository.findAllByIdIn(ids);
        List<Integer> finalIds = ids;
        games.sort(Comparator.comparing(game -> finalIds.indexOf(game.getId())));
        return games
                .stream()
                .map(gameMapper::map)
                .map(this::mapToShort)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDto> getFutureList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        List<GameDto> res = new ArrayList<>();
        gameRepository.findAllByRated_User_Id_AndRated_Rate(userId, (short) 0, paging)
                .forEach(gameEntity -> res.add(mapToFuture(gameMapper.map(gameEntity))));
        return res;
    }

    @Override
    public List<GameDto> getRatedList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        List<GameDto> res = new ArrayList<>();
        gameRepository.findAllByRated_User_Id_AndRated_RateGreaterThan(userId, (short) 0, paging)
                .forEach(gameEntity -> res.add(mapToShort(gameMapper.map(gameEntity), userId)));
        return res;
    }

    @Override
    public List<GameDto> getAllList(Integer page, Integer sort, Set<Integer> genres, Integer userId) {
        Pageable paging;
        switch (checkSort(sort)) {
            case DateASC:
                paging = PageRequest.of(page, pageSize, Sort.by(DEVELOPER).descending());
                break;
            case NameASC:
                paging = PageRequest.of(page, pageSize, Sort.by("name").ascending());
                break;
            case NameDSC:
                paging = PageRequest.of(page, pageSize, Sort.by("name").descending());
                break;
            default:
                paging = PageRequest.of(page, pageSize, Sort.by(DEVELOPER).ascending());
                break;
        }

        if (genres == null || genres.isEmpty())
            return gameCrudService.findAll(paging).stream()
                    .map(item -> mapToShort(item, userId))
                    .collect(Collectors.toList());

        if (genres.size() == 1)
            return gameCrudService.findAllByGenre(genres.iterator().next(), paging).stream()
                    .map(item -> mapToShort(item, userId))
                    .collect(Collectors.toList());

        return gameCrudService.findAllByGenresIds(checkFilters(genres), paging).stream()
                .map(item -> mapToShort(item, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDto> getByNameList(Integer page, String name, Integer userId) {
        if (name == null) return getAllList(page, 0, null, userId);
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(DEVELOPER).descending());
        return gameRepository.findAllByNameContainsIgnoreCase(name, paging).toList().stream()
                .map(gameMapper::map)
                .map(gameDto -> mapToShort(gameDto, userId))
                .collect(Collectors.toList());
    }

    @Override
    public GameDto getById(Integer id, Integer userId) {
        return mapToShort(gameCrudService.findById(id), userId);
    }

    private GameFullDto mapToShort(GameDto gameDto, Integer userId) {
        GameFullDto gameShortDto = new GameFullDto(gameDto);
        ItemRateDto rate = null;
        if (userId != 0)
            rate = gameRateCrudService.findByUserAndGame(userId,gameDto.getId());
        if (rate != null) {
            gameShortDto.setRate(rate.getRate());
            gameShortDto.setFutured(rate.getRate() == 0);
        }
        return gameShortDto;
    }

    private GameFullDto mapToShort(GameDto gameDto) {
        return new GameFullDto(gameDto);
    }

    private GameFullDto mapToFuture(GameDto gameDto) {
        GameFullDto gameShortDto = mapToShort(gameDto);
        gameShortDto.setRate((short) 0);
        gameShortDto.setFutured(true);
        return gameShortDto;
    }

    @Override
    protected Set<Integer> checkFilters(Set<Integer> inGenres) {
        if (inGenres == null || inGenres.isEmpty()) return inGenres;
        Set<Integer> finalGenres = genreCrudService.getIdsSet();
        return inGenres.stream()
                .filter(finalGenres::contains)
                .collect(Collectors.toSet());
    }

    @Override
    protected SortEnum checkSort(Integer sortMethod) {
        return SortEnum.findByKey(sortMethod);
    }
}
