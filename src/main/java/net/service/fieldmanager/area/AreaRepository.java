package net.service.fieldmanager.area;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;
import com.google.firebase.FirebaseApp;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class AreaRepository {

    private final Firestore firestore;

    public AreaRepository(FirebaseApp firebaseApp) {
        this.firestore = FirestoreClient.getFirestore(firebaseApp);
    }

    public Area save(Area area) throws ExecutionException, InterruptedException {
        if (area.getId() == null || area.getId().isEmpty()) {
            var docRef = firestore.collection("areas").add(area).get();
            area.setId(docRef.getId());
            return area;
        } else {
            firestore.collection("areas").document(area.getId()).set(area).get();
            return area;
        }
    }

    public Optional<Area> findById(String id) throws ExecutionException, InterruptedException {
        return Optional.ofNullable(firestore.collection("areas").document(id).get().get().toObject(Area.class));
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        firestore.collection("areas").document(id).delete().get();
    }

    public List<Area> findAllByAreaGroupId(String areaGroupId) throws ExecutionException, InterruptedException {
        return firestore.collection("areas").whereEqualTo("areaGroupId", areaGroupId).get().get().getDocuments().stream()
                .map(document -> document.toObject(Area.class))
                .collect(Collectors.toList());
    }

    public int countByAreaGroupId(String areaGroupId) throws ExecutionException, InterruptedException {
        return firestore.collection("areas").whereEqualTo("areaGroupId", areaGroupId).get().get().size();
    }

    public List<Area> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection("areas").get().get().getDocuments().stream()
                .map(document -> document.toObject(Area.class))
                .collect(Collectors.toList());
    }
}
