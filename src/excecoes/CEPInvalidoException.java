package excecoes;

public class CEPInvalidoException extends Exception {
	private static final long serialVersionUID = 1106452320882914220L;
	private String cep;
	
	public CEPInvalidoException(String cep) {this.cep = cep;}
	
	public String getCep() {return this.cep;}
}
