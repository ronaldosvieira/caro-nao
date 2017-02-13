package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RecordSet;
import util.Row;

public class ParadaTableGateway extends TableGateway {

	public ParadaTableGateway() throws ClassNotFoundException, SQLException {
		super("parada");
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
			row.put("logradouro_id", rs.getInt("logradouro_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public RecordSet obter(int id) throws SQLException, IndexOutOfBoundsException {
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
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("logradouro_id", rs.getInt("logradouro_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
		
	public RecordSet obterPorCarona(int idCarona) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectId, 
				this.getTableName(), "carona_id");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idCarona);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("logradouro_id", rs.getInt("logradouro_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public int inserir(int idCarona, int idLogradouro) 
			throws SQLException {
		String sql = String.format(this.insert, 
				this.getTableName(), 
				"carona_id, logradouro_id",
				"?, ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, 
					Statement.RETURN_GENERATED_KEYS);

		stmt.setInt(1, idCarona);
		stmt.setInt(2, idLogradouro);
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir parada.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir parada.");
            }
        }
	}

	public void excluir(int idCarona, int idLogradouro) throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.deleteId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idCarona);
		stmt.setInt(1, idLogradouro);
		
		stmt.executeUpdate();
	}
}
