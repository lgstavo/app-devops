<template>
  <div>
    <div class="container">
      <header>
        <h2>Bem-vindo, <span>{{ currentUsername }}</span>!</h2>
        <div class="nav-buttons">
          <button @click="activeView = 'users'" :class="{ active: activeView === 'users' }">Ver Usuários</button>
          <button @click="activeView = 'requests'" :class="{ active: activeView === 'requests' }">
            Ver Solicitações ({{ requestsList.length }})
          </button>
          <button @click="activeView = 'friends'" :class="{ active: activeView === 'friends' }">
            Ver Amigos ({{ friendsList.length }})
          </button>
          <button @click="logout" class="logout-button">Logout</button>
        </div>
      </header>

      <div v-if="loading.initial" class="loading-message">Carregando dados do dashboard...</div>
      <div v-if="error" class="error-message">{{ error }}</div>

      <section v-if="activeView === 'users'">
        <h3>Usuários para Adicionar</h3>
        <ul v-if="userList.length > 0">
          <li v-for="user in userList" :key="user.id" class="list-item">
            <span class="item-name">{{ user.username }}</span>
            <button @click="sendFriendRequest(user.id)" class="action-button add-button">Adicionar Amigo</button>
          </li>
        </ul>
        <p v-else class="empty-message">Não há novos usuários para adicionar no momento.</p>
      </section>

      <section v-if="activeView === 'requests'">
        <h3>Solicitações de Amizade Recebidas</h3>
        <ul v-if="requestsList.length > 0">
          <li v-for="request in requestsList" :key="request.requestId" class="list-item">
            <span class="item-name">{{ request.fromUsername }}</span>
            <div>
              <button @click="respondToRequest(request.requestId, 'aceita')" class="action-button accept-button">Aceitar</button>
              <button @click="respondToRequest(request.requestId, 'recusada')" class="action-button reject-button">Recusar</button>
            </div>
          </li>
        </ul>
        <p v-else class="empty-message">Você não tem nenhuma solicitação de amizade pendente.</p>
      </section>

      <section v-if="activeView === 'friends'">
        <h3>Amigos</h3>
        <ul v-if="friendsList.length > 0">
          <li v-for="friend in friendsList" :key="friend.id" class="list-item">
            <span class="item-name">{{ friend.username }}</span>
          </li>
        </ul>
        <p v-else class="empty-message">Você ainda não tem amigos.</p>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from 'vue-router';
import api from "@/services/api.js";

const router = useRouter();

// --- VARIÁVEIS DE ESTADO REATIVAS ---
const currentUsername = ref('');
const activeView = ref('users'); // A aba inicial a ser mostrada

// Variáveis para as listas de dados
const userList = ref([]);
const friendsList = ref([]);
const requestsList = ref([]);

// Variáveis para controle de UI (loading e erros)
const loading = ref({ initial: true });
const error = ref(null);

// --- FUNÇÕES DE LÓGICA ---

// Busca a lista de usuários potenciais
async function fetchUsers() {
  try {
    const response = await api.get('/api/users');
    userList.value = response.data;
  } catch (err) {
    console.error('Erro ao buscar usuários: ', err);
    error.value = "Não foi possível carregar a lista de usuários.";
  }
}

// Busca a lista de solicitações recebidas
async function fetchRequests() {
  try {
    const response = await api.get('/api/friend-requests/received');
    requestsList.value = response.data;
  } catch (err) {
    console.error('Erro ao buscar solicitações:', err);
    error.value = "Não foi possível carregar as solicitações de amizade.";
  }
}

// Busca a lista de amigos
async function fetchFriends() {
  try {
    const response = await api.get('/api/friend-requests/friends');
    friendsList.value = response.data;
  } catch (err) {
    console.error('Erro ao buscar amigos:', err);
    error.value = "Não foi possível carregar a lista de amigos.";
  }
}

// Envia um pedido de amizade
async function sendFriendRequest(userId) {
  try {
    await api.post('/api/friend-requests', { toUserId: userId });
    alert('Pedido de amizade enviado!');
    // Atualiza a UI removendo o usuário da lista de potenciais amigos
    userList.value = userList.value.filter(user => user.id !== userId);
  } catch (err) {
    console.error('Erro ao enviar pedido:', err);
    alert(err.response?.data || 'Não foi possível enviar o pedido. Talvez já exista um.');
  }
}

// Responde a um pedido (aceitando ou recusando)
async function respondToRequest(requestId, status) {
  try {
    await api.post('/api/friend-requests/respond', { requestId, status });
    alert(`Pedido ${status === 'aceita' ? 'aceito' : 'recusado'} com sucesso!`);
    // Atualiza a UI removendo o pedido da lista
    requestsList.value = requestsList.value.filter(req => req.requestId !== requestId);
    // Se aceitou, atualiza a lista de amigos para refletir a mudança
    if (status === 'aceita') {
      await fetchFriends();
    }
  } catch (err) {
    console.error('Erro ao responder pedido:', err);
    alert('Não foi possível responder ao pedido.');
  }
}

// Faz o logout do usuário
const logout = () => {
  localStorage.removeItem('authToken');
  localStorage.removeItem('username');
  delete api.defaults.headers.common['Authorization'];
  router.push('/login');
};

// --- GANCHO DE CICLO DE VIDA ---
onMounted(async () => {
  console.log("DASHBOARD: Componente montado, buscando todos os dados...");

  const storedUsername = localStorage.getItem('username');
  if (storedUsername) {
    currentUsername.value = storedUsername;
  } else {
    // Se não houver nome de usuário, algo está errado, volte para o login
    logout();
    return;
  }

  // Promise.all executa todas as buscas de dados em paralelo para mais eficiência
  await Promise.all([
    fetchUsers(),
    fetchRequests(),
    fetchFriends()
  ]);

  // Desativa a mensagem de carregamento inicial após todas as buscas terminarem
  loading.value.initial = false;
});
</script>

<style scoped>
</style>
