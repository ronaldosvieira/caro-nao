<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/style.css">
<title>Criar grupo de carona - Caronão</title>
</head>
<body>
	<div class="center-content">
		<br>
		<br>
		<br>
		<br>
		<br>
		<form action="CriacaoGrupo" class="formExtended formMedium" method="post">
			<h2>Criar grupo de carona</h2>
			<label>Nome:</label>
			<input type="text" name="nome">
			<br>
			<label>Descrição:</label>
			<textarea name="descricao"></textarea>
			<br>
			<label>Lista de regras:</label>
			<textarea name="regras"></textarea>
			<br>
			<label>Limite mínimo de avaliações ruins para inativar usuário:</label>
			<input type="text" name="limitAval">
			<br><br>
			<input type="submit" value="Criar">
		</form> 
	</div>
</body>
</html>