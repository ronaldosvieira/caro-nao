package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.RecordSet;
import util.Row;

public class EstadoCaronaTableGateway extends TableGateway {

	public EstadoCaronaTableGateway() throws ClassNotFoundException, SQLException {
		super("estado_carona");
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
			row.put("pode_entrar", rs.getBoolean("pode_entrar"));
			row.put("pode_avaliar", rs.getBoolean("pode_avaliar"));
			
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
			row.put("pode_entrar", rs.getBoolean("pode_entrar"));
			row.put("pode_avaliar", rs.getBoolean("pode_avaliar"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
}
