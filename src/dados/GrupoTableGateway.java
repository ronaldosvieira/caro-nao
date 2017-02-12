package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RecordSet;
import util.Row;

public class GrupoTableGateway extends TableGateway {

	public GrupoTableGateway() throws ClassNotFoundException, SQLException {
		super("grupo");
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
			row.put("nome", rs.getString("nome"));
			row.put("descricao", rs.getString("descricao"));
			row.put("regras", rs.getString("regras"));
			row.put("limite_avaliacoes_negativas", 
					rs.getInt("limite_avaliacoes_negativas"));
			row.put("ativo", rs.getBoolean("ativo"));
			row.put("criado_em", rs.getDate("criado_em"));
			row.put("alterado_em", rs.getDate("alterado_em"));
			
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
			row.put("nome", rs.getString("nome"));
			row.put("descricao", rs.getString("descricao"));
			row.put("regras", rs.getString("regras"));
			row.put("limite_avaliacoes_negativas", 
					rs.getInt("limite_avaliacoes_negativas"));
			row.put("ativo", rs.getBoolean("ativo"));
			row.put("criado_em", rs.getDate("criado_em"));
			row.put("alterado_em", rs.getDate("alterado_em"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public int inserir(String nome, String descricao, String regras, int limite, boolean ativo) 
			throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, 
					Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, "nome, descricao, regras, "
				+ "limite_avaliacoes_negativas, ativo");
		
		StringBuilder data = new StringBuilder();
		
		data.append(nome); data.append(", ");
		data.append(descricao); data.append(", ");
		data.append(regras); data.append(", ");
		data.append(limite); data.append(", ");
		data.append(ativo);
		
		stmt.setString(2, data.toString());
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir grupo.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir grupo.");
            }
        }
	}

	public void atualizar(int id, String nome, String descricao, 
			String regras, int limite, boolean ativo) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		StringBuilder data = new StringBuilder();
		
		data.append("nome = "); data.append(nome); data.append(", ");
		data.append("descricao = "); data.append(descricao); data.append(", ");
		data.append("regras = "); data.append(regras); data.append(", ");
		data.append("limite = "); data.append(limite); data.append(", ");
		data.append("ativo = "); data.append(ativo);
		
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
