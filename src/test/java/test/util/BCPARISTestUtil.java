package test.util;

import ca.bc.gov.iamp.bcparis.model.message.Body;
import ca.bc.gov.iamp.bcparis.model.message.Envelope;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.transformation.MessageTransform;

public class BCPARISTestUtil {

	public static Layer7Message getMessageDriverSNME() {		
		return getMessage("cdata/sample-driver-snme");
	}
	
	public static Layer7Message getMessageDriverDL() {
		return getMessage("cdata/sample-driver-dl");
	}
	
	public static Layer7Message getMessageVehicleVIN() {
		return getMessage("cdata/sample-vehicle-vin");
	}
	
	public static Layer7Message getMessageVehicleLIC() {
		return getMessage("cdata/sample-vehicle-lic");
	}
	
	public static Layer7Message getMessageVehicleRVL() {
		return getMessage("cdata/sample-vehicle-rvl");
	}
	
	private static Layer7Message getMessage(final String cdata) {
		MessageTransform transform = new MessageTransform();
		return transform.parse(Layer7Message.builder()
				.envelope(Envelope.builder().body(getMessageBody(cdata)).build())
				.build());
	}
	
	public static Body getMessageBody(String file) {
		final String msgFFmt = TestUtil.readFile(file);
		final Body body = new Body();
		body.setMsgFFmt(msgFFmt);
		return body;
	}
	
}