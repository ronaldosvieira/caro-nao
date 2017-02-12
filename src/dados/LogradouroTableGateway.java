package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.RecordSet;
import util.Row;

public class LogradouroTableGateway extends TableGateway {

	public LogradouroTableGateway() throws ClassNotFoundException, SQLException {
		super("logradouro");
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
			row.put("cep", rs.getString("cep"));
			row.put("estado", rs.getString("estado"));
			row.put("distrito", rs.getString("distrito"));
			row.put("endereco", rs.getString("endereco"));
			row.put("numero", rs.getString("numero"));
			
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
			row.put("cep", rs.getString("cep"));
			row.put("estado", rs.getString("estado"));
			row.put("distrito", rs.getString("distrito"));
			row.put("endereco", rs.getString("endereco"));
			row.put("numero", rs.getString("numero"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public RecordSet obterPorCEP(String cep) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "cep");
		stmt.setString(2, cep);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("cep", rs.getString("cep"));
			row.put("estado", rs.getString("estado"));
			row.put("distrito", rs.getString("distrito"));
			row.put("endereco", rs.getString("endereco"));
			row.put("numero", rs.getString("numero"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public void inserir(String cep, String estado, String distrito, 
			String endereco, String numero) 
			throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "cep, estado, distrito, endereco, numero");
		
		StringBuilder data = new StringBuilder();
		
		data.append(cep); data.append(", ");
		data.append(estado); data.append(", ");
		data.append(distrito); data.append(", ");
		data.append(endereco); data.append(", ");
		data.append(numero);
		
		stmt.setString(2, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, String cep, String estado, 
			String distrito, String endereco, String numero) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		StringBuilder data = new StringBuilder();
		
		data.append("cep = "); data.append(cep); data.append(", ");
		data.append("estado = "); data.append(estado); data.append(", ");
		data.append("distrito = "); data.append(distrito); data.append(", ");
		data.append("endereco = "); data.append(endereco); data.append(", ");
		data.append("numero = "); data.append(numero);
		
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
