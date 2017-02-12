package dominio;

import java.sql.SQLException;

import dados.UsuarioTableGateway;
import excecoes.EmailJaCadastradoException;
import util.RecordSet;
import util.Row;

public class UsuarioModule {
	private RecordSet dataset;
	
	public UsuarioModule(RecordSet dataset) {
		this.dataset = dataset;
	}
	
	public void inserirUsuario(String nome, String email, String telefone) throws ClassNotFoundException, SQLException, EmailJaCadastradoException {
		UsuarioTableGateway utg = new UsuarioTableGateway();
		
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
