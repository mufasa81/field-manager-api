package net.service.fieldmanager.areagroup.map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapCoordinates {
    private Center center;
    private int level;
    private BoundaryGeoJson boundaryGeoJson;

    public MapCoordinates() {
        // Firestore deserialization requires a no-arg constructor
    }
}

