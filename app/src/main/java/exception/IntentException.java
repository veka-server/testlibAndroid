package exception;

public class IntentException extends Exception {

	private static final long serialVersionUID = -9154471528565108902L;
		
	public IntentException(String message) {
		super(message);
	}

	public IntentException() {
		this("This intent cannot be reached");
	}

}
