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
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoNaoAutorizadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/grupo/editar")
public class EditarGrupo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EditarGrupo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idGrupo = (String) request.getParameter("id");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule(new RecordSet());
			
			RecordSet grupo = um.validarGrupo(
					usuario.get(0).getInt("id"), 
					Integer.parseInt(idGrupo));
			
			request.setAttribute("grupo", grupo);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/grupo/editar.jsp");
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
		String nome = (String) request.getParameter("nome");
		String descricao = (String) request.getParameter("descricao");
		int limite = Integer.parseInt(request.getParameter("limite"));
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule(new RecordSet());
			um.validarGrupo(
				usuario.get(0).getInt("id"), 
				Integer.parseInt(idGrupo));
			
			GrupoModule gm = new GrupoModule(new RecordSet());
			
			gm.atualizarGrupo(Integer.parseInt(idGrupo), 
					nome, descricao, limite);
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | GrupoNaoExisteException 
				| GrupoNaoAutorizadoException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

}
