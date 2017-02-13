package dominio;

import java.sql.SQLException;

import dados.GrupoUsuarioTableGateway;
import dados.UsuarioTableGateway;
import excecoes.EmailJaCadastradoException;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.GrupoUsuarioNaoExisteException;
import excecoes.UsuarioNaoExisteException;
import util.RecordSet;
import util.Row;

public class GrupoUsuarioModule {
	private GrupoUsuarioTableGateway gutg;
	
	public GrupoUsuarioModule() throws ClassNotFoundException, SQLException {
		this.gutg = new GrupoUsuarioTableGateway();
	}
	
	private RecordSet obter(int id) throws GrupoUsuarioNaoExisteException, SQLException {
		RecordSet grupoUsuario = gutg.obter(id);
		
		if (grupoUsuario.isEmpty()) {
			throw new GrupoUsuarioNaoExisteException();
		}
		
		return grupoUsuario;
	}
	
	public RecordSet obterGrupoUsuarioPorUsuario(int idUsuario) throws SQLException {
		return gutg.obterPorUsuario(idUsuario);
	}
	
	public RecordSet obterGrupoUsuarioPorGrupo(int idGrupo) throws SQLException {
		return gutg.obterPorGrupo(idGrupo);
	}

	public RecordSet listarGruposPorUsuario(int idUsuario) throws SQLException, ClassNotFoundException {
		RecordSet gruposUsuario = gutg.obterPorUsuario(idUsuario);
		
		GrupoModule gm = new GrupoModule();
		
		return gm.obterVarios("grupo_id", gruposUsuario);
	}
	
	public RecordSet listarUsuariosPorGrupo(int idGrupo) throws SQLException, ClassNotFoundException {
		RecordSet usuariosGrupo = gutg.obterPorGrupo(idGrupo);
		
		UsuarioModule um = new UsuarioModule();
		
		return um.obterVarios("usuario_id", usuariosGrupo);
	}
	
	public int inserirGrupoUsuario(int idGrupo, int idUsuario) throws SQLException, GrupoUsuarioJaExisteException {
		RecordSet jaExiste = gutg.obter(idGrupo, idUsuario);
		
		if (!jaExiste.isEmpty()) {
			throw new GrupoUsuarioJaExisteException();
		}
		
		Row grupoUsuario = new Row();
		
		grupoUsuario.put("grupo_id", idGrupo);
		grupoUsuario.put("usuario_id", idUsuario);
		grupoUsuario.put("ativo", true);
		
		return gutg.inserir(idGrupo, idUsuario, true);
	}

	public void desativarGrupoUsuario(int id) throws SQLException, GrupoUsuarioNaoExisteException {
		RecordSet grupoUsuario = this.obter(id);
		
		for (Row gU : grupoUsuario) {
			gutg.atualizar(gU.getInt("id"), false);
		}
	}
}
