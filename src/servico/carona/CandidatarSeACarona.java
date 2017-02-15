package servico.carona;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.CaronaModule;
import dominio.CaronaUsuarioModule;
import dominio.UsuarioModule;
import excecoes.CEPInvalidoException;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoNaoExisteException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/carona/candidatar-se")
public class CandidatarSeACarona extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CandidatarSeACarona() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			CaronaModule cm = new CaronaModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			RecordSet usuariosCarona = 
					cm.listarUsuarios(Integer.parseInt(idCarona));
			
			request.setAttribute("usuario", usuario);
			request.setAttribute("carona", carona);
			request.setAttribute("usuariosCarona", usuariosCarona);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/carona/candidatar-se.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (NumberFormatException | CaronaNaoAutorizadaException
				| VeiculoNaoExisteException | UsuarioNaoExisteException 
				| LogradouroNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");
		String idLogradouro = (String) request.getParameter("logradouro_id");
		String cep = (String) request.getParameter("cep");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			CaronaModule cm = new CaronaModule();
			UsuarioModule um = new UsuarioModule();
			
			um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			if (Integer.parseInt(idLogradouro) == -1) {
				cm.inserirUsuarioNaCarona(Integer.parseInt(idCarona),
					usuario.get(0).getInt("id"),
					cep);
			} else {
				cm.inserirUsuarioNaCarona(Integer.parseInt(idCarona),
					usuario.get(0).getInt("id"), 
					Integer.parseInt(idLogradouro));
			}
			
			response.sendRedirect(request.getContextPath() 
					+ "/carona/ver?id=" + idCarona);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | CaronaUsuarioJaExisteException
				| CaronaNaoAutorizadaException | VeiculoNaoExisteException
				| UsuarioNaoExisteException | LogradouroNaoExisteException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ServicoDeEnderecosInacessivelException e) {
			response.getWriter().append(
					"Erro ao acessar o serviço de endereços.");
			e.printStackTrace();
		} catch (CEPInvalidoException e) {
			request.setAttribute("erro", "CEP inválido.");
			
			RequestDispatcher rd = 
					request.getRequestDispatcher(
							request.getContextPath() + 
							"../../carona/candidatar-se?id=" + idCarona);
			rd.forward(request, response);
		}
	}

}
