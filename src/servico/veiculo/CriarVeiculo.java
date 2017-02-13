package servico.veiculo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.VeiculoModule;
import excecoes.EmailJaCadastradoException;
import excecoes.UsuarioNaoLogadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/veiculo/criar")
public class CriarVeiculo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CriarVeiculo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/veiculo/criar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String modelo = (String) request.getParameter("modelo");
		String placa = (String) request.getParameter("placa");
		String cor = (String) request.getParameter("cor");
		int vagas = Integer.parseInt(request.getParameter("vagas"));
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			VeiculoModule vm = new VeiculoModule();
			
			vm.inserirVeiculo(modelo, placa, cor, vagas, 
					usuario.get(0).getInt("id"));
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		}
	}

}
