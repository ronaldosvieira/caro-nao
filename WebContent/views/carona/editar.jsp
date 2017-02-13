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
    
    <!-- CSS do caron�o -->
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
  <% RecordSet carona = (RecordSet) request.getAttribute("carona"); %>
  
	<header class="text-center">
		<h1>
			Caro<span style="color: #DDD;">,</span>n�o
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
			
			<h4>Editar ve�culo de carona</h4>
			
			<form action="${pageContext.request.contextPath}/carona/editar?id=<%= carona.get(0).getInt("id") %>"
				method="post" class="form-horizontal">
				<div class="form-group">
					<label for="veiculo_id">Ve�culo</label>
					<select name="veiculo_id" class="form-control" required>
					<option value="" class="text-center">
						(Escolha um ve�culo)
					</option>
					<% for (Row veiculo : veiculos) { %>
						<option value="<%= veiculo.getInt("id") %>" 
							class="text-center" required
							<%= veiculo.getInt("id") == 
								carona.get(0).getInt("veiculo_id")?
										"selected" : "" %>>
							<%= veiculo.getString("modelo") %>
						</option>
					<% } %>
					</select>
				</div>
				
				<div class="form-group">
					<button class="btn btn-default btn-block" 
						type="submit">Salvar</button>
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