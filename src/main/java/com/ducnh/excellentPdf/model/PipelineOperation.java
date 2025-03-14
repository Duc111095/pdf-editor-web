package com.ducnh.excellentPdf.model;

import java.util.Map;

public class PipelineOperation {
	private String operation;
	private Map<String, Object> parameters;
	
	public String getOperation() {
		return this.operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public Map<String, Object> getParameters() {
		return this.parameters;
	}
	
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public String toString() {
		return "PipelineOperation [operation=" + operation + ", parameters=" + parameters + "]";
	}
}
