package net.service.fieldmanager.servicegroup;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class ServiceGroupRepository {

    private static final String COLLECTION_NAME = "serviceGroups";

    public ServiceGroup save(ServiceGroup serviceGroup) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        String docId = serviceGroup.getId();
        boolean isNew = false;
        if (docId == null || docId.isEmpty()) {
            docId = dbFirestore.collection(COLLECTION_NAME).document().getId();
            serviceGroup.setId(docId);
            isNew = true;
        }

        Map<String, Object> groupData = new HashMap<>();
        groupData.put("groupName", serviceGroup.getGroupName());
        groupData.put("serviceDate", serviceGroup.getServiceDate());
        groupData.put("assignedAreaIds", serviceGroup.getAssignedAreaIds());
        groupData.put("assignedAreaNames", serviceGroup.getAssignedAreaNames());
        groupData.put("members", serviceGroup.getMembers());
        groupData.put("leaderId", serviceGroup.getLeaderId());
        groupData.put("lastModifiedDate", FieldValue.serverTimestamp());

        DocumentReference docRef = dbFirestore.collection(COLLECTION_NAME).document(docId);

        if (isNew) {
            groupData.put("createdDate", FieldValue.serverTimestamp());
            docRef.set(groupData).get();
        } else {
            // Using set() to perform an "upsert". This will create the document if it doesn't exist,
            // or overwrite it if it does. This handles the case where a group is cancelled (deleted)
            // and then confirmed again.
            docRef.set(groupData).get();
        }

        // Note: The returned object will not contain the server-generated timestamps.
        // The controller returns this object to the frontend, which might need adjustment
        // if it relies on the updated timestamps. For now, this ensures data persistence.
        return serviceGroup;
    }

    public List<ServiceGroup> findAllByServiceDate(String serviceDate) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).whereEqualTo("serviceDate", serviceDate).get();
        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(ServiceGroup.class))
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(id).delete();
    }

    public List<ServiceGroup> findAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).get();
        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(ServiceGroup.class))
                .collect(Collectors.toList());
    }
}