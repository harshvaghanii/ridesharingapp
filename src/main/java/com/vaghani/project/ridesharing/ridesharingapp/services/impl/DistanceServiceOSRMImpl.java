package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    private static String OSRM_API_BASE_URL = "https://router.project-osrm.org/route/v1/driving/13.388860,52.517037;13.397634,52.529407";

    @Override
    public double calculateDistance(Point src, Point dest) {
        try {
            String uri = STR."\{src.getX()},\{src.getY()};\{dest.getX()},\{dest.getY()}";
            OSRMResponseDto responseDto = RestClient.builder()
                    .baseUrl(OSRM_API_BASE_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDto.class);
            return responseDto.getRoutes().getFirst().getDistance() / 1000.0;
        } catch (Exception exception) {
            throw new RuntimeException(STR."Error getting data from OSMR \{exception.getMessage()}");
        }
    }
}

@Data
class OSRMResponseDto {
    private List<OSRMRoute> routes;
}

@Data
class OSRMRoute {
    private Double distance;

}
