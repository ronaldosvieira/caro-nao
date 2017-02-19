package excecoes;

public class ErroDeValidacao extends Exception {
	private static final long serialVersionUID = -3046248554428949135L;
	private String message;
	
	public ErroDeValidacao(String message) {
		this.message = message;
	}
	
	public String getMessage() {return this.message;}
}
