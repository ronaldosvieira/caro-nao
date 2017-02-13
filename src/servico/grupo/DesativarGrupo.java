package servico.grupo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.GrupoModule;
import dominio.UsuarioModule;
import excecoes.DesativacaoGrupoInvalidaException;
import excecoes.GrupoNaoAutorizadoException;
import excecoes.GrupoNaoExisteException;
import excecoes.GrupoUsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoNaoAutorizadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/grupo/desativar")
public class DesativarGrupo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DesativarGrupo() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idGrupo = (String) request.getParameter("id");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule(new RecordSet());
			um.validarGrupo(
				usuario.get(0).getInt("id"), 
				Integer.parseInt(idGrupo));
			
			GrupoModule gm = new GrupoModule(new RecordSet());
			
			gm.desativarGrupo(Integer.parseInt(idGrupo));
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | GrupoNaoExisteException 
				| GrupoNaoAutorizadoException | GrupoUsuarioNaoExisteException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (DesativacaoGrupoInvalidaException e) {
			// TODO: adicionar msg de erro
			response.sendRedirect(request.getContextPath() + "/grupo/ver?id=" + idGrupo);
		}
	}

}
