package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RecordSet;
import util.Row;

public class GrupoUsuarioTableGateway extends TableGateway {

	public GrupoUsuarioTableGateway() throws ClassNotFoundException, SQLException {
		super("grupo_usuario");
	}

	public RecordSet obterTodos() throws SQLException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.select, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("grupo_id", rs.getInt("grupo_id"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			row.put("ativo", rs.getBoolean("ativo"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public RecordSet obter(int id) throws SQLException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectId, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("grupo_id", rs.getInt("grupo_id"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			row.put("ativo", rs.getBoolean("ativo"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public RecordSet obter(int idGrupo, int idUsuario) throws SQLException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectMany, 
				this.getTableName(), "grupo_id = ? and usuario_id = ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idGrupo);
		stmt.setInt(2, idUsuario);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("grupo_id", rs.getInt("grupo_id"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			row.put("ativo", rs.getBoolean("ativo"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
		
	public RecordSet obterPorGrupo(int idGrupo) throws SQLException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "grupo_id");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);

		stmt.setInt(1, idGrupo);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("grupo_id", rs.getInt("grupo_id"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			row.put("ativo", rs.getBoolean("ativo"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public RecordSet obterPorUsuario(int idUsuario) throws SQLException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "usuario_id");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idUsuario);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("grupo_id", rs.getInt("grupo_id"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			row.put("ativo", rs.getBoolean("ativo"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public int inserir(int idGrupo, int idUsuario, boolean ativo) 
			throws SQLException {
		String sql = String.format(this.insert, 
				this.getTableName(), 
				"grupo_id, usuario_id, ativo",
				"?, ?, ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setInt(1, idGrupo);
		stmt.setInt(2, idUsuario);
		stmt.setBoolean(3, ativo);
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir relação entre grupo e usuário.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir relação entre grupo e usuário.");
            }
        }
	}

	public void atualizar(int id, boolean ativo) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, 
				this.getTableName(), "ativo = ?");
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);

		stmt.setBoolean(1, ativo);
		stmt.setInt(2, id);
		
		stmt.executeUpdate();
	}

	public void excluir(int id) throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.deleteId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		stmt.executeUpdate();
	}
}
