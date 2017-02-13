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
import excecoes.CaronaNaoAutorizadaException;
import excecoes.CaronaNaoExisteException;
import excecoes.DataInvalidaException;
import excecoes.GrupoNaoAutorizadoException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoJaSelecionadoException;
import excecoes.VeiculoNaoExisteException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/carona/cancelar")
public class CancelarCarona extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CancelarCarona() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");

		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			CaronaModule cm = new CaronaModule();
			UsuarioModule um = new UsuarioModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			um.validarDonoCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
	
			cm.cancelarCarona(Integer.parseInt(idCarona));
			
			response.sendRedirect(request.getContextPath() + "/carona/ver?id=" + idCarona);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | CaronaNaoAutorizadaException
				| VeiculoNaoExisteException | CaronaNaoExisteException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

}
