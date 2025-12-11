package net.service.fieldmanager.visit;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class VisitRepository {

    private final Firestore firestore;

    public VisitRepository(FirebaseApp firebaseApp) {
        this.firestore = FirestoreClient.getFirestore(firebaseApp);
    }

    public Visit save(Visit visit) throws ExecutionException, InterruptedException {
        if (visit.getVisitId() == null || visit.getVisitId().isEmpty()) {
            var docRef = firestore.collection("visits").document();
            visit.setVisitId(docRef.getId());
            docRef.set(visit).get();
            return visit;
        } else {
            firestore.collection("visits").document(visit.getVisitId()).set(visit).get();
            return visit;
        }
    }

    public List<Visit> findVisitsByAreaIdAndBuildingId(String areaId, String buildingId) throws ExecutionException, InterruptedException {
        return firestore.collection("visits")
                .whereEqualTo("areaId", areaId)
                .whereEqualTo("buildingId", buildingId)
                .get().get().getDocuments().stream()
                .map(document -> document.toObject(Visit.class))
                .collect(Collectors.toList());
    }

    public Optional<Visit> findVisitsByBuildingIdAndUnitIdAndVisitNumber(String buildingId, String unitId, int visitNumber) throws ExecutionException, InterruptedException {
        return firestore.collection("visits")
                .whereEqualTo("buildingId", buildingId)
                .whereEqualTo("unitId", unitId)
                .whereEqualTo("visitNumber", visitNumber)
                .limit(1)
                .get().get().getDocuments().stream()
                .map(document -> document.toObject(Visit.class))
                .findFirst();
    }
}
