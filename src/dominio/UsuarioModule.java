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
	}
	
	public void armazenar() throws IndexOutOfBoundsException, SQLException {
		for (int i = 0; i < dataset.size(); ++i) {
			Row usuario = dataset.get(i);
			boolean jaExiste = usuario.containsKey("id");
			
			if (jaExiste) {System.out.println("atualizar " + usuario.getString("nome"));
				utg.atualizar(usuario.getInt("id"), 
						usuario.getString("nome"), 
						usuario.getString("email"), 
						usuario.getString("telefone"));
			} else {System.out.println("inserir " + usuario.getString("nome"));
				int id = utg.inserir(usuario.getString("nome"), 
						usuario.getString("email"), 
						usuario.getString("telefone"));
				
				usuario.put("id", id);
				dataset.set(i, usuario);
			}
		}
	}
}
