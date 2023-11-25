package com.driver.services.impl;

import com.driver.model.*;
import com.driver.model.Spot;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();
        SpotType spotType = null;
        if(numberOfWheels <= 2){
            spotType = SpotType.TWO_WHEELER;
        }
        else if(numberOfWheels <= 4){
            spotType = SpotType.FOUR_WHEELER;
        }
        else{
            spotType = SpotType.OTHERS;
        }
        spot.setPricePerHour(pricePerHour);
        spot.setSpotType(spotType);

        // get parking lot by id
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);

        // now add this spot to given parking lot
        parkingLot.getSpotList().add(spot);
        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = null;
        for(Spot curSpot : parkingLot.getSpotList()){
            if(curSpot.getId() == spotId){
                curSpot.setPricePerHour(pricePerHour);
                spotRepository1.save(curSpot);
                spot = curSpot;
                break;
            }
        }
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
