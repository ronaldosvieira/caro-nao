insert into grupo (nome, descricao, regras, limite_avaliacoes_negativas)
values ('Grupo de carona #1', 'Carona po', 'Nenhuma', 3),
	('Grupo vazio', '-', 'Nenhuma', 1);

insert into usuario (nome, email, telefone)
values ('Rodrigo', 'rodrigovicente@gmail.com', '90909090'),
	('Ronaldo', 'ronaldo.vieira@ufrrj.br', '89898989');
	
insert into grupo_usuario (grupo_id, usuario_id)
values (1, 1);

insert into veiculo (modelo, placa, cor, vagas, usuario_id)
values ('Fiat Uno', 'SOD-4394', 'Azul marinho', 4, 1),
	('Veloster', 'NWE-4821', 'Vermelho', 3, 1);

insert into logradouro (cep, estado, cidade, distrito, endereco, numero)
values ('26170230', 'RJ', 'Belford Roxo', 'Pauline', 'Rua Acre', '28'),
	('29238237', 'RJ', 'Rio de Janeiro', 'Cidade Universitária', 'Rua Helio de Almeida', 's/n');

insert into carona (veiculo_id, dia_horario, logradouro_origem_id, logradouro_destino_id)
values (1, NOW(), 1, 2);

insert into carona_usuario (usuario_id, carona_id, logradouro_id)
values (1, 1, 2);