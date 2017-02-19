package servico.carona;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.CaronaModule;
import dominio.UsuarioModule;
import excecoes.CEPInvalidoException;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.DataInvalidaException;
import excecoes.ErroDeValidacao;
import excecoes.GrupoNaoAutorizadoException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoJaSelecionadoException;
import excecoes.VeiculoNaoExisteException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/carona/criar")
public class CriarCarona extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CriarCarona() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			
			RecordSet veiculos = um.listarVeiculos(usuario.get(0).getInt("id"));
			
			request.setAttribute("usuario", usuario);
			request.setAttribute("veiculos", veiculos);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/carona/criar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (NumberFormatException | UsuarioNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idVeiculo = (String) request.getParameter("veiculo_id");
		String data = (String) request.getParameter("data");
		String horario = (String) request.getParameter("horario");
		String cepOrigem = request.getParameter("cep_origem");
		String cepDestino = request.getParameter("cep_destino");
		String numeroOrigem = request.getParameter("numero_origem");
		String numeroDestino = request.getParameter("numero_destino");

		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			CaronaModule cm = new CaronaModule();
			UsuarioModule um = new UsuarioModule();
			
			RecordSet veiculos = um.listarVeiculos(usuario.get(0).getInt("id"));

			cm.inserirCarona(Integer.parseInt(idVeiculo), data, 
					horario, cepOrigem, numeroOrigem, cepDestino, 
					numeroDestino);
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | VeiculoNaoExisteException
				| CaronaUsuarioJaExisteException | UsuarioNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (VeiculoJaSelecionadoException e) {
			request.setAttribute("erro", 
					"O veÌculo informado j· foi escolhido para uma "
					+ "carona no mesmo dia e hor·rio.");
			
			doGet(request, response);
		} catch (ServicoDeEnderecosInacessivelException e) {
			response.getWriter().append("Erro ao acessar o servi√ßo de endere√ßos.");
			e.printStackTrace();
		} catch (DataInvalidaException e) {
			request.setAttribute("erro", 
					"Data ou hor·rio de saÌda " + e.getData() 
					+ " inv·lido.");
			
			doGet(request, response);
		} catch (CEPInvalidoException e) {
			request.setAttribute("erro", 
					"CEP " + e.getCep() + " n√£o encontrado.");
			
			doGet(request, response);
		} catch (ErroDeValidacao e) {
			request.setAttribute("erro", e.obterErro());
			
			doGet(request, response);
		}
	}

}
