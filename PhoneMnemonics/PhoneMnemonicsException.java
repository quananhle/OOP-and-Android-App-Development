public class PhoneMnemonicsException extends Exception {
	public PhoneMnemonicsException() {
		super ("PhoneMnemonics Exception");
	}
	public PhoneMnemonicsException(String message) {
		super (message);
	}
	public String getMessages() {
		return super.getMessage();
	}
}
