package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RecordSet;
import util.Row;

public class AvaliacaoTableGateway extends TableGateway {

	public AvaliacaoTableGateway() throws ClassNotFoundException, SQLException {
		super("avaliacao");
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
			row.put("avaliador_id", rs.getInt("avaliador_id"));
			row.put("avaliado_id", rs.getInt("avaliado_id"));
			row.put("nota", rs.getInt("nota"));
			row.put("data", rs.getDate("data"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public RecordSet obter(int idCarona, int idAvaliador, int idAvaliado) 
			throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectMany, this.getTableName(),
				"carona_id = ? and avaliador_id = ? and avalido_id = ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idCarona);
		stmt.setInt(2, idAvaliador);
		stmt.setInt(3, idAvaliado);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("avaliador_id", rs.getInt("avaliador_id"));
			row.put("avaliado_id", rs.getInt("avaliado_id"));
			row.put("nota", rs.getInt("nota"));
			row.put("data", rs.getDate("data"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public RecordSet obterPorCarona(int idCarona) 
			throws SQLException, IndexOutOfBoundsException {
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
			row.put("avaliador_id", rs.getInt("avaliador_id"));
			row.put("avaliado_id", rs.getInt("avaliado_id"));
			row.put("nota", rs.getInt("nota"));
			row.put("data", rs.getDate("data"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public RecordSet obterPorAvaliador(int idAvaliador) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "avaliador_id");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idAvaliador);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("avaliador_id", rs.getInt("avaliador_id"));
			row.put("avaliado_id", rs.getInt("avaliado_id"));
			row.put("nota", rs.getInt("nota"));
			row.put("data", rs.getDate("data"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public RecordSet obterPorAvaliado(int idAvaliado) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "avaliado_id");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idAvaliado);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("avaliador_id", rs.getInt("avaliador_id"));
			row.put("avaliado_id", rs.getInt("avaliado_id"));
			row.put("nota", rs.getInt("nota"));
			row.put("data", rs.getDate("data"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public RecordSet obterPorCaronaEAvaliador(int idCarona, int idAvaliador) 
			throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectMany, 
				this.getTableName(), "carona_id = ? and avaliador_id = ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idCarona);
		stmt.setInt(2, idAvaliador);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("carona_id", rs.getInt("carona_id"));
			row.put("avaliador_id", rs.getInt("avaliador_id"));
			row.put("avaliado_id", rs.getInt("avaliado_id"));
			row.put("nota", rs.getInt("nota"));
			row.put("data", rs.getDate("data"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public int inserir(int idCarona, int idAvaliador, 
			int idAvaliado, int nota) 
			throws SQLException {
		String sql = String.format(this.insert, 
				this.getTableName(), 
				"carona_id, avaliador_id, avaliado_id, nota",
				"?, ?, ?, ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, 
					Statement.RETURN_GENERATED_KEYS);

		stmt.setInt(1, idCarona);
		stmt.setInt(2, idAvaliador);
		stmt.setInt(3, idAvaliado);
		stmt.setInt(4, nota);
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir avaliação.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir avaliação.");
            }
        }
	}

	public void atualizar(int idCarona, int idAvaliador, 
			int idAvaliado, int nota) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateMany, 
				this.getTableName(), "nota = ?", 
				"carona_id = ? and avaliador_id = ? and avaliado_id = ?");
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, nota);
		stmt.setInt(2, idAvaliador);
		stmt.setInt(3, idAvaliado);
		stmt.setInt(4, idCarona);
		
		stmt.executeUpdate();
	}

	public void excluir(int idCarona, int idAvaliador, int idAvaliado) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.deleteMany, this.getTableName(),
				"carona_id = ? and avaliador_id = ? and avaliado_id = ?");
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idCarona);
		stmt.setInt(2, idAvaliador);
		stmt.setInt(3, idAvaliado);
		
		stmt.executeUpdate();
	}
}
