create table grupo (
	id serial primary key,
	nome varchar(45) not null,
	descricao varchar(255) not null,
	regras text not null,
	limite_avaliacoes_negativas integer not null,
	ativo boolean not null default true,
	criado_em timestamp not null default NOW(),
	alterado_em timestamp
);

create table usuario (
	id serial primary key,
	nome varchar(45) not null,
	email varchar(45) not null,
	telefone varchar(45) not null
);

create table grupo_usuario (
	id serial primary key,
	grupo_id integer not null references grupo (id),
	usuario_id integer not null references usuario (id),
	ativo boolean not null default true
);

create table avaliacao (
	id serial primary key,
	avaliador_id integer not null 
		references grupo_usuario (id),
	avaliado_id integer not null
		references grupo_usuario (id),
	nota integer not null,
	data timestamp not null default NOW()
);

create table veiculo (
	id serial primary key,
	modelo varchar(45) not null,
	placa varchar(45) not null,
	vagas integer not null,
	usuario_id integer not null references usuario (id),
	ativo boolean not null default true
);

create table logradouro (
	id serial primary key,
	cep varchar(45) not null,
	estado varchar(2) not null,
	distrito varchar(45),
	endereco varchar(255) not null,
	numero varchar(45) not null
);

create table estado_carona (
	id serial primary key,
	nome varchar(45) not null,
	pode_entrar boolean not null,
	pode_avaliar boolean not null
);

insert into estado_carona (nome, pode_entrar, pode_avaliar) values 
	('Ativa', true, false), 
	('Cancelada', false, false),
	('Concluída', false, true);

create table carona (
	id serial primary key,
	veiculo_id integer not null references veiculo (id),
	horario timestamp not null,
	logradouro_origem_id integer not null 
		references logradouro (id),
	logradouro_destino_id integer not null 
		references logradouro (id),
	estado_carona_id int not null default 1
		references estado_carona (id)
);
