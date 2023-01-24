package thirdparty.seatbooking;

public class SeatReservationServiceImpl implements SeatReservationService {

    @Override
    public void reserveSeat(long accountId, int totalSeatsToAllocate) {
        // Real implementation omitted, assume working code will make the seat reservation.
		System.out.println("Seats reserved Successfully for accountId :" + accountId + "  with total Seats = "+ totalSeatsToAllocate);

    }

}
