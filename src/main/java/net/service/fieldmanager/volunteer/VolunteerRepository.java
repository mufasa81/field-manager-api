package net.service.fieldmanager.volunteer;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class VolunteerRepository {

    private static final String COLLECTION_NAME = "volunteers";

    public Volunteer save(Volunteer volunteer) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        if (volunteer.getId() == null || volunteer.getId().isEmpty()) {
            DocumentReference docRef = dbFirestore.collection(COLLECTION_NAME).document();
            volunteer.setId(docRef.getId());
        }
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COLLECTION_NAME).document(volunteer.getId()).set(volunteer);
        collectionsApiFuture.get();
        return volunteer;
    }

    public Optional<Volunteer> findById(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return Optional.ofNullable(document.toObject(Volunteer.class));
        } else {
            return Optional.empty();
        }
    }

    public List<Volunteer> findAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).get();
        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(Volunteer.class))
                .collect(Collectors.toList());
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public List<Volunteer> findAllByServiceDate(String serviceDate) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).whereEqualTo("serviceDate", serviceDate).get();
        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(Volunteer.class))
                .collect(Collectors.toList());
    }

    public List<Volunteer> findAllByServiceDateAndUserName(String serviceDate, String userName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME)
                .whereEqualTo("serviceDate", serviceDate)
                .whereEqualTo("userName", userName)
                .get();
        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(Volunteer.class))
                .collect(Collectors.toList());
    }

    public List<Volunteer> findAllByServiceDateAndUserNameAndServiceType(String serviceDate, String userName, ServiceType serviceType) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME)
                .whereEqualTo("serviceDate", serviceDate)
                .whereEqualTo("userName", userName)
                .whereEqualTo("serviceType", serviceType)
                .get();
        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(Volunteer.class))
                .collect(Collectors.toList());
    }
}
