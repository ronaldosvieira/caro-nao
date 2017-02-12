<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="pt_BR">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Caronão</title>

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
	RecordSet grupo = (RecordSet) request.getAttribute("grupo");
	RecordSet usuarios = (RecordSet) request.getAttribute("usuarios");
	RecordSet usuariosGrupo = (RecordSet) request.getAttribute("usuariosGrupo");
	%>
  
	<header class="text-center">
		<h1>
			Caro<span style="color: #DDD;">,</span>não
		</h1>
	 </header>
	<div class="container">
		<div class="col-sm-offset-2 col-sm-8 text-center">
			
			<% if (erro != null) { %>
				<div class="text-center text-danger">
					Erro: <%= erro %>
				</div>
			<% } %>
			
			<h4>Ver grupo</h4>
			
			<div class="form-group">
				<label for="nome">Nome</label>
				<p class="form-control-static">
					<%= grupo.get(0).getString("nome") %>
				</p>
			</div>
			
			<div class="form-group">
				<label for="descricao">Descricao</label>
				<p class="form-control-static">
					<%= grupo.get(0).getString("descricao") %>
				</p>
			</div>
			
			<div class="form-group">
				<label for="regras">Regras</label>
				<p class="form-control-static">
					<%= grupo.get(0).getString("regras") %>
				</p>
			</div>
			
			<div class="form-group">
				<label for="limite">Limite de avaliações negativas</label>
				<p class="form-control-static">
					<%= grupo.get(0).getInt("limite_avaliacoes_negativas") %>
				</p>
			</div>
			
			<h4>Usuários do grupo</h4>
			
			<table class="table table-hover">
				<thead>
				<tr>
					<th class="text-center col-sm-3">Nome</th>
					<th class="text-center col-sm-3">Email</th>
					<th class="text-center col-sm-4">Telefone</th>
					<th class="text-center col-sm-1">Nota</th>
					<th class="text-center col-sm-1">Ações</th>
				</tr>
				</thead>
				<tbody>
				<% for (Row usuario : usuarios) { %>
					<% Row usuarioGrupo = usuariosGrupo.get(usuariosGrupo.find("usuario_id", usuario.getInt("id"))); %>
					<% if (usuarioGrupo.getBoolean("ativo")) { %>
					<tr>
						<td><%= usuario.getString("nome") %></td>
						<td><%= usuario.getString("email") %></td>
						<td><%= usuario.getString("telefone") %></td>
						<td><%= 5/* todo: colocar nota */ %></td>
						<td>
							<a href="${pageContext.request.contextPath}/grupo/ver?id=<%= usuario.getInt("id") %>"
								class="btn btn-link">
								Ver
							</a>
						</td>
					</tr>
					<% } %>
				<% } %>
				</tbody>
			</table>
			
			<div class="form-group">
				<a href="${pageContext.request.contextPath}/grupo/editar?id=<%= grupo.get(0).getInt("id") %>"
					class="btn btn-default pull-right col-xs-12 col-sm-6">Editar</a>
				<a href="${pageContext.request.contextPath}/dashboard" 
					class="btn btn-link pull-right col-xs-12 col-sm-6">Voltar</a>
			</div>
		</div>
	</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  </body>
</html>