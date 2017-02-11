package gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrupoUsuarioTableGateway extends TableGateway {

	public GrupoUsuarioTableGateway() throws ClassNotFoundException, SQLException {
		super("grupo_usuario");
	}

	public ResultSet obterTodos() throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.select);
		
		stmt.setString(1, this.getTableName());
		
		if (stmt.execute()) rs = stmt.getResultSet();
		
		return rs;
	}

	public ResultSet obter(int id) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectId);
		
		stmt.setString(1, this.getTableName());
		stmt.setInt(2, id);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}
		
	public ResultSet obterPorGrupo(int idGrupo) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectColumn);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "grupo_id");
		stmt.setInt(3, idGrupo);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}
	
	public ResultSet obterPorUsuario(int idUsuario) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectColumn);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "usuario_id");
		stmt.setInt(3, idUsuario);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}

	public void inserir(int idGrupo, int idUsuario, boolean ativo) 
			throws SQLException {
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.insert);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "grupo_id, usuario_id, ativo");
		
		StringBuilder data = new StringBuilder();
		
		data.append(idGrupo); data.append(", ");
		data.append(idUsuario); data.append(", ");
		data.append(ativo);
		
		stmt.setString(3, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, boolean ativo) 
			throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.updateId);
		
		stmt.setString(1, this.getTableName());
		
		StringBuilder data = new StringBuilder();
		
		data.append("ativo = "); data.append(ativo);
		
		stmt.setString(2, data.toString());
		stmt.setInt(3, id);
		
		stmt.executeUpdate();
	}

	public void excluir(int id) throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.deleteId);
		
		stmt.setString(1, this.getTableName());
		stmt.setInt(2, id);
		
		stmt.executeUpdate();
	}
}
