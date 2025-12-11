package net.service.fieldmanager.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class CardVisitHistoryService {

    private final CardVisitHistoryRepository visitHistoryRepository;

    public CardVisitHistory createVisitHistory(CardVisitHistory visitHistory) throws ExecutionException, InterruptedException {
        return visitHistoryRepository.save(visitHistory);
    }

    public List<CardVisitHistory> getVisitHistoryByArea(String areaId) throws ExecutionException, InterruptedException {
        return visitHistoryRepository.findByAreaId(areaId);
    }

    public List<CardVisitHistory> getVisitHistoryByBuilding(String buildingId) throws ExecutionException, InterruptedException {
        return visitHistoryRepository.findByBuildingId(buildingId);
    }

    public void deleteVisitHistory(String visitId) throws ExecutionException, InterruptedException {
        visitHistoryRepository.deleteById(visitId);
    }
}
