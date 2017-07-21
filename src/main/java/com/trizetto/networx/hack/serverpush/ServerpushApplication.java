package com.trizetto.networx.hack.serverpush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	public static List<MyEmitter> ssList = new ArrayList<>();

	@Autowired
	MyService myService;

	@RequestMapping(path = "/stream/{modelId}", method = RequestMethod.GET)
	public SseEmitter stream(@PathVariable String modelId) throws IOException {

		System.out.println("Stream Called!!!");
		MyEmitter emitter = new MyEmitter();
		emitter.setModelId(modelId);
		ssList.add(emitter);

		try {
			myService.getStatus(emitter);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return emitter;
	}

	@RequestMapping(path = "/alerts", method = RequestMethod.GET)
	public SseEmitter alerts() throws IOException {

		System.out.println("Stream Called!!!");
		SseEmitter emitter = new SseEmitter();

		try {
			myService.getAlerts(emitter);
			;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return emitter;
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerpushApplication.class, args);
	}

}
