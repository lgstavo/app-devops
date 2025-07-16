FROM node:20-alpine AS builder
# Define o diretório de trabalho.
WORKDIR /app

# Copia os arquivos de definição de pacotes e instala as dependências.
# Aproveita o cache do Docker.
COPY package*.json ./
RUN npm install

RUN chmod +x /app/node_modules/.bin/vite

# Copia o resto do código fonte do frontend.
COPY . .

# Executa o comando de build do Vue/Vite.
# Isso gera a pasta 'dist/' com os arquivos estáticos otimizados.
RUN npm run build


FROM nginx:1.25-alpine

# Expõe a porta 80, que é a porta padrão do Nginx.
EXPOSE 80

COPY --from=builder /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

# Comando padrão para iniciar o Nginx.
CMD ["nginx", "-g", "daemon off;"]