import axios from 'axios'

// A baseURL agora é apenas '/', pois o Nginx está na mesma origem
// e vai redirecionar as chamadas para /api/
const api = axios.create({
  baseURL: '/',
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api