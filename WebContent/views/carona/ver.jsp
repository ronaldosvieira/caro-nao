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
    
    <!-- CSS do caronão -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  
  <%@ page import = "java.sql.Timestamp" %>
  <%@ page import = "util.RecordSet" %>
  <%@ page import = "util.Row" %>
  
  <% String erro = (String) request.getAttribute("erro"); %>
  <% Boolean dono = (Boolean) request.getAttribute("dono"); %>
  <% RecordSet usuario = (RecordSet) request.getAttribute("usuario"); %>
  <% RecordSet carona = (RecordSet) request.getAttribute("carona"); %>
  <% RecordSet usuariosCarona = (RecordSet) request.getAttribute("usuariosCarona"); %>
  
  <%
  	boolean jaComecou =
	carona.get(0).getTimestamp("dia_horario")
		.before(new Timestamp(System.currentTimeMillis()));
	boolean ativa = carona.get(0).getInt("estado_carona_id") == 1;
	boolean concluida = carona.get(0).getInt("estado_carona_id") == 3;
	boolean jaParticipa = usuariosCarona.contains("id", usuario.get(0).getInt("id"));
	int usuariosNaCarona = usuariosCarona.size();
  %>
  
	<header class="text-center">
		<h1>
			Caro<span style="color: #DDD;">,</span>não
		</h1>
	 </header>
	<div class="container">
		<div class="col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6
			col-lg-offset-4 col-lg-4 text-center">
			
			<% if (erro != null) { %>
				<div class="text-center text-danger">
					Erro: <%= erro %>
				</div>
			<% } %>
			
			<h4>Ver carona</h4>

			<div class="form-group">
				<label for="veiculo">Veículo</label>
				<p><%= carona.get(0).getString("veiculo") %></p>
			</div>
			
			<div class="form-group">
				<label for="data_horario">Data/Horário</label>
				<p><%= carona.get(0).getTimestamp("dia_horario") %></p>
			</div>
			
			<div class="form-group">
				<label for="origem">Logradouro de origem</label>
				<p><%= carona.get(0).getString("origem") %></p>
			</div>
			
			<div class="form-group">
				<label for="destino">Logradouro de destino</label>
				<p><%= carona.get(0).getString("destino") %></p>
			</div>
			
			<% if (jaParticipa) { %>
				<% if (dono) { %>
					<div class="form-group">
						<p class="text-success">
							Você é o criador desta carona.
						</p>
					</div>
				<% } else { %>
					<div class="form-group">
						<p class="text-success">
							Você está participando desta carona.
						</p>
					</div>
				<% } %>
			<% } %>
			
			<div class="form-group">
				<% if (ativa) { %>
					<% if (!jaParticipa) { %>
						<a href="${pageContext.request.contextPath}/carona/candidatar-se?id=<%= carona.get(0).getInt("id") %>" 
							class="btn btn-default btn-block">Candidatar-se</a>
					<% } else if (!dono) { %>
						<form action="${pageContext.request.contextPath}/carona/desistir?id=<%= carona.get(0).getInt("id") %>"
							method="post">
							<button type="submit" class="btn btn-default btn-block">
								Desistir de participar
							</button>
						</form>
					<% } %>
					
					<% if (dono) { %>
						<% if (!jaComecou) { %>
							<a href="${pageContext.request.contextPath}/carona/editar?id=<%= carona.get(0).getInt("id") %>" 
								class="btn btn-default btn-block">Editar</a>
						<% } %>
							
						<% if (jaComecou) { %>
							<form action="${pageContext.request.contextPath}/carona/encerrar?id=<%= carona.get(0).getInt("id") %>"
								method="post">
								<button type="submit" class="btn btn-default btn-block">
									Encerrar
								</button>
							</form>
						<% } else if (usuariosNaCarona <= 1) { %>
							<form action="${pageContext.request.contextPath}/carona/cancelar?id=<%= carona.get(0).getInt("id") %>"
								method="post">
								<button type="submit" class="btn btn-default btn-block">
									Cancelar
								</button>
							</form>
						<% } %>
					<% } %>
				<% } %>
				
				<a href="${pageContext.request.contextPath}/dashboard" 
					class="btn btn-block btn-link">Voltar</a>
			</div>
		</div>
	</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  </body>
</html>