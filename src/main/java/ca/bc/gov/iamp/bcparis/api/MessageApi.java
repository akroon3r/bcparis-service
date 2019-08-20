package ca.bc.gov.iamp.bcparis.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.MessageProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.SatelliteProcessor;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;
import ca.bc.gov.iamp.bcparis.service.SatelliteService;

@RestController
@RequestMapping("/api/v1/message")
public class MessageApi {

	private final Logger log = LoggerFactory.getLogger(MessageApi.class);

	@Autowired
	private MessageProcessor processor;
	
	@Autowired
	private Layer7MessageRepository repository;
	
	@Autowired
	private SatelliteProcessor satellite;
	
	@Autowired
	private SatelliteService satelliteService;
	
	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<Layer7Message> message( @RequestBody Layer7Message message ){
		
		Layer7Message messageResponse = onMessage(message);
		
		checkSatelliteMessage(messageResponse);
		
		return ResponseEntity.ok(messageResponse);
	}
	
	private Layer7Message onMessage(Layer7Message messageContent) {
		
		log.info("Message Received:\n" + messageContent);
		
		return processor.processMessage(messageContent);
	}
	
	private void checkSatelliteMessage(Layer7Message messageResponse) {
		final String text = messageResponse.getEnvelope().getBody().getCDATAAttribute("TEXT");
		if(text.startsWith("BCPARIS Diagnostic Test")) {
			String date = text.substring(text.indexOf("qwe") + 3);
			log.info("(Satellite) Execution time: " + satelliteService.calculateExecutionTime(date));
		}
	}
	
	//Only for test purpose
	@PostMapping( path="/test/layer7", consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<String> testPutMessageLayer7( @RequestBody Layer7Message message ){
		final String response = repository.sendMessage(message);
		return ResponseEntity.ok(response);
	}
	
	//Only for test purpose
//	@PostMapping( path="/test/satellite/vehicle", consumes=MediaType.APPLICATION_JSON_VALUE)
//	private ResponseEntity<String> testSatelliteVehicle( @RequestBody String message ){
//		satellite.sendVehicleMessages();
//		return ResponseEntity.ok("Satellite flow doesn`t return response.");
//	}
	
	//Only for test purpose
	@PostMapping( path="/test/satellite/driver", consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<String> testSatelliteDriver( @RequestBody String message ){
		satellite.sendDriverMessages();
		return ResponseEntity.ok("Sent to MQ.");
	}
}
