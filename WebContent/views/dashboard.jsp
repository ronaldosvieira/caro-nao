<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="pt_BR">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Caron�o</title>

    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  
  <%@ page import = "util.RecordSet" %>
  <%@ page import = "util.Row" %>
  
  <% 
  String erro = (String) request.getAttribute("erro");
  RecordSet usuario = (RecordSet) request.getAttribute("usuario");
  RecordSet grupos = (RecordSet) request.getAttribute("grupos");
  %>
  
	<header class="text-center">
		<h1>
			Caro<span style="color: #DDD;">,</span>n�o
		</h1>
	 </header>
	<div class="container">
		<div class="col-md-offset-2 col-md-8 text-center">
			
			<% if (erro != null) { %>
				<div class="text-center text-danger">
					Erro: <%= erro %>
				</div>
			<% } %>
			
			<span>Bem vindo(a), <%= usuario.get(0).getString("nome") %>!</span>
			
			<form action="${pageContext.request.contextPath}/sair"
				method="post">
				<a href="${pageContext.request.contextPath}/perfil/editar">
					Editar perfil
				</a>
				<button type="submit" class="btn btn-link">Sair</button>
			</form>
			
			<h4>Meus grupos</h4>
			
			<table class="table table-hover">
				<thead>
				<tr>
					<th class="text-center col-sm-1">C�d.</th>
					<th class="text-center col-sm-3">Nome</th>
					<th class="text-center col-sm-5">Descri��o</th>
					<th class="text-center col-sm-3">A��es</th>
				</tr>
				</thead>
				<tbody>
				<% if (grupos.isEmpty()) { %>
				<tr>
					<td colspan="4">
						Voc� ainda n�o participa de grupos.<br> 
						Pe�a convites aos seus amigos ou <a>crie um novo grupo.</a>
					</td>
				</tr>
				<% } else { %>
					<% for (Row grupo : grupos) { %>
						<% if (grupo.getBoolean("ativo")) { %>
						<tr>
							<td><%= grupo.getInt("id") %></td>
							<td><%= grupo.getString("nome") %></td>
							<td class="text-left"><%= grupo.getString("descricao") %></td>
							<td>
								<a href="${pageContext.request.contextPath}/grupo/ver?id=<%= grupo.getInt("id") %>"
									class="btn btn-link">
									Ver
								</a>
								<a href="${pageContext.request.contextPath}/grupo/convidar?id=<%= grupo.getInt("id") %>"
									class="btn btn-link">
									Convidar
								</a>
							</td>
						</tr>
						<% } %>
					<% } %>
				<% } %>
				</tbody>
			</table>
		</div>
	</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  </body>
</html>