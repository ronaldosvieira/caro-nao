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
			
			row.put("id", rs.getInt("id"));
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
		String sql = String.format(this.selectId, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "avaliador_id");
		stmt.setInt(2, idAvaliador);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
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
	
	public RecordSet obterPorAvaliado(int idAvaliado) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "avaliado_id");
		stmt.setInt(2, idAvaliado);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
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

	public int inserir(int idAvaliador, int idAvaliado, int nota) 
			throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, 
					Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, "avaliador_id, avaliado_id, nota");
		
		StringBuilder data = new StringBuilder();
		
		data.append(idAvaliador); data.append(", ");
		data.append(idAvaliado); data.append(", ");
		data.append(nota);
		
		stmt.setString(2, data.toString());
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir usuário.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir usuário.");
            }
        }
	}

	public void atualizar(int id, int idAvaliador, 
			int idAvaliado, int nota) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		StringBuilder data = new StringBuilder();
		
		data.append("avaliador_id = "); data.append(idAvaliador); data.append(", ");
		data.append("avaliado_id = "); data.append(idAvaliado); data.append(", ");
		data.append("nota = "); data.append(nota);
		
		stmt.setString(1, data.toString());
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
