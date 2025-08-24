package net.service.fieldmanager.areagroup.map;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoundaryGeoJson {
    private String type;
    private List<List<List<Double>>> coordinates;

    public BoundaryGeoJson() {
        // Firestore deserialization requires a no-arg constructor
    }
}

