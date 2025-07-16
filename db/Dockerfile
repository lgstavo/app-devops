# Usa a imagem oficial do MySQL 8.0 como ponto de partida.
FROM mysql:8.0

# Copia o nosso script de inicialização (init.sql) do contexto de build (a pasta 'db')
# para a pasta especial '/docker-entrypoint-initdb.d/' dentro do contêiner.
# O MySQL executará automaticamente qualquer script .sql nesta pasta na primeira inicialização.
COPY init.sql /docker-entrypoint-initdb.d/