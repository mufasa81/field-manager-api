package net.service.fieldmanager.area;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class AreaRepository {

    private static final String COLLECTION_NAME = "areas";

    public Area save(Area area) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        if (area.getId() == null || area.getId().isEmpty()) {
            DocumentReference docRef = dbFirestore.collection(COLLECTION_NAME).document();
            area.setId(docRef.getId());
        }
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COLLECTION_NAME).document(area.getId()).set(area);
        collectionsApiFuture.get(); // wait for write to complete
        return area;
    }

    public Optional<Area> findById(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return Optional.ofNullable(dbFirestore.collection(COLLECTION_NAME).document(id).get().get().toObject(Area.class));
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public List<Area> findAllByAreaGroupId(String areaGroupId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore.collection(COLLECTION_NAME).whereEqualTo("areaGroupId", areaGroupId).get().get().getDocuments().stream()
                .map(document -> document.toObject(Area.class))
                .collect(Collectors.toList());
    }

    public int countByAreaGroupId(String areaGroupId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore.collection(COLLECTION_NAME).whereEqualTo("areaGroupId", areaGroupId).get().get().size();
    }

    public List<Area> findAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore.collection(COLLECTION_NAME).get().get().getDocuments().stream()
                .map(document -> document.toObject(Area.class))
                .collect(Collectors.toList());
    }
}
