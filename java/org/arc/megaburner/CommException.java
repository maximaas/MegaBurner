/**
 * Communication Exception
 * @author mingzo@gmail.com
 * @since 2017/11/10
 */
package org.arc.megaburner;

public class CommException extends Exception {

	private static final long serialVersionUID = -8918877138543664041L;

	public CommException() {
		super();
	}
	
	public CommException(String msg) {
		super(msg);
	}
}
