package com.trizetto.networx.hack.serverpush;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class MyService {

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("MyAsync");
		executor.initialize();
		return executor;
	}

	@Async
	public void getStatus(String modelId) throws InterruptedException {

		SseEmitter sse = ServerpushApplication.sseMap.get(modelId);
		
		for (int i = 0; i <= 100; i+=5) {
			System.out.println("Current Progress  = " + i);		
			try {				
				sse.send(i);
				Thread.sleep(ThreadLocalRandom.current().nextInt(1, 5)*1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sse.complete();
	}
	
	@Async
	public void getAlerts(SseEmitter sse) throws InterruptedException {
		
		String[] alerts  = {"Model AmericanPhysicians is Complete", "Model SingerOrtho is Complete",	
				"Model LargeTest is taking longer than usual", "Scheduled Purge to run in 3 hours", "RateSheet JaneContract is unsed for 6 months", "Node 2 is heap is 80% full" };
		
		
		Thread.sleep(10000L);
		for (int i = 0; i < alerts.length; i++) {
			System.out.println("Sending Alert " + i);			
			
			try {	
				sse.send(alerts[i]);
				Thread.sleep(10000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sse.complete();

	}

}
