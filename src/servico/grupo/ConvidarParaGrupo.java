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
import excecoes.GrupoNaoAutorizadoException;
import excecoes.GrupoNaoExisteException;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/grupo/convidar")
public class ConvidarParaGrupo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ConvidarParaGrupo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idGrupo = (String) request.getParameter("id");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			
			RecordSet grupo = um.validarGrupo(
					usuario.get(0).getInt("id"), 
					Integer.parseInt(idGrupo));
			
			request.setAttribute("grupo", grupo);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/grupo/convidar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (NumberFormatException | GrupoNaoAutorizadoException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idGrupo = (String) request.getParameter("id");
		String email = (String) request.getParameter("email");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			GrupoModule gm = new GrupoModule();
			
			um.validarGrupo(
				usuario.get(0).getInt("id"), 
				Integer.parseInt(idGrupo));
			
			RecordSet grupo = gm.obter(Integer.parseInt(idGrupo));
			
			request.setAttribute("grupo", grupo);
			
			um.convidarUsuario(email, Integer.parseInt(idGrupo));
			
			request.setAttribute("sucesso", "Usu�rio convidado com sucesso!");
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/grupo/convidar.jsp");
			rd.forward(request, response);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | GrupoUsuarioJaExisteException 
				| GrupoNaoExisteException | GrupoNaoAutorizadoException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (UsuarioNaoExisteException e) {
			request.setAttribute("erro", 
					"Seu amigo(a) ainda n�o est� cadastrado(a) no Caron�o.");
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/grupo/convidar.jsp");
			rd.forward(request, response);
		}
	}

}
