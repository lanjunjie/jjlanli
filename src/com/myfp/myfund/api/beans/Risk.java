package com.myfp.myfund.api.beans;

public class Risk {
	private String Evaluation,IndexName,ParameterValues;

	public String getEvaluation() {
		return Evaluation;
	}

	public void setEvaluation(String evaluation) {
		Evaluation = evaluation;
	}

	public String getIndexName() {
		return IndexName;
	}

	public void setIndexName(String indexName) {
		IndexName = indexName;
	}

	public String getParameterValues() {
		return ParameterValues;
	}

	public void setParameterValues(String parameterValues) {
		ParameterValues = parameterValues;
	}
}
