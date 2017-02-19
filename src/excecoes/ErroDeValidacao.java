package excecoes;

public class ErroDeValidacao extends Exception {
	private static final long serialVersionUID = -3046248554428949135L;
	private String erro;
	
	public ErroDeValidacao(String erro) {
		this.erro = erro;
	}
	
	public String getErro() {return this.erro;}
}
