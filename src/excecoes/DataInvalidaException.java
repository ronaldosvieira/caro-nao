package excecoes;

public class DataInvalidaException extends Exception {
	private static final long serialVersionUID = -6115335879515991151L;
	private String data;
	
	public DataInvalidaException(String data) {this.data = data;}
	
	public String getData() {return this.data;}
}
