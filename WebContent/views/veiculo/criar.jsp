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
  
  <% String erro = (String) request.getAttribute("erro"); %>
  
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
			
			<h4>Cadastro de novo veículo</h4>
			
			<form action="${pageContext.request.contextPath}/veiculo/criar"
				method="post" class="form-horizontal">
				<div class="form-group">
					<label for="modelo">Modelo</label>
					<input type="text" name="modelo" class="form-control text-center" 
					placeholder="ex: Fiat Uno" required>
				</div>
				
				<div class="form-group">
					<label for="placa">Placa</label>
					<input type="text" name="placa" class="form-control text-center" 
					placeholder="ex: JDS-8723" required>
				</div>
				
				<div class="form-group">
					<label for="cor">Cor</label>
					<input type="text" name="cor" class="form-control text-center" 
					placeholder="ex: Prata" required>
				</div>
				
				<div class="form-group">
					<label for="vagas">Quantidade de vagas</label>
					<input type="number" min="1" name="vagas" 
					class="form-control text-center" 
					placeholder="ex: 4" required>
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