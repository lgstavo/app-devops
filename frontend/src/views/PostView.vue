<template>
  <div>
    <div class="container">
      <header>
        <h2>Escreva algo para seus amigos!</h2>
        <div class="nav-buttons">
          <button @click="sendPost(postContent)" :class="{ active: activeView === 'requests' }">
            Postar
          </button>
        </div>
      </header>
    </div>

    <div class="container">
        <header>
            <div>
                <input type="text" placeholder="Escreva algo para seus amigos!"></input>
            </div>
        </header>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from 'vue-router';
import api from "@/services/api.js";

const router = useRouter();

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

async function sendPost(content) {
  try {
    await api.post('/api/posts', { postContent: content });
    alert('Post enviado!');
    // Aqui você pode adicionar lógica para atualizar a lista de posts exibida
  } catch (err) {
    console.error('Erro ao enviar post:', err);
    alert('Não foi possível enviar o post.');
  }
}
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
