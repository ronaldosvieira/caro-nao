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
  
  <%@ page import = "util.RecordSet" %>
  <%@ page import = "util.Row" %>
  
  <% String erro = (String) request.getAttribute("erro"); %>
  <% RecordSet veiculos = (RecordSet) request.getAttribute("veiculos"); %>
  
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
			
			<h4>Criar carona</h4>
			
			<form action="${pageContext.request.contextPath}/carona/criar"
				method="post" class="form-horizontal">
				<div class="form-group">
					<label for="veiculo_id">Veículo</label>
					<select name="veiculo_id" class="form-control">
					<option value="" class="text-center" required>
						(Escolha um veículo)
					</option>
					<% for (Row veiculo : veiculos) { %>
						<option value="<%= veiculo.getInt("id") %>" 
							class="text-center" required>
							<%= veiculo.getString("modelo") %>
						</option>
					<% } %>
					</select>
				</div>
				
				<div class="form-group">
					<label for="data">Data</label>
					<input type="date" name="data" 
					class="form-control text-center" 
					placeholder="ex: 13/02/2017" required>
				</div>
				
				<div class="form-group">
					<label for="horario">Horário</label>
					<input type="text" name="horario" 
					class="form-control text-center" 
					placeholder="ex: 15:00" required>
				</div>
				
				<h5>Logradouro de origem</h5>
				
				<div class="form-group">
					<label for="cep_origem">CEP</label>
					<input type="text" name="cep_origem" 
					class="form-control text-center" 
					placeholder="ex: 00000-000" required>
				</div>
				
				<div class="form-group">
					<label for="numero_origem">Número</label>
					<input type="text" name="numero_origem" 
					class="form-control text-center" 
					placeholder="ex: 324" required>
				</div>
				
				<h5>Logradouro de destino</h5>
				
				<div class="form-group">
					<label for="cep_destino">CEP</label>
					<input type="text" name="cep_destino" 
					class="form-control text-center" 
					placeholder="ex: 00000-000" required>
				</div>
				
				<div class="form-group">
					<label for="numero_destino">Número</label>
					<input type="text" name="numero_destino" 
					class="form-control text-center" 
					placeholder="ex: 324" required>
				</div>
				
				<div class="form-group">
					<button class="btn btn-default btn-block" 
						type="submit">Cadastrar</button>
					<a href="${pageContext.request.contextPath}/dashboard" 
						class="btn btn-block btn-link">Voltar</a>
				</div>
			</form>
		</div>
	</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  </body>
</html>