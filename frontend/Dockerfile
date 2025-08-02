# Estágio 1: Build da aplicação Vue.js
FROM node:20-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
# O comando 'build' irá compilar seu app Vue para arquivos estáticos
RUN npm run build

# Estágio 2: Servir os arquivos estáticos com Nginx
FROM nginx:stable-alpine as production-stage
# Copia os arquivos compilados do estágio de build para a pasta do Nginx
COPY --from=build-stage /app/dist /usr/share/nginx/html
# Copia nosso arquivo de configuração do Nginx para o lugar certo
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Expõe a porta 80 (padrão do Nginx)
EXPOSE 80
# Comando para iniciar o Nginx
CMD ["nginx", "-g", "daemon off;"]