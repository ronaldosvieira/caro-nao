package dominio;

import java.sql.SQLException;

import dados.UsuarioTableGateway;
import excecoes.EmailJaCadastradoException;
import excecoes.GrupoNaoAutorizadoException;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.UsuarioNaoExisteException;
import excecoes.VeiculoNaoAutorizadoException;
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
	
	public RecordSet obter(int id) throws SQLException {
		return this.utg.obter(id);
	}
	
	public RecordSet obterVarios(String column) throws SQLException {
		RecordSet resultado = new RecordSet();
		
		for (Row row : this.dataset) {
			if (!row.containsKey(column)) continue;
			
			Row usuario = this.utg.obter(row.getInt(column)).get(0);
			
			resultado.add(usuario);
		}
		
		return resultado;
	}
	
	public RecordSet listarGrupos(int id) throws SQLException, ClassNotFoundException {
		GrupoUsuarioModule gum = new GrupoUsuarioModule(new RecordSet());
		
		return gum.listarGruposPorUsuario(id);
	}
	
	public RecordSet listarVeiculos(int id) throws SQLException, ClassNotFoundException {
		RecordSet usuario = this.obter(id);
		
		VeiculoModule vm = new VeiculoModule(usuario);
		
		return vm.obterVariosPorUsuario("id");
	}
	
	public int inserirUsuario(String nome, String email, String telefone) throws EmailJaCadastradoException, SQLException {
		RecordSet jaExiste = utg.obterPeloEmail(email);
		
		if (!jaExiste.isEmpty()) {
			throw new EmailJaCadastradoException();
		}
		
		Row usuario = new Row();
		
		usuario.put("nome", nome);
		usuario.put("email", email);
		usuario.put("telefone", telefone);
		
		dataset.add(usuario);
		
		return utg.inserir(nome, email, telefone);
	}
	
	public void atualizarUsuario(int id, String nome, String telefone) throws SQLException, UsuarioNaoExisteException {
		RecordSet jaExiste = utg.obter(id);
		
		if (jaExiste.isEmpty()) {
			throw new UsuarioNaoExisteException();
		}
		
		Row usuario = jaExiste.get(0);
		
		usuario.put("nome", nome);
		usuario.put("telefone", telefone);
		
		if (dataset.contains("id", id)) {
			dataset.set(dataset.find("id", id), usuario);
		} else {
			dataset.add(usuario);
		}
		
		utg.atualizar(id, nome, usuario.getString("email"), telefone);
	}
	
	public RecordSet validarVeiculo(int id, int idVeiculo) throws ClassNotFoundException, SQLException, VeiculoNaoAutorizadoException {
		RecordSet veiculosDoUsuario = this.listarVeiculos(id);
		RecordSet veiculo = new RecordSet();
		
		for (Row veiculoDoUsuario : veiculosDoUsuario) {
			if (veiculoDoUsuario.getInt("id") == idVeiculo) {
				veiculo.add(veiculoDoUsuario);
				
				break;
			}
		}
		
		if (veiculo.isEmpty()) throw new VeiculoNaoAutorizadoException();
		else return veiculo;
	}
	
	public RecordSet validarGrupo(int id, int idGrupo) throws GrupoNaoAutorizadoException, ClassNotFoundException, SQLException {
		RecordSet gruposDoUsuario = this.listarGrupos(id);
		RecordSet grupo = new RecordSet();
		
		for (Row grupoDoUsuario : gruposDoUsuario) {
			if (grupoDoUsuario.getInt("id") == idGrupo) {
				grupo.add(grupoDoUsuario);
				
				break;
			}
		}
		
		if (grupo.isEmpty()) throw new GrupoNaoAutorizadoException();
		else return grupo;
	}

	public void convidarUsuario(String email, int idGrupo) 
			throws SQLException, UsuarioNaoExisteException, 
			ClassNotFoundException, GrupoUsuarioJaExisteException {
		RecordSet usuario = utg.obterPeloEmail(email);
		
		if (usuario.isEmpty()) {
			throw new UsuarioNaoExisteException();
		}
		
		GrupoUsuarioModule gum = new GrupoUsuarioModule(new RecordSet());
		
		gum.inserirGrupoUsuario(idGrupo, 
				usuario.get(0).getInt("id"));
	}
}
