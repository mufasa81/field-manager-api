package net.service.fieldmanager.building;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class BuildingRepository {

    private static final String COLLECTION_NAME = "buildings";

    public Building save(Building building) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        if (building.getId() == null || building.getId().isEmpty()) {
            DocumentReference docRef = dbFirestore.collection(COLLECTION_NAME).document();
            building.setId(docRef.getId());
        }
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COLLECTION_NAME).document(building.getId()).set(building);
        collectionsApiFuture.get();
        return building;
    }

    public Optional<Building> findById(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return Optional.ofNullable(document.toObject(Building.class));
        } else {
            return Optional.empty();
        }
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public List<Building> findAllByAreaId(String areaId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).whereEqualTo("areaId", areaId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Building> buildings = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            buildings.add(document.toObject(Building.class));
        }
        return buildings;
    }
}
