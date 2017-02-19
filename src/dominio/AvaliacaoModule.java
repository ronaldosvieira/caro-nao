package dominio;

import java.sql.SQLException;

import dados.AvaliacaoTableGateway;
import excecoes.AvaliacaoJaExisteException;
import excecoes.AvaliacaoNaoExisteException;
import util.RecordSet;
import util.Row;

public class AvaliacaoModule {
	AvaliacaoTableGateway atg;
	
	public AvaliacaoModule() throws ClassNotFoundException, SQLException {
		this.atg = new AvaliacaoTableGateway();
	}
	
	public RecordSet obter(int idCarona, int idAvaliador, int idAvaliado) 
			throws AvaliacaoNaoExisteException, SQLException {
		RecordSet avaliacao = 
				this.atg.obter(idCarona, idAvaliador, idAvaliado);
		
		if (avaliacao.isEmpty()) {
			throw new AvaliacaoNaoExisteException();
		}
		
		return avaliacao;
	}
	
	public RecordSet obterPorAvaliado(int idUsuario) throws SQLException {
		return this.atg.obterPorAvaliado(idUsuario);
	}

	public void validarInsercao(int idCarona, int idAvaliador, 
			int idAvaliado, int nota) 
			throws IndexOutOfBoundsException, SQLException, 
			AvaliacaoJaExisteException {
		RecordSet jaExiste = atg.obterPorCarona(idCarona)
				.getWhere("avaliador_id", idAvaliador)
				.getWhere("avaliado_id", idAvaliado);
		
		if (!jaExiste.isEmpty()) {
			throw new AvaliacaoJaExisteException();
		}
		
		/* TODO validar range da nota */
	}
	
	public double obterNotaMedia(String colunaNota, 
			RecordSet avaliacoes) {
		double media = 0.0;
		
		for (Row avaliacao : avaliacoes) {
			media += avaliacao.getInt(colunaNota);
		}
		
		return media / avaliacoes.size();
	}
	
	public RecordSet obterNotasNegativas(String colunaNota, 
			RecordSet avaliacoes) {
		RecordSet avalNegativas = new RecordSet();
		
		for (Row avaliacao : avaliacoes) {
			if (avaliacao.getInt("nota") < 3) {
				avalNegativas.add(avaliacao);
			}
		}
		
		return avalNegativas;
	}
}
