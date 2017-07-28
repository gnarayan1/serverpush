package com.trizetto.networx.hack.serverpush;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@SpringBootApplication
@EnableAsync
@Controller
public class ServerpushApplication {

	public static Map<String, MyEmitter> sseMap = new HashMap<>();

	@Autowired
	MyService myService;

	@RequestMapping(path = "/stream/{modelDefintionId}", method = RequestMethod.GET)
	public SseEmitter stream(@PathVariable String modelDefintionId) throws IOException {
		System.out.println("Calling Notify for ModelDefinitionId: "+modelDefintionId);
		MyEmitter emitter = new MyEmitter();
		emitter.setModelId(modelDefintionId);
		sseMap.put(modelDefintionId, emitter);

		try {
			myService.getStatus(modelDefintionId);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return emitter;
	}

	@RequestMapping(path = "/alerts", method = RequestMethod.GET)
	public SseEmitter alerts() throws IOException {
		SseEmitter emitter = new SseEmitter();

		try {
			myService.getAlerts(emitter);
			;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return emitter;
	}
	
	
	@RequestMapping(path = "/notify/{modelId}/{percentage}", method = RequestMethod.GET)
	public void notify(@PathVariable String modelDefinitionId, @PathVariable String percentage) throws IOException {
		System.out.println("Calling Notify for ModelDefinitionId: "+modelDefinitionId+ " Percentage ="+percentage);
		if (sseMap.get(modelDefinitionId) != null) {
			sseMap.get(modelDefinitionId).send(percentage);
		}
			
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerpushApplication.class, args);
	}

}
