package br.com.linkcom.wms.util;

public class InsercaoInvalidaException extends Exception {

	private static final long serialVersionUID = 1L;

	public InsercaoInvalidaException() {
		super();
	}

	public InsercaoInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsercaoInvalidaException(String message) {
		super(message);
	}

	public InsercaoInvalidaException(Throwable cause) {
		super(cause);
	}

}
