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
	
	<% 
	String erro = (String) request.getAttribute("erro");
	RecordSet grupo = (RecordSet) request.getAttribute("grupo");
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
			
			<h4>Editar grupo</h4>
			
			<form action="${pageContext.request.contextPath}/grupo/editar?id=<%= grupo.get(0).getInt("id") %>"
				method="post" class="form-horizontal">
				<div class="form-group">
					<label for="nome">Nome</label>
					<input type="text" name="nome" class="form-control text-center" 
					placeholder="ex: Caroneiros(as) do IM" required
					value="<%= grupo.get(0).getString("nome") %>">
				</div>
				
				<div class="form-group">
					<label for="descricao">Descricao</label>
					<input type="text" name="descricao" 
					class="form-control text-center" 
					placeholder="ex: Grupo de caronas do IM" required
					value="<%= grupo.get(0).getString("descricao") %>">
				</div>
				
				<div class="form-group">
					<label for="regras">Regras</label>
					<textarea name="regras" 
					class="form-control mostrar-tooltip" required readonly
					data-toggle="tooltip" data-placement="bottom"
					title="Não é possível alterar as regras do grupo"
					><%= grupo.get(0).getString("regras") %></textarea>
				</div>
				
				<div class="form-group">
					<label for="limite">Limite de avaliações negativas</label>
					<input type="number" min="1" name="limite" 
					class="form-control text-center" 
					placeholder="ex: 4" required
					value="<%= grupo.get(0).getInt("limite_avaliacoes_negativas") %>">
				</div>
				
				<div class="form-group">
					<button class="btn btn-default btn-block" 
						type="submit">Salvar</button>
					<a href="${pageContext.request.contextPath}/grupo/ver?id=<%= grupo.get(0).getInt("id") %>" 
						class="btn btn-block btn-link">Voltar</a>
				</div>
			</form>
		</div>
	</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script>
    	$('.mostrar-tooltip').tooltip();
    </script>
  </body>
</html>