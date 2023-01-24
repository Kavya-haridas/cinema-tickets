package uk.gov.dwp.uc.pairtest;

import java.util.HashMap;
import java.util.Map;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
	/**
	 * Should only have private methods other than the one below.
	 */
	private static final int infantCost = 0, childCost = 10, adultCost = 20;
	
	private static final String TOTAL_SEATS = "TOTAL_SEATS";
	
	private static final String TOTAL_NUMBER_OF_TICKETS = "TOTAL_NUMBER_OF_TICKETS";
	
	private String errorMessage;
	
	TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();
	
	SeatReservationService seatReservationService = new SeatReservationServiceImpl();

	@Override
	public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
			throws InvalidPurchaseException {
		// Validation checks
		if (isPurchaseRequestValid(ticketTypeRequests)) {
			// if all validations passed
			Map<String, Integer> resultMap = getTicketNumberAndTotalCount(ticketTypeRequests);
			int totalTicketCost = resultMap.get(TOTAL_NUMBER_OF_TICKETS);
			int totalSeat = resultMap.get(TOTAL_SEATS);
			ticketPaymentService.makePayment(accountId, totalTicketCost);
			seatReservationService.reserveSeat(accountId, totalSeat);
			this.setErrorMessage("Tickets Successfully Booked");
		} else {
			System.out.println(this.getErrorMessage());
			throw new InvalidPurchaseException(this.getErrorMessage());
		}

	}

	private boolean isPurchaseRequestValid(TicketTypeRequest... ticketTypeRequests) {
		int totalSeat = 0, adult = 0, child = 0, infant = 0;

		if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
			this.setErrorMessage("Invalid Purchase Request!!!");
			return false;
		} else {
			for (TicketTypeRequest data : ticketTypeRequests) {
				totalSeat = totalSeat + data.getNoOfTickets();
				switch (data.getTicketType()) {
				case INFANT:
					infant = infant + data.getNoOfTickets();
					break;
				case CHILD:
					child = child + data.getNoOfTickets();
					break;
				default:
					adult = adult + data.getNoOfTickets();
				}
			}
			if ((child >= 1 && adult == 0) || (infant >= 1 && adult == 0)) {
				this.setErrorMessage("Sorry!!..A child/infant cannot watch the movie unaccompanied");
				return false;
			}
			if (totalSeat > 20) {
				this.setErrorMessage("Sorry!!..You cannot purchase more than 20 tickets");
				return false;
			}
		}
		return true;
	}

	private Map<String, Integer> getTicketNumberAndTotalCount(TicketTypeRequest... ticketTypeRequests) {
		Map<String, Integer> resultMap = new HashMap<>();
		int totalSeat = 0;
		int totalTicketCost, adult = 0, child = 0, infant = 0;
		for (TicketTypeRequest data : ticketTypeRequests) {
			totalSeat = totalSeat + data.getNoOfTickets();
			switch (data.getTicketType()) {
			case INFANT:
				infant = infant + data.getNoOfTickets();
				break;
			case CHILD:
				child = child + data.getNoOfTickets();
				break;
			default:
				adult = adult + data.getNoOfTickets();
			}
		}
		
		totalTicketCost = (adult * adultCost) + (child * childCost) + (infant * infantCost);
		resultMap.put(TOTAL_SEATS, totalSeat);
		resultMap.put(TOTAL_NUMBER_OF_TICKETS, totalTicketCost);
		return resultMap;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
