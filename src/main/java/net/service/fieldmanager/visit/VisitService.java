package net.service.fieldmanager.visit;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Visit recordVisit(String areaId, String buildingId, String unitId, String visitorId, String visitorName, int visitNumber) throws ExecutionException, InterruptedException {
        Visit visit = new Visit();
        visit.setAreaId(areaId);
        visit.setBuildingId(buildingId);
        visit.setUnitId(unitId);
        visit.setVisitorId(visitorId);
        visit.setVisitorName(visitorName);
        visit.setVisitDate(new Date());
        visit.setVisitNumber(visitNumber);
        return visitRepository.save(visit);
    }

    public List<Visit> getVisitsForBuilding(String areaId, String buildingId) throws ExecutionException, InterruptedException {
        return visitRepository.findVisitsByAreaIdAndBuildingId(areaId, buildingId);
    }

    public Optional<Visit> getVisitForUnit(String buildingId, String unitId, int visitNumber) throws ExecutionException, InterruptedException {
        return visitRepository.findVisitsByBuildingIdAndUnitIdAndVisitNumber(buildingId, unitId, visitNumber);
    }
}
