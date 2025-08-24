package net.service.fieldmanager.areagroup;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapCoordinates {
    private LatLng center;
    private int level;
    private GeoJson boundaryGeoJson;

    public MapCoordinates() { /* No-arg constructor for Firestore */ }

    @Getter
    @Setter
    public static class LatLng {
        private double lat;
        private double lng;

        public LatLng() { /* No-arg constructor for Firestore */ }
    }

    @Getter
    @Setter
    public static class GeoJson {
        private String type; // e.g., "Polygon"
        private List<List<List<Double>>> coordinates; // GeoJSON Polygon format

        public GeoJson() { /* No-arg constructor for Firestore */ }
    }
}
