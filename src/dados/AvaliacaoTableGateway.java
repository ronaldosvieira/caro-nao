package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvaliacaoTableGateway extends TableGateway {

	public AvaliacaoTableGateway() throws ClassNotFoundException, SQLException {
		super("avaliacao");
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
		
	public ResultSet obterPorAvaliador(int idAvaliador) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectColumn);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "avaliador_id");
		stmt.setInt(3, idAvaliador);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}
	
	public ResultSet obterPorAvaliado(int idAvaliado) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectColumn);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "avaliado_id");
		stmt.setInt(3, idAvaliado);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}

	public void inserir(int idAvaliador, int idAvaliado, int nota) 
			throws SQLException {
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.insert);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "avaliador_id, avaliado_id, nota");
		
		StringBuilder data = new StringBuilder();
		
		data.append(idAvaliador); data.append(", ");
		data.append(idAvaliado); data.append(", ");
		data.append(nota);
		
		stmt.setString(3, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, int idAvaliador, 
			int idAvaliado, int nota) 
			throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.updateId);
		
		stmt.setString(1, this.getTableName());
		
		StringBuilder data = new StringBuilder();
		
		data.append("avaliador_id = "); data.append(idAvaliador); data.append(", ");
		data.append("avaliado_id = "); data.append(idAvaliado); data.append(", ");
		data.append("nota = "); data.append(nota);
		
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
