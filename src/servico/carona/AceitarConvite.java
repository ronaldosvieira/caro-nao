package servico.carona;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.CaronaModule;
import dominio.CaronaUsuarioModule;
import dominio.UsuarioModule;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.CaronaUsuarioNaoExisteException;
import excecoes.LogradouroNaoExisteException;
import excecoes.UsuarioJaEstaNaCaronaException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoNaoExisteException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/carona/aceitar")
public class AceitarConvite extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AceitarConvite() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");

		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			CaronaModule cm = new CaronaModule();
			UsuarioModule um = new UsuarioModule();
			CaronaUsuarioModule cum = new CaronaUsuarioModule();
			
			um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			cum.aceitarConvite(Integer.parseInt(idCarona), 
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
				| UsuarioJaEstaNaCaronaException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

}
