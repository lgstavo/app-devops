<template>
  <div>
    <div>
      <h1>Página de Login</h1>
    </div>

    <div class="login-container">
      <form class="login-form" @submit.prevent="handleLogin">
        <h2>Login</h2>
        <div class="form-group">
          <label for="username">Usuário</label>
          <input
            type="text"
            id="username"
            v-model="username"
            required
            placeholder="Digite seu nome de usuário"
          />
        </div>
        <div class="form-group">
          <label for="password">Senha</label>
          <input
            type="password"
            id="password"
            v-model="password"
            required
            placeholder="Digite sua senha"
          />
        </div>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <button type="submit">Entrar</button>
        <div class="register-link">
          <p>Não tem uma conta? <RouterLink to="/register">Registre-se</RouterLink></p>
        </div>
      </form>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
const username = ref('')
const password = ref('')

const errorMessage = ref('')
import { useRouter, RouterLink } from 'vue-router'
import api from '@/services/api'

const router = useRouter()

const handleLogin = async () => {
  try {
    const response = await api.post('/api/auth/login', {
      username: username.value,
      password: password.value,
    });

    const receivedUsername = response.data.username;
    const token = response.data.token;

    if (token && receivedUsername) {
      localStorage.setItem('authToken', token);
      localStorage.setItem('username', receivedUsername); // Salva o nome de usuário recebido

      console.log('1. LOGIN: Token salvo no localStorage:', token);

      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      await router.push('/dashboard');
    }

  } catch (error) {
    console.error('Falha no Login: ', error);
    errorMessage.value = 'Usuário ou senha inválidos. Tente novamente.';
  }
};


</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}

.login-form {
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  background-color: var(--color-background-soft);
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: var(--color-heading);
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background-color: var(--color-background);
  color: var(--color-text);
}

button {
  width: 100%;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: hsla(160, 100%, 37%, 0.8);
}

.error-message {
  color: #f44336;
  text-align: center;
  margin-bottom: 1rem;
}

.register-link {
  text-align: center;
  margin-top: 1.5rem;
}

.register-link a {
  font-weight: bold;
  color: hsla(160, 100%, 37%, 1);
}
</style>
