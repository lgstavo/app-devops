<template>
  <div class="dashboard-wrapper">
    <div class="container">
      <header class="dashboard-header">
        <h2>Bem-vindo, <span class="highlight">{{ currentUsername }}</span>!</h2>
        
        <nav class="nav-buttons">
          <button @click="activeView = 'users'" :class="{ active: activeView === 'users' }">
            Ver Usuários
          </button>
          <button @click="activeView = 'requests'" :class="{ active: activeView === 'requests' }">
            Ver Solicitações ({{ requestsList.length }})
          </button>
          <button @click="activeView = 'friends'" :class="{ active: activeView === 'friends' }">
            Ver Amigos ({{ friendsList.length }})
          </button>
          <button @click="activeView = 'posts'" :class="{ active: activeView === 'posts' }">
            Ver Postagens ({{ postsList.length }})
          </button>
          <button @click="logout" class="logout-button">Logout</button>
        </nav>
      </header>

      <div v-if="loading.initial" class="status-message loading-message">
        <span class="spinner"></span> Carregando dados do dashboard...
      </div>
      <div v-if="error" class="status-message error-message">{{ error }}</div>

      <main class="content-section" v-if="!loading.initial">
        <!-- Users Section -->
        <section v-if="activeView === 'users'" class="view-section fade-in">
          <h3>Usuários para Adicionar</h3>
          <ul v-if="userList.length > 0">
            <li v-for="user in userList" :key="user.id" class="list-item">
              <span class="item-name">{{ user.username }}</span>
              <button @click="sendFriendRequest(user.id)" class="action-button add-button">Adicionar Amigo</button>
            </li>
          </ul>
          <p v-else class="empty-message">Não há novos usuários para adicionar no momento.</p>
        </section>

        <!-- Requests Section -->
        <section v-if="activeView === 'requests'" class="view-section fade-in">
          <h3>Solicitações de Amizade Recebidas</h3>
          <ul v-if="requestsList.length > 0">
            <li v-for="request in requestsList" :key="request.requestId" class="list-item">
              <span class="item-name">{{ request.fromUsername }}</span>
              <div class="action-group">
                <button @click="respondToRequest(request.requestId, 'aceita')" class="action-button accept-button">Aceitar</button>
                <button @click="respondToRequest(request.requestId, 'recusada')" class="action-button reject-button">Recusar</button>
              </div>
            </li>
          </ul>
          <p v-else class="empty-message">Você não tem nenhuma solicitação de amizade pendente.</p>
        </section>

        <!-- Friends Section -->
        <section v-if="activeView === 'friends'" class="view-section fade-in">
          <h3>Meus Amigos</h3>
          <ul v-if="friendsList.length > 0">
            <li v-for="friend in friendsList" :key="friend.id" class="list-item">
              <span class="item-name">{{ friend.username }}</span>
              <span class="friend-badge">Amigo</span>
            </li>
          </ul>
          <p v-else class="empty-message">Você ainda não tem amigos.</p>
        </section>

        <!-- Posts Section -->
        <section v-if="activeView === 'posts'" class="view-section fade-in">
          <h3>Postagens Recentes</h3>
          <ul v-if="postsList.length > 0" class="posts-list">
            <li v-for="post in postsList" :key="post.id" class="post-card">
              <div class="post-header">
                <span class="item-name">{{ post.username }}</span>
              </div>
              <p class="post-content">{{ post.content }}</p>
            </li>
          </ul>
          <p v-else class="empty-message">Nenhuma postagem encontrada.</p>
        </section>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from 'vue-router';
import api from "@/services/api.js";

const router = useRouter();

const currentUsername = ref('');
const activeView = ref('users');

const userList = ref([]);
const friendsList = ref([]);
const requestsList = ref([]);
const postsList = ref([]);
const loading = ref({ initial: true });
const error = ref(null);

async function fetchUsers() {
  try {
    const response = await api.get('/api/users');
    userList.value = response.data;
  } catch (err) {
    console.error('Erro ao buscar usuários: ', err);
    error.value = "Não foi possível carregar a lista de usuários.";
  }
}

async function fetchRequests() {
  try {
    const response = await api.get('/api/friend-requests/received');
    requestsList.value = response.data;
  } catch (err) {
    console.error('Erro ao buscar solicitações:', err);
    error.value = "Não foi possível carregar as solicitações de amizade.";
  }
}

async function fetchFriends() {
  try {
    const response = await api.get('/api/friend-requests/friends');
    friendsList.value = response.data;
  } catch (err) {
    console.error('Erro ao buscar amigos:', err);
    error.value = "Não foi possível carregar a lista de amigos.";
  }
}

