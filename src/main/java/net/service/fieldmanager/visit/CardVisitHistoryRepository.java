package net.service.fieldmanager.visit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class CardVisitHistoryRepository {

    private static final String COLLECTION_NAME = "card-visit-history";

    public CardVisitHistory save(CardVisitHistory visitHistory) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        if (visitHistory.getId() == null || visitHistory.getId().isEmpty()) {
            DocumentReference docRef = dbFirestore.collection(COLLECTION_NAME).document();
            visitHistory.setId(docRef.getId());
        }
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COLLECTION_NAME).document(visitHistory.getId()).set(visitHistory);
        collectionsApiFuture.get();
        return visitHistory;
    }

    public List<CardVisitHistory> findByAreaId(String areaId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).get();
        
        List<CardVisitHistory> allHistories = future.get().getDocuments().stream()
                .map(doc -> doc.toObject(CardVisitHistory.class))
                .collect(Collectors.toList());

        return allHistories.stream()
                .filter(h -> areaId.equals(h.getAreaId()))
                .sorted(Comparator.comparing(CardVisitHistory::getVisitDate).reversed())
                .collect(Collectors.toList());
    }

    public List<CardVisitHistory> findByBuildingId(String buildingId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).get();

        List<CardVisitHistory> allHistories = future.get().getDocuments().stream()
                .map(doc -> doc.toObject(CardVisitHistory.class))
                .collect(Collectors.toList());

        return allHistories.stream()
                .filter(h -> buildingId.equals(h.getBuildingId()))
                .sorted(Comparator.comparing(CardVisitHistory::getVisitDate).reversed())
                .collect(Collectors.toList());
    }

    public void deleteById(String visitId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(visitId).delete().get();
    }
}
