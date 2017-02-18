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
  RecordSet usuario = (RecordSet) request.getAttribute("usuario");
  RecordSet grupos = (RecordSet) request.getAttribute("grupos");
  RecordSet veiculos = (RecordSet) request.getAttribute("veiculos");
  RecordSet caronas = (RecordSet) request.getAttribute("caronas");
  %>
  
	<header class="text-center">
		<h1>
			Caro<span style="color: #DDD;">,</span>não
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
			
			<br>

			<h4>Meus grupos</h4>
			
			<table class="table table-hover">
				<thead>
				<tr>
					<th class="text-center col-sm-1">Cód.</th>
					<th class="text-center col-sm-3">Nome</th>
					<th class="text-center col-sm-5">Descrição</th>
					<th class="text-center col-sm-3">Ações</th>
				</tr>
				</thead>
				<tbody>
				<% if (grupos.isEmpty()) { %>
				<tr>
					<td colspan="4">
						Você ainda não participa de grupos.<br> 
						Peça convites aos seus amigos ou 
						<a href="${pageContext.request.contextPath}/grupo/criar">
							crie um novo grupo
						</a>.
					</td>
				</tr>
				<% } else { %>
					<% for (Row grupo : grupos) { %>
						<% if (grupo.getBoolean("ativo") && 
								grupo.getBoolean("usuario_ativo")) { %>
						<tr>
							<td><%= grupo.getInt("id") %></td>
							<td><%= grupo.getString("nome") %></td>
							<td class="text-left"><%= grupo.getString("descricao") %></td>
							<td>
							<% if (grupo.getBoolean("aceitou_regras")) { %>
								<a href="${pageContext.request.contextPath}/grupo/ver?id=<%= grupo.getInt("id") %>"
									class="btn btn-link">
									Ver
								</a>
								<a href="${pageContext.request.contextPath}/grupo/convidar?id=<%= grupo.getInt("id") %>"
									class="btn btn-link">
									Convidar
								</a>
							<% } else { %>
								<a href="${pageContext.request.contextPath}/grupo/ver?id=<%= grupo.getInt("id") %>"
									class="btn btn-link">
									Aceitar convite
								</a>
							<% } %>
							</td>
						</tr>
						<% } %>
					<% } %>
				<% } %>
				</tbody>
			</table>
			
			<div class="text-right">
				<a href="${pageContext.request.contextPath}/grupo/criar" 
				class="btn btn-link">
					Criar um novo grupo
				</a>
			</div>
			
			<br>
			
			<h4>Caronas dos seus grupos</h4>
			
			<table class="table table-hover">
				<thead>
				<tr>
					<th class="text-center col-sm-1">Veículo</th>
					<th class="text-center col-sm-2">Data/horário</th>
					<th class="text-center col-sm-3">Origem</th>
					<th class="text-center col-sm-3">Destino</th>
					<th class="text-center col-sm-1">Vagas restantes</th>
					<th class="text-center col-sm-1">Estado</th>
					<th class="text-center col-sm-1">Ações</th>
				</tr>
				</thead>
				<tbody>
				<% if (caronas.isEmpty()) { %>
				<tr>
					<td colspan="7">
						Nenhuma carona disponível.
					</td>
				</tr>
				<% } else { %>
					<% for (Row carona : caronas) { %>
						<% String estado = ""; %>
						<% 
						switch (carona.getInt("estado_carona_id")) {
							case 1:
								estado = "Ativa";
								break;
							case 2:
								estado = "Cancelada";
								break;
							case 3:
								estado = "Concluída";
								break;
						} 
						%>
						<tr>
							<td><%= carona.getString("veiculo") %></td>
							<td><%= carona.getTimestamp("dia_horario") %></td>
							<td><%= carona.getString("origem") %></td>
							<td><%= carona.getString("destino") %></td>
							<td>
								<%= carona.getInt("vagas_restantes") %> /
								<%= carona.getInt("vagas") %>
							</td>
							<td><%= estado %></td>
							<td>
								<a href="${pageContext.request.contextPath}/carona/ver?id=<%= carona.getInt("id") %>"
									class="btn btn-link">
									Ver
								</a>
							</td>
						</tr>
					<% } %>
				<% } %>
				</tbody>
			</table>
			
			<% if (!veiculos.isEmpty()) { %>
			<div class="text-right">
				<a href="${pageContext.request.contextPath}/carona/criar" 
				class="btn btn-link">
					Criar uma nova carona
				</a>
			</div>
			
			<br>
			<% } %>
			
			<h4>Meus veículos</h4>
			
			<table class="table table-hover">
				<thead>
				<tr>
					<th class="text-center col-sm-3">Modelo</th>
					<th class="text-center col-sm-3">Placa</th>
					<th class="text-center col-sm-2">Cor</th>
					<th class="text-center col-sm-1">Vagas</th>
					<th class="text-center col-sm-3">Ações</th>
				</tr>
				</thead>
				<tbody>
				<% if (veiculos.isEmpty()) { %>
				<tr>
					<td colspan="5">
						Você não possui veículos cadastrados.<br> 
						Deseja oferecer caronas? Então 
						<a href="${pageContext.request.contextPath}/veiculo/criar">
							cadastre um veículo
						</a>.
					</td>
				</tr>
				<% } else { %>
					<% for (Row veiculo : veiculos) { %>
						<% if (veiculo.getBoolean("ativo")) { %>
						<tr>
							<td><%= veiculo.getString("modelo") %></td>
							<td><%= veiculo.getString("placa") %></td>
							<td><%= veiculo.getString("cor") %></td>
							<td><%= veiculo.getInt("vagas") %></td>
							<td>
								<a href="${pageContext.request.contextPath}/veiculo/ver?id=<%= veiculo.getInt("id") %>"
									class="btn btn-link">
									Ver
								</a>
							</td>
						</tr>
						<% } %>
					<% } %>
				<% } %>
				</tbody>
			</table>
			
			<div class="text-right">
				<a href="${pageContext.request.contextPath}/veiculo/criar" 
				class="btn btn-link">
					Cadastrar um veículo
				</a>
			</div>
		</div>
	</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  </body>
</html>