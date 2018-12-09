package br.com.linkcom.wms.util;

public class WmsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WmsException() {
		super();
	}

	public WmsException(String message, Throwable cause) {
		super(message, cause);
	}

	public WmsException(String message) {
		super(message);
	}

	public WmsException(Throwable cause) {
		super(cause);
	}

}
