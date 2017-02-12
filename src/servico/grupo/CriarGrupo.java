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

@WebServlet("/grupo/criar")
public class CriarGrupo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CriarGrupo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/grupo/criar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome = (String) request.getParameter("nome");
		String descricao = (String) request.getParameter("descricao");
		String regras = (String) request.getParameter("regras");
		int limite = Integer.parseInt(request.getParameter("limite"));
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			GrupoModule gm = new GrupoModule(new RecordSet());
			GrupoUsuarioModule gum = new GrupoUsuarioModule(new RecordSet()); 
			
			gm.inserirGrupo(usuario.get(0).getInt("id"), 
					nome, descricao, regras, limite);
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		}
	}

}
