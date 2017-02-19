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
  <% RecordSet usuario = (RecordSet) request.getAttribute("usuario"); %>
  <% RecordSet avaliado = (RecordSet) request.getAttribute("avaliado"); %>
  <% RecordSet carona = (RecordSet) request.getAttribute("carona"); %>
  
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
			
			<h4>Avaliar <%= avaliado.get(0).getString("nome") %> na carona</h4>
			
			<form action="${pageContext.request.contextPath}/carona/avaliar?id=<%= carona.get(0).getInt("id") %>&usuario_id=<%= avaliado.get(0).getInt("id") %>"
				method="post" class="form-horizontal">
				<div class="form-group">
					<label for="nota">Nota</label>
					<select name="nota" class="form-control text-center" required>
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</div>
				
				<div class="form-group">
					<button class="btn btn-default btn-block" 
						type="submit">Enviar avaliação</button>
					<a href="${pageContext.request.contextPath}/carona/ver?id=<%= carona.get(0).getInt("id") %>" 
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