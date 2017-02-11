# Caronão
Carona (ou não ( ͡° ͜ʖ ͡°)).

Caro? Não.

Cara, carona cara, cara? Pô.

## Padrões arquiteturais
Camada de lógica de negócio: Módulo de tabela

Camada de dados: Portão de acesso de dados por tabelas

## Requisitos
### Grupo
- Criar grupo
- Alterar grupo
- Visualizar grupo
- Desativar grupo
- Convidar usuário

### Usuário
- Criar usuário
- Alterar usuário
- Visualizar usuário
- Criar veículo
- Alterar veículo

### Carona
- Criar carona
- Alterar carona
- Candidatar-se à carona
- Criar ponto de parada
- Desistir da carona
- Cancelar carona
- Encerrar carona

## Regras de negócio
- O usuário que criar um grupo será automaticamente incluído nele.
- Apenas o último usuário ativo em um grupo pode desativá-lo
- Um grupo será desativado automaticamente quando não tiver usuários ativos
- Uma avaliação considerar ruim é uma com menos de três estrelas
- Se um usuário receber mais avaliações negativas do que o limite definido pelo grupo se tornará inativo
- Um usuário inativo em um grupo não tem acesso a nenhuma informação dele
- Um motorista é um usuário que possui um ou mais veículos
- Apenas um motorista pode criar caronas
- O criador de uma carona pode alterar o veículo utilizado, desde que não reduza o número de vagas disponíveis
- O criador de uma carona pode alterar o horário de saída, desde que o veículo e o usuário não estejam associados a nenhuma carona no mesmo dia e horário
- O criador de uma carona pode alterar a origem e destino de uma carona, desde que não existam passageiros na carona
- Um usuário pode se candidatar como passageiro de uma carona, desde que exista vagas disponíveis no veículo
- Os usuários de uma carona podem avaliar uns aos outros somente após o encerramento da carona

## Requisitos não-funcionais
- O sistema deverá encontrar um endereço a partir de um CEP através da api https://viacep.com.br
