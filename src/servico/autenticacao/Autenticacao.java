package servico.autenticacao;

import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dados.UsuarioTableGateway;
import dominio.UsuarioModule;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import util.RecordSet;

public class Autenticacao {
	
	private Autenticacao() {}
	
    public static RecordSet autenticar(HttpServletRequest request, HttpServletResponse response) throws UsuarioNaoLogadoException {
    	Cookie[] cookies = request.getCookies();
		String emailUsuarioLogado = null;
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("caronao-login")) {
					emailUsuarioLogado = cookie.getValue();
					break;
				}
			}
		}
		
		try (UsuarioTableGateway utg = new UsuarioTableGateway()) {
			UsuarioModule um = new UsuarioModule();
			
			RecordSet usuario = utg.obterPeloEmail(emailUsuarioLogado);
			
			um.validarExistencia(usuario);
			
			return usuario;
		} catch (UsuarioNaoExisteException | ClassNotFoundException | SQLException e) {
			throw new UsuarioNaoLogadoException();
		}
    }
}
