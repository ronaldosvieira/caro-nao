package dominio;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import dados.CaronaTableGateway;
import excecoes.CEPInvalidoException;
import excecoes.CaronaNaoExisteException;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.CaronaUsuarioNaoExisteException;
import excecoes.DataInvalidaException;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoExisteException;
import excecoes.VeiculoComMenosVagasException;
import excecoes.VeiculoJaSelecionadoException;
import excecoes.VeiculoNaoExisteException;
import util.RecordSet;
import util.Row;

public class CaronaModule {
	private CaronaTableGateway ctg;
	private enum EstadoCarona {
		Ativa(1), Cancelada(2), Concluida(3);
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
	
	public RecordSet obterDono(int id) 
			throws ClassNotFoundException, SQLException, 
			CaronaNaoExisteException, VeiculoNaoExisteException, 
			UsuarioNaoExisteException {
		VeiculoModule vm = new VeiculoModule();
		
		RecordSet carona = this.obter(id);
		
		return vm.obterDono(carona.get(0).getInt("veiculo_id"));
	}
	
	public RecordSet listarUsuarios(int id) throws SQLException, ClassNotFoundException, LogradouroNaoExisteException {
		CaronaUsuarioModule gum = new CaronaUsuarioModule();
		
		return gum.listarUsuariosPorCarona(id);
	}
	
	public int inserirCarona(int idVeiculo, String data, String horario, 
			String cepOrigem, String numeroOrigem, String cepDestino,
			String numeroDestino) 
			throws SQLException, VeiculoJaSelecionadoException, 
			ClassNotFoundException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, 
			VeiculoNaoExisteException, CaronaUsuarioJaExisteException, UsuarioNaoExisteException {
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
		VeiculoModule vm = new VeiculoModule();
		CaronaUsuarioModule cum = new CaronaUsuarioModule();
		
		RecordSet criador = vm.obterDono(idVeiculo);
		
		int idLogradouroOrigem = 
				lm.inserirLogradouro(cepOrigem, numeroOrigem);
		int idLogradouroDestino =
				lm.inserirLogradouro(cepDestino, numeroDestino);
		
		int idCarona = ctg.inserir(idVeiculo, diaHorario, 
				idLogradouroOrigem, idLogradouroDestino);
		
		cum.inserirCaronaUsuario(idCarona, 
				criador.get(0).getInt("id"), idLogradouroDestino,
				true);
		
		return idCarona;
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
	
	private void atualizarEstadoDaCarona(int id, int idEstadoCarona) throws SQLException, CaronaNaoExisteException {
		RecordSet carona = this.obter(id);
		
		this.ctg.atualizar(id, carona.get(0).getInt("veiculo_id"), 
				carona.get(0).getTimestamp("dia_horario"), 
				carona.get(0).getInt("logradouro_origem_id"), 
				carona.get(0).getInt("logradouro_destino_id"), 
				idEstadoCarona);
	}

	public void cancelarCarona(int id) throws SQLException, CaronaNaoExisteException {
		RecordSet carona = this.obter(id);
		Row caronaRow = carona.get(0);
		
		boolean estaAtiva = caronaRow.getInt("estado_carona_id")
				== EstadoCarona.Ativa.getId();
		boolean jaComecou = caronaRow.getTimestamp("dia_horario").getTime()
				< new Timestamp(System.currentTimeMillis()).getTime();
		
		if (estaAtiva && !jaComecou) {
			this.atualizarEstadoDaCarona(id, 
					EstadoCarona.Cancelada.getId());
		}
	}

	public void concluirCarona(int id) throws SQLException, CaronaNaoExisteException {
		RecordSet carona = this.obter(id);
		Row caronaRow = carona.get(0);
		
		boolean estaAtiva = caronaRow.getInt("estado_carona_id")
				== EstadoCarona.Ativa.getId();
		boolean jaComecou = caronaRow.getTimestamp("dia_horario").getTime()
				< new Timestamp(System.currentTimeMillis()).getTime();
		
		if (estaAtiva && jaComecou) {
			this.atualizarEstadoDaCarona(id, 
					EstadoCarona.Concluida.getId());
		}
	}
	
	public void convidarUsuario(int id, int idUsuario, String cep) 
			throws ClassNotFoundException, SQLException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioJaExisteException {
		this.inserirUsuarioNaCarona(id, idUsuario, 
				cep, false);
	}
	
	public void convidarUsuario(int id, int idUsuario, int idLogradouro) 
			throws ClassNotFoundException, SQLException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioJaExisteException {
		this.inserirUsuarioNaCarona(id, idUsuario, 
				idLogradouro, false);
	}
	
	private void inserirUsuarioNaCarona(int id, int idUsuario,
			String cep, boolean ativo) 
			throws ClassNotFoundException, SQLException, 
			ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException, CaronaUsuarioJaExisteException {
		LogradouroModule lm = new LogradouroModule();
		
		int idLogradouro = lm.inserirLogradouro(cep, "" /* TODO */);
		
		this.inserirUsuarioNaCarona(id, idUsuario, idLogradouro, ativo);
	}

	private void inserirUsuarioNaCarona(int id, int idUsuario, 
			int idLogradouro, boolean ativo) 
			throws ClassNotFoundException, SQLException, 
			CaronaUsuarioJaExisteException {
		CaronaUsuarioModule cum = new CaronaUsuarioModule();
		
		// validar se usuário já está na carona
		// validar se é possível adicionar usuario na carona
		
		cum.inserirCaronaUsuario(id, idUsuario, idLogradouro, ativo);
	}

	public void removerUsuarioDaCarona(int id, int idUsuario) 
			throws SQLException, ClassNotFoundException, 
			CaronaUsuarioNaoExisteException {
		CaronaUsuarioModule cum = new CaronaUsuarioModule();
		
		// validar se carona esta ativa
		
		RecordSet caronaUsuario = cum.obter(id, idUsuario);
	
		cum.excluirCaronaUsuario(id, idUsuario);
	}
}
