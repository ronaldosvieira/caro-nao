package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RecordSet;
import util.Row;

public class CaronaUsuarioTableGateway extends TableGateway {

	public CaronaUsuarioTableGateway() throws ClassNotFoundException, SQLException {
		super("carona_usuario");
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
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
		
	public RecordSet obterPorCarona(int idCarona) throws SQLException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "carona_id");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);

		stmt.setInt(1, idCarona);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			
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
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public int inserir(int idCarona, int idUsuario) 
			throws SQLException {
		String sql = String.format(this.insert, 
				this.getTableName(), 
				"carona_id, usuario_id",
				"?, ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setInt(1, idCarona);
		stmt.setInt(2, idUsuario);
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir relação entre carona e usuário.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir relação entre carona e usuário.");
            }
        }
	}

	public void excluir(int idCarona, int idUsuario) throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.deleteId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idCarona);
		stmt.setInt(2, idUsuario);
		
		stmt.executeUpdate();
	}
}
