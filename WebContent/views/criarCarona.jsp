<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/style.css">
<title>Criar carona - Caronão</title>
</head>
<body>
	<div class="center-content">
		<br>
		<br>
		<br>
		<br>
		<br>
		<form action="CriacaoCarona" class="formExtended formMedium" method="post">
			<h2>Criar carona</h2>
			<div class="data">
				<label>Dia:</label>
				<input type="text" name="dia"> / 
				<input type="text" name="mes"> / 
				<input type="text" name="ano">
			</div>
			<br>
			<div class="hora">
				<label>Horário de saída:</label>
				<input type="text" name="hora"> horas e 
				<input type="text" name="minuto"> minutos
			</div>
			<br>
			<div class="inputRight">
				<label>CEP do logradouro de origem:</label>
				<input type="text" name="cepOrigem">
				<br>
				<label>CEP do logradouro de destino:</label>
				<input type="text" name="cepDestino">
			</div>
			<label>Veículo da carona:</label>
			<div class="selectBox">
  				<input type="radio" name="veiculo" value="placa1" id="placa1"> <label for="placa1">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa2" id="placa2"> <label for="placa2">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa3" id="placa3"> <label for="placa3">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa4" id="placa4"> <label for="placa4">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa5" id="placa5"> <label for="placa5">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa6" id="placa6"> <label for="placa6">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa7" id="placa7"> <label for="placa7">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa8" id="placa8"> <label for="placa8">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa9" id="placa9"> <label for="placa9">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa10" id="placa10"> <label for="placa10">Modelo de cor Cor e placa Placa</label><br>
			</div>
			
			<br>
			<input type="submit" value="Criar">
		</form> 
	</div>
</body>
</html>