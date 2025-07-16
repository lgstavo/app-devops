<template>
  <div>
    <h1>Página de Cadastro</h1>

    <div class="register-container">
      <form class="register-form" @submit.prevent="handleRegister">
        <h2>Registro de Usuário</h2>

        <div class="form-group">
          <label for="username">Usuário</label>
          <input type="text" id="username" v-model="username" required placeholder="Insira um nome de usuário"/>
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="email" required placeholder="Insira um e-mail"/>
        </div>

        <div class="form-group">
          <label for="password">Senha</label>
          <input type="password" id="password" v-model="password" required placeholder="Insira uma senha"/>
        </div>

        <div class="form-group">
          <label for="confirmPassword">Confirmar Senha</label>
          <input type="password" id="confirmPassword" v-model="confirmPassword" required placeholder="Confirme sua senha"/>
        </div>

        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <button type="submit">Cadastrar</button>

        <div class="login-link">
          <p>Já tem uma conta? <RouterLink to="/login">Faça Login</RouterLink></p>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const username = ref('')
const password = ref('')
const email = ref('')
const confirmPassword = ref('')
const errorMessage = ref('')
import { useRouter, RouterLink } from 'vue-router'
import api from '@/services/api'

const router = useRouter()

const handleRegister = async () => {
  // Limpa erros antigos
  errorMessage.value = ''

  // Validação de senhas
  if (confirmPassword.value !== password.value) {
    errorMessage.value = 'As senhas não coincidem.'
    return
  }

  try {
    // Chamada à API continua a mesma
    await api.post('/api/auth/register', {
      username: username.value,
      password: password.value,
      email: email.value, // Assumindo que você implementou o email no backend
    })

    alert('Usuário cadastrado com sucesso! Por favor, faça o login.');
    await router.push('/login'); // Redireciona para /login, não /dashboard

  } catch (error) {
    if (error.response && error.response.status === 409) {
      errorMessage.value = 'Este nome de usuário ou email já está em uso.'
    } else {
      console.error('Falha no Registro: ', error)
      errorMessage.value = 'Ocorreu um erro durante o cadastro. Tente novamente.'
    }
  }
}
</script>



<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}

.register-form {
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

.login-link {
  text-align: center;
  margin-top: 1.5rem;
}

.login-link a {
  font-weight: bold;
  color: hsla(160, 100%, 37%, 1);
}
</style>
