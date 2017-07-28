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

	@RequestMapping(path = "/stream/{modelId}", method = RequestMethod.GET)
	public SseEmitter stream(@PathVariable String modelId) throws IOException {
		MyEmitter emitter = new MyEmitter();
		emitter.setModelId(modelId);
		sseMap.put(modelId, emitter);

		try {
			myService.getStatus(modelId);
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
	public void notify(@PathVariable String modelId, @PathVariable String percentage) throws IOException {
			sseMap.get(modelId).send(percentage);
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerpushApplication.class, args);
	}

}
