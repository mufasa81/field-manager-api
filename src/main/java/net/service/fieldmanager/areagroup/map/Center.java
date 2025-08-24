package net.service.fieldmanager.areagroup.map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Center {
    private double lat;
    private double lng;

    public Center() {
        // Firestore deserialization requires a no-arg constructor
    }
}

