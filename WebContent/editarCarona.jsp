<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/style.css">
<title>Editar carona - Caronão</title>
</head>
<body>
	<div class="center-content">
		<br>
		<br>
		<br>
		<br>
		<br>
		<form action="EditarCarona" class="formExtended formMedium" method="post">
			<h2>Editar carona</h2>
			<p>Dia: DIA / MES / ANO</p> 
			<p>Horário de saída: HORA horas e MINUTO minutos</p>
			<p>CEP do logradouro de origem: CEP</p>
			<p>CEP do logradouro de destino: CEP</p>
			<p>Veículo da carona:</p>
			<div class="selectBox">
  				<input type="radio" name="veiculo" value="placa1" id="placa1" checked> <label for="placa1">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa2" id="placa2"> <label for="placa2">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa3" id="placa3"> <label for="placa3">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa4" id="placa4"> <label for="placa4">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa5" id="placa5"> <label for="placa5">Modelo de cor Cor e placa Placa</label><br>
  				<input type="radio" name="veiculo" value="placa6" id="placa6"> <label for="placa6">Modelo de cor Cor e placa Placa</label><br>
			</div>
			
			<br>
			<input type="submit" value="Editar">
		</form> 
	</div>
</body>
</html>