package com.myfp.myfund.api;

public class CancelException extends Exception {

	private static final long serialVersionUID = 8740567601464736006L;
	public static final int UNKNOW_STATUS = -1;
	public static final int CANCEL_STATUS = 10;

	public CancelException(Exception e) {
		super(e);
	}

	public CancelException(String message) {
		super(message);
	}

	public int getCancelStatus() {
		return CANCEL_STATUS;
	}
}
