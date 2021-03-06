package ca.bc.gov.iamp.bcparis.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.datagram.DriverProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.PORProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.SatelliteProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.VehicleProcessor;
import test.util.BCPARISTestUtil;

public class DispatcherServiceTest {

	@InjectMocks
	private DispatcherService service = new DispatcherService();
	
	@Mock
	private DriverProcessor driver;
	
	@Mock
	private VehicleProcessor vehicle;
	
	@Mock
	private PORProcessor por;
	
	@Mock
	private SatelliteProcessor satellite;
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void dispatch_driver_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageDriverSNME();
		service.dispatch(message);
		Mockito.verify(driver, Mockito.times(1)).process(message);
	}
	
	@Test
	public void dispatch_vehicle_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleLIC();
		service.dispatch(message);
		Mockito.verify(vehicle, Mockito.times(1)).process(message);
	}
	
	@Test
	public void dispatch_por_success() {
		final Layer7Message message = BCPARISTestUtil.getMessagePOR();
		service.dispatch(message);
		Mockito.verify(por, Mockito.times(1)).process(message);
	}
	
	@Test
	public void dispatch_satellite_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageSatelliteRoundTrip();
		service.dispatch(message);	
		Mockito.verify(satellite, Mockito.times(1)).process(message);
	}
	
}
