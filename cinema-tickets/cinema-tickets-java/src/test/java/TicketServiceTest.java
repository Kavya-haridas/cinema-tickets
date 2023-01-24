import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceTest {

	TicketService ts = new TicketServiceImpl();

	long accountId = 12345678910L;

	@Test
	public void testFailureScenarioWithChildAndInfantWithoutAdult() {
		try {
			ts.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2),
					new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
		} catch (final InvalidPurchaseException e) {
			final String msg = "Sorry!!..A child/infant cannot watch the movie unaccompanied";
			assertEquals(msg, e.getMessage());
		}
	}

	@Test
	public void testFailureScenarioWithOnlyChild() {

		try {
			ts.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
		} catch (final InvalidPurchaseException e) {
			final String msg = "Sorry!!..A child/infant cannot watch the movie unaccompanied";
			assertEquals(msg, e.getMessage());
		}
	}

	@Test
	public void testFailureScenarioWithOnlyInfant() {
		try {
			ts.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1));
		} catch (final InvalidPurchaseException e) {
			final String msg = "Sorry!!..A child/infant cannot watch the movie unaccompanied";
			assertEquals(msg, e.getMessage());
		}
	}

	@Test
	public void testFailureDonotExceed20() {
		try {
			ts.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2),
					new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 4),
					new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 16));
		} catch (final InvalidPurchaseException e) {
			final String msg = "Sorry!!..You cannot purchase more than 20 tickets";
			assertEquals(msg, e.getMessage());
		}
	}
	
	@Test
	public void testFailureScenarioInvalidTestRequest() {
		try {
			ts.purchaseTickets(accountId, null);
		} catch (final InvalidPurchaseException e) {
			final String msg = "Invalid Purchase Request!!!";
			assertEquals(msg, e.getMessage());
		}
	}

	@Test
	public void testSuccessScenarioWithChildAndParent() {
		ts.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
				new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
	}

	@Test
	public void testSuccessScenarioWithInfantAndParent() {
		ts.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
				new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1));
	}

	@Test
	public void testSuccessScenarioWithInfantAndChildAndParent() {
		ts.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
				new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
				new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2));
	}

}
