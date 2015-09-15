package com.myfp.httpmime_lib;

public interface MultipartBodyPart {

	public abstract String getName();

	public abstract ContentBody getBody();

	public abstract Header getHeader();

	public abstract void addField(final String name, final String value);

}