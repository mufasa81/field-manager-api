package net.service.fieldmanager.areagroup;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class AreaGroupRepository {

    private static final String COLLECTION_NAME = "areaGroups";

    public AreaGroup save(AreaGroup areaGroup) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        if (areaGroup.getId() == null || areaGroup.getId().isEmpty()) {
            DocumentReference docRef = dbFirestore.collection(COLLECTION_NAME).document();
            areaGroup.setId(docRef.getId());
        }
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COLLECTION_NAME).document(areaGroup.getId()).set(areaGroup);
        collectionsApiFuture.get(); // wait for write to complete
        return areaGroup;
    }

    public Optional<AreaGroup> findById(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return Optional.ofNullable(document.toObject(AreaGroup.class));
        } else {
            return Optional.empty();
        }
    }

    public List<AreaGroup> findAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<AreaGroup> areaGroups = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            areaGroups.add(document.toObject(AreaGroup.class));
        }
        return areaGroups;
    }

    public void deleteById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(id).delete();
    }
}
