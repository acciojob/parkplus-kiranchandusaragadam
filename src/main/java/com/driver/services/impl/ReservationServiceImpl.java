package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        // get user
        User user = userRepository3.findById(userId).orElse(null);
        // get parking lot
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).orElse(null);

        if(user == null || parkingLot == null){
            throw new Exception("Cannot make reservation");
        }
        // get the spot as per given criteria
        List<Spot> spotList = parkingLot.getSpotList();
        Spot spot = null;
        int totalPrice = Integer.MAX_VALUE;

        for(Spot curSpot : spotList){
            if(!curSpot.getOccupied()){
                int currTotal = 0;
                if(numberOfWheels == 2){
                    currTotal = timeInHours * curSpot.getPricePerHour();
                }
                else if(numberOfWheels == 4 && !curSpot.getSpotType().toString().equals("TWO_WHEELER")){
                    currTotal = timeInHours * curSpot.getPricePerHour();
                }
                else if(numberOfWheels > 4 && curSpot.getSpotType().toString().equals("OTHERS")){
                    currTotal = timeInHours * curSpot.getPricePerHour();
                }
                if(currTotal != 0 && currTotal < totalPrice){
                    totalPrice = currTotal;
                    spot = curSpot;
                }
            }
        }

        if(spot == null){
            throw new Exception("Cannot make reservation");
        }

        // now make Reservation
        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);
        reservation.setSpot(spot);
        spot.setOccupied(true);

        user.getReservationList().add(reservation);
        spot.getReservationList().add(reservation);

        reservationRepository3.save(reservation);
        return reservation;
    }
}
