package ua.lviv.iot.ParkingServer.logic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.ParkingServer.datastorage.ParkingSpotFileStore;
import ua.lviv.iot.ParkingServer.model.ParkingSpot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Service
public class ParkingSpotService {
    private Map<Long, ParkingSpot> parkingSpots = new HashMap();
    private Long index = 0L;

    @Autowired
    ParkingSpotFileStore parkingSpotFileStore;

    @Autowired
    ParkingService parkingService;


    public List<ParkingSpot> findAllParkingSpots() {
        return new ArrayList<>(parkingSpots.values());
    }

    public ParkingSpot findParkingSpotById(Long parkingSpotId) {
        return parkingSpots.get(parkingSpotId);
    }

    public ParkingSpot addParkingSpot(ParkingSpot parkingSpot) {
        index += 1;
        parkingSpot.setParkingSpotId(index);
        parkingSpots.put(index, parkingSpot);
        return parkingSpot;
    }

    public ParkingSpot updateParkingSpot(Long parkingSpotId, ParkingSpot parkingSpot) {
        parkingSpot.setParkingSpotId(index);
        parkingSpots.put(parkingSpotId, parkingSpot);
        return parkingSpot;
    }

    public ParkingSpot deleteParkingSpot(Long parkingSpotId) {
        return parkingSpots.remove(parkingSpotId);
    }


    @PreDestroy
    private void saveParkingSpotData() throws IOException {
        List<ParkingSpot> list = parkingSpots.values().stream().toList();
        parkingSpotFileStore.saveParkingSpotData(list);
    }

    @PostConstruct
    private void pushParkingSpot() throws IOException {
        if (parkingSpotFileStore.pushParkingSpot() != null) {
            List<ParkingSpot> list = parkingSpotFileStore.pushParkingSpot();
            for (ParkingSpot parkingSpot : list) {
                parkingSpots.put(parkingSpot.getParkingSpotId(), parkingSpot);
            }
        }
    }

}