package dominio;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import dados.CaronaTableGateway;
import excecoes.CEPInvalidoException;
import excecoes.CaronaNaoExisteException;
import excecoes.DataInvalidaException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.VeiculoComMenosVagasException;
import excecoes.VeiculoJaSelecionadoException;
import excecoes.VeiculoNaoExisteException;
import util.RecordSet;
import util.Row;

public class CaronaModule {
	private CaronaTableGateway ctg;
	private enum EstadoCarona {
		Ativa(1), Cancelada(2), Concluida(2);
		private int id;
		
		private EstadoCarona(int id) {this.id = id;}
		public int getId() {return this.id;}
	};

	private static long intervaloMinEntreCaronas = 
			1 /* h */ * 60 /* m */ * 60 /* s */ * 1000 /* ms */;
	
	public CaronaModule() throws ClassNotFoundException, SQLException {
		this.ctg = new CaronaTableGateway();
	}
	
	public RecordSet obter(int id) throws SQLException, CaronaNaoExisteException {
		RecordSet carona = this.ctg.obter(id);
		
		if (carona.isEmpty()) throw new CaronaNaoExisteException();
		
		return carona;
	}
	
	public RecordSet obterPeloVeiculo(int idVeiculo) throws SQLException {
		return this.ctg.obterPorVeiculo(idVeiculo);
	}
	
	public int inserirCarona(int idVeiculo, String data, String horario, 
			String cepOrigem, String numeroOrigem, String cepDestino,
			String numeroDestino) 
			throws SQLException, VeiculoJaSelecionadoException, 
			ClassNotFoundException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException {
		String[] dataSep = data.split("-");
		String[] horarioSep = horario.split(":");
		
		Timestamp diaHorario;
		
		try {
			diaHorario = Timestamp.valueOf(
				LocalDateTime.of(Integer.parseInt(dataSep[0]), 
					Integer.parseInt(dataSep[1]), 
					Integer.parseInt(dataSep[2]), 
					Integer.parseInt(horarioSep[0]), 
					Integer.parseInt(horarioSep[1])));
		} catch (NumberFormatException e) {
			throw new DataInvalidaException(data + " " + horario);
		}
		
		RecordSet caronas = this.obterPeloVeiculo(idVeiculo);
		
		for (Row carona: caronas) {
			if (carona.getInt("estado_carona_id") 
					!= EstadoCarona.Ativa.getId()) {
				continue;
			}
			
			if (Math.abs(carona.getTimestamp("dia_horario").getTime()
					- diaHorario.getTime()) <= intervaloMinEntreCaronas) {
				throw new VeiculoJaSelecionadoException();
			}
		}
		
		LogradouroModule lm = new LogradouroModule();
		
		int idLogradouroOrigem = 
				lm.inserirLogradouro(cepOrigem, numeroOrigem);
		int idLogradouroDestino =
				lm.inserirLogradouro(cepDestino, numeroDestino);
		
		return ctg.inserir(idVeiculo, diaHorario, 
				idLogradouroOrigem, idLogradouroDestino);
	}
	
	public void atualizarVeiculoDaCarona(int id, int idVeiculo) 
			throws SQLException, VeiculoNaoExisteException, 
			VeiculoJaSelecionadoException, CaronaNaoExisteException, 
			ClassNotFoundException, VeiculoComMenosVagasException {
		VeiculoModule vm = new VeiculoModule();
		
		RecordSet novoVeiculo = vm.obter(idVeiculo);
		RecordSet veiculoAtual = vm.obter(id);
		
		if (novoVeiculo.get(0).getInt("vagas")
				< veiculoAtual.get(0).getInt("vagas")) {
			throw new VeiculoComMenosVagasException();
		}
		
		RecordSet carona = this.obter(id);
		RecordSet caronasVeiculo = this.obterPeloVeiculo(idVeiculo);
		
		Row caronaRow = carona.get(0);
		
		for (Row caronaVeiculo: caronasVeiculo) {
			if (caronaVeiculo.getInt("id") == caronaRow.getInt("id")) {
				continue;
			}
			
			if (caronaVeiculo.getInt("estado_carona_id") 
					!= EstadoCarona.Ativa.getId()) {
				continue;
			}
			
			if (Math.abs(caronaVeiculo.getTimestamp("dia_horario").getTime()
					- caronaRow.getTimestamp("dia_horario").getTime())
					<= intervaloMinEntreCaronas) {
				throw new VeiculoJaSelecionadoException();
			}
		}
		
		ctg.atualizar(id, idVeiculo, caronaRow.getTimestamp("dia_horario"), 
				caronaRow.getInt("logradouro_origem_id"), 
				caronaRow.getInt("logradouro_destino_id"), 
				caronaRow.getInt("estado_carona_id"));
	}
}
