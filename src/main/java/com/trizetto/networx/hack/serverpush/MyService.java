package com.trizetto.networx.hack.serverpush;

import java.util.concurrent.Executor;

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
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("MyAsync");
		executor.initialize();
		return executor;
	}

	@Async
	public void getStatus(SseEmitter sse) throws InterruptedException {

		for (int i = 0; i < 100; i++) {
			System.out.println("Calling for i" + i);
			Thread.sleep(1000L);
			try {
				
				sse.send("Value of i is " + i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
