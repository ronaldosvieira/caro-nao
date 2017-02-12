package dominio;

import java.sql.SQLException;

import dados.UsuarioTableGateway;
import excecoes.EmailJaCadastradoException;
import excecoes.UsuarioNaoExisteException;
import util.RecordSet;
import util.Row;

public class UsuarioModule {
	private RecordSet dataset;
	private UsuarioTableGateway utg;
	
	public UsuarioModule(RecordSet dataset) throws ClassNotFoundException, SQLException {
		this.dataset = dataset;
		this.utg = new UsuarioTableGateway();
	}
	
	public RecordSet autenticar(String email) throws SQLException, UsuarioNaoExisteException {
		RecordSet usuario = utg.obterPeloEmail(email);
		
		if (usuario.size() > 0) return usuario;
		else throw new UsuarioNaoExisteException();
	}
	
	public void inserirUsuario(String nome, String email, String telefone) throws EmailJaCadastradoException, IndexOutOfBoundsException, SQLException {
		RecordSet jaExiste = utg.obterPeloEmail(email);
		
		if (jaExiste.size() > 0) {
			throw new EmailJaCadastradoException();
		}
		
		Row usuario = new Row();
		
		usuario.put("nome", nome);
		usuario.put("email", email);
		usuario.put("telefone", telefone);
		
		dataset.add(usuario);
		
		utg.inserir(nome, email, telefone);
	}
}
