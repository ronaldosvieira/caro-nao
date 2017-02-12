<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/style.css">
<title>Novo cadastro de usuário</title>
</head>
<body>
	<div class="center-content">
		<br>
		<br>
		<br>
		<br>
		<br>
		<form action="CriacaoUsuario" class="formExtended formMedium" method="post">
			<h2>Cadastro de usuário</h2>
			<label>Nome:</label>
			<input type="text" name="nome">
			<br>
			<label>Email:</label>
			<input type="text" name="email">
			<!-- 
			<br>
			<label>Senha:</label>
			<input type="password" name="senha">
			-->
			<br>
			<label>Telefone:</label>
			<input type="text" name="telefone" maxlength="13">
			<br><br>
			<input type="submit" value="Cadastrar">
		</form> 
	</div>
</body>
</html>