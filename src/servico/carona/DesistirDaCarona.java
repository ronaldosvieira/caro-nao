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
import excecoes.CaronaNaoAtivaException;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.CaronaNaoExisteException;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.CaronaUsuarioNaoExisteException;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoNaoExisteException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/carona/desistir")
public class DesistirDaCarona extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DesistirDaCarona() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");

		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			CaronaModule cm = new CaronaModule();
			UsuarioModule um = new UsuarioModule();
			
			um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			cm.removerUsuarioDaCarona(Integer.parseInt(idCarona),
					usuario.get(0).getInt("id"));
			
			response.sendRedirect(request.getContextPath() 
					+ "/carona/ver?id=" + idCarona);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | CaronaNaoAutorizadaException 
				| VeiculoNaoExisteException | CaronaUsuarioNaoExisteException
				| UsuarioNaoExisteException | LogradouroNaoExisteException
				| CaronaNaoAtivaException | CaronaNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

}
