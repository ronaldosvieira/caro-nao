package dominio;

import java.sql.SQLException;

import dados.GrupoUsuarioTableGateway;
import dados.UsuarioTableGateway;
import excecoes.EmailJaCadastradoException;
import excecoes.UsuarioNaoExisteException;
import util.RecordSet;
import util.Row;

public class GrupoUsuarioModule {
	private RecordSet dataset;
	private GrupoUsuarioTableGateway gutg;
	
	public GrupoUsuarioModule(RecordSet dataset) throws ClassNotFoundException, SQLException {
		this.dataset = dataset;
		this.gutg = new GrupoUsuarioTableGateway();
	}

	public RecordSet listarGruposPorUsuario(int idUsuario) throws SQLException, ClassNotFoundException {
		RecordSet gruposUsuario = gutg.obterPorUsuario(idUsuario);
		
		GrupoModule gm = new GrupoModule(gruposUsuario);
		
		return gm.obterVarios("grupo_id");
	}
	
	public RecordSet listarUsuariosPorGrupo(int idGrupo) throws SQLException, ClassNotFoundException {
		RecordSet usuariosGrupo = gutg.obterPorGrupo(idGrupo);
		
		UsuarioModule um = new UsuarioModule(usuariosGrupo);
		
		return um.obterVarios("usuario_id");
	}
}
