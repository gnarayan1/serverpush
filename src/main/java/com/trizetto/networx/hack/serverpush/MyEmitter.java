package com.trizetto.networx.hack.serverpush;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class MyEmitter extends SseEmitter {
	String modelId;

	/**
	 * @return the modelId
	 */
	public String getModelId() {
		return modelId;
	}

	/**
	 * @param modelId the modelId to set
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	

}
