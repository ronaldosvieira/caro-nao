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
  <% RecordSet usuariosCarona = (RecordSet) request.getAttribute("usuariosCarona"); %>
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
			
			<h4>Adicionar usuário à carona</h4>
			
			<form action="${pageContext.request.contextPath}/carona/adicionar-usuario?id=<%= carona.get(0).getInt("id") %>"
				method="post" class="form-horizontal">
				<div class="form-group">
					<label for="nome">Email do usuário</label>
					<input type="email" name="email" class="form-control text-center" 
					placeholder="ex: tio-do-bandejao@ufrrj.br" required>
				</div>
				
				<div class="form-group">
					<label for="logradouro_id">Parada</label>
					<select name="logradouro_id" 
					class="form-control logradouro_id" required>
					<option value="" class="text-center">
						(Escolha uma parada)
					</option>
					<% for (Row usuarioCarona : usuariosCarona) { %>
						<option value="<%= usuarioCarona.getInt("logradouro_id") %>" 
							class="text-center">
							<%= usuarioCarona.getString("logradouro") %>
						</option>
					<% } %>
					<option value="-1" class="text-center">
						(Nova parada - Inserir CEP abaixo)
					</option>
					</select>
				</div>
				
				<div class="form-group">
					<label for="cep">CEP</label>
					<input type="text" name="cep" 
					class="form-control text-center cep" 
					placeholder="ex: 00000-000" disabled>
				</div>
				
				<div class="form-group">
					<button class="btn btn-default btn-block" 
						type="submit">Adicionar usuário</button>
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
    <script>
   		$('.logradouro_id')
	    	.change(function() {
	    		var value = $(this).find('option:selected').val();
	    		
	    		$('.cep').prop('disabled', value != -1);
				$('.cep').prop('required', value == -1);
	    	});
    </script>
  </body>
</html>