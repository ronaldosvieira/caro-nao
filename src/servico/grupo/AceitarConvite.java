package servico.grupo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.GrupoModule;
import dominio.GrupoUsuarioModule;
import dominio.UsuarioModule;
import excecoes.GrupoNaoAutorizadoException;
import excecoes.GrupoUsuarioNaoExisteException;
import excecoes.UsuarioJaEstaNoGrupoException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/grupo/aceitar")
public class AceitarConvite extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AceitarConvite() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idGrupo = (String) request.getParameter("id");

		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			GrupoModule gm = new GrupoModule();
			UsuarioModule um = new UsuarioModule();
			GrupoUsuarioModule gum = new GrupoUsuarioModule();
			
			um.validarGrupo(usuario.get(0).getInt("id"), 
					Integer.parseInt(idGrupo));
			
			gum.aceitarConvite(Integer.parseInt(idGrupo), 
					usuario.get(0).getInt("id"));
			
			response.sendRedirect(request.getContextPath() 
					+ "/grupo/ver?id=" + idGrupo);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | GrupoNaoAutorizadoException
				| UsuarioNaoExisteException | GrupoUsuarioNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (UsuarioJaEstaNoGrupoException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() 
					+ "/grupo/ver?id=" + idGrupo);
		}
	}

}