async function fetchPosts() {
  try {
    const response = await api.get('/api/postagens');
    postsList.value = response.data;
  } catch (err) {
    console.error('Erro ao buscar posts:', err);
    error.value = 'Não foi possível carregar os posts.';
  }
}

async function sendFriendRequest(userId) {
  try {
    await api.post('/api/friend-requests', { toUserId: userId });
    alert('Pedido de amizade enviado!');
    userList.value = userList.value.filter(user => user.id !== userId);
  } catch (err) {
    console.error('Erro ao enviar pedido:', err);
    alert(err.response?.data || 'Não foi possível enviar o pedido. Talvez já exista um.');
  }
}

async function respondToRequest(requestId, status) {
  try {
    await api.post('/api/friend-requests/respond', { requestId, status });
    alert(`Pedido ${status === 'aceita' ? 'aceito' : 'recusado'} com sucesso!`);
    requestsList.value = requestsList.value.filter(req => req.requestId !== requestId);
    if (status === 'aceita') {
      await fetchFriends();
    }
  } catch (err) {
    console.error('Erro ao responder pedido:', err);
    alert('Não foi possível responder ao pedido.');
  }
}

const logout = () => {
  localStorage.removeItem('authToken');
  localStorage.removeItem('username');
  delete api.defaults.headers.common['Authorization'];
  router.push('/login');
};

onMounted(async () => {
  const storedUsername = localStorage.getItem('username');
  if (storedUsername) {
    currentUsername.value = storedUsername;
  } else {
    logout();
    return;
  }

  await Promise.all([
    fetchUsers(),
    fetchRequests(),
    fetchFriends(),
    fetchPosts(),
  ]);

  loading.value.initial = false;
});
</script>

<style scoped>
/* Global-like styles wrapped in the main div to keep it scoped */
.dashboard-wrapper {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
  background-color: #f0f2f5;
  color: #333;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  padding: 40px 20px;
  box-sizing: border-box;
}

.container {
  background-color: #fff;
  padding: 30px 40px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  width: 100%;
  max-width: 800px;
}

/* Header & Typography */
.dashboard-header {
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid #dddfe2;
}

.dashboard-header h2 {
  margin: 0 0 20px 0;
  font-size: 1.8em;
  color: #1d2129;
}

.highlight {
  color: #007bff;
}

h3 {
  color: #1d2129;
  font-size: 1.3em;
  margin-bottom: 15px;
}

/* Navigation */
.nav-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.nav-buttons button {
  flex: 1;
  min-width: max-content;
  padding: 10px 16px;
  background-color: #f0f2f5;
  color: #4b4f56;
  border: 1px solid transparent;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.95em;
  font-weight: 600;
  transition: all 0.2s ease;
}

.nav-buttons button:hover {
  background-color: #e4e6eb;
}

.nav-buttons button.active {
  background-color: #007bff;
  color: white;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
}

.nav-buttons button.logout-button {
  background-color: transparent;
  color: #dc3545;
  border: 1px solid #dc3545;
  flex: 0 0 auto;
}

.nav-buttons button.logout-button:hover {
  background-color: #dc3545;
  color: white;
}

/* Lists and Items */
ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  margin-bottom: 12px;
  background-color: #fff;
  border: 1px solid #e1e4e8;
  border-radius: 8px;
  transition: box-shadow 0.2s ease;
}

.list-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.item-name {
  font-weight: 600;
  font-size: 1.05em;
  color: #1d2129;
}

/* Buttons inside lists */
.action-group {
  display: flex;
  gap: 8px;
}

.action-button {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9em;
  font-weight: 600;
  color: white;
  transition: background-color 0.2s;
}

.add-button {
  background-color: #007bff;
}
.add-button:hover { background-color: #0056b3; }

.accept-button {
  background-color: #28a745;
}
.accept-button:hover { background-color: #1e7e34; }

.reject-button {
  background-color: #dc3545;
}
.reject-button:hover { background-color: #b02a37; }

/* Posts UI */
.posts-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.post-card {
  padding: 20px;
  background-color: #f7f8fa;
  border: 1px solid #e1e4e8;
  border-radius: 8px;
}

.post-header {
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e1e4e8;
}

.post-content {
  margin: 0;
  color: #4b4f56;
  line-height: 1.5;
}

/* Badges & Text */
.friend-badge {
  background-color: #e4e6eb;
  color: #4b4f56;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.85em;
  font-weight: 600;
}

.empty-message {
  text-align: center;
  color: #888;
  font-style: italic;
  padding: 30px 0;
}

.status-message {
  text-align: center;
  padding: 20px;
  border-radius: 8px;
  margin-top: 20px;
  font-weight: 600;
}

.loading-message {
  color: #007bff;
  background-color: #e7f1ff;
}

.error-message {
  color: #dc3545;
  background-color: #f8d7da;
}

/* Animation */
.fade-in {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(5px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>