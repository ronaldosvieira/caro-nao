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
import dominio.GrupoUsuarioModule;
import excecoes.UsuarioNaoLogadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/grupo/ver")
public class VerGrupo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public VerGrupo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idGrupo = (String) request.getParameter("id"); 
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			GrupoModule gm = new GrupoModule(new RecordSet());
			GrupoUsuarioModule gum = new GrupoUsuarioModule(new RecordSet());
			
			RecordSet grupo = gm.obter(Integer.parseInt(idGrupo));
			RecordSet usuarios = gm.listarUsuarios(grupo.get(0).getInt("id"));
			RecordSet usuariosGrupo = 
					gum.obterGrupoUsuarioPorGrupo(grupo.get(0).getInt("id"));
			
			request.setAttribute("grupo", grupo);
			request.setAttribute("usuarios", usuarios);
			request.setAttribute("usuariosGrupo", usuariosGrupo);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/grupo/ver.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		}
	}
}
