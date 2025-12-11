package net.service.fieldmanager.user;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class User {

    private String Id;
    private String name;
    private String email;
    private String password;
    private Instant createdDate;
    private Role role;
    private boolean active = true;

    // Firestore가 직렬화/역직렬화할 때 사용할 헬퍼 메소드
    public Timestamp getCreatedDateAsTimestamp() {
        return createdDate != null ? Timestamp.of(Date.from(createdDate)) : null;
    }

    public void setCreatedDateAsTimestamp(Timestamp timestamp) {
        this.createdDate = timestamp != null ? timestamp.toDate().toInstant() : null;
    }
}
