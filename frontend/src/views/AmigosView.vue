<template>
  <div class="friends-container">
    <h1>Meus Amigos</h1>

    <div v-if="isLoading" class="loading">
      Carregando lista de amigos...
    </div>

    <div v-else-if="error" class="error-message">
      {{ error }}
    </div>

    <div v-else>
      <ul v-if="friends.length > 0" class="friends-list">
        <li v-for="friend in friends" :key="friend.id" class="friend-item">
          <span class="friend-name">{{ friend.username }}</span>
          <button @click="removeFriend(friend.id)" class="remove-button">
            Desfazer Amizade
          </button>
        </li>
      </ul>
      <p v-else class="no-friends-message">
        Você ainda não tem amigos. Que tal enviar alguns convites?
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '@/services/api';

// 1. Variáveis de Estado Reativas
// Para guardar a lista de amigos que virá da API.
const friends = ref([]);
// Para controlar a exibição da mensagem de "Carregando...".
const isLoading = ref(true);
// Para guardar qualquer mensagem de erro que ocorra.
const error = ref(null);

// 2. Função para buscar os dados no Backend
const fetchFriends = async () => {
  try {
    // Chama o endpoint GET que criamos no FriendRequestController
    const response = await api.get('/api/friend-requests/friends');
    // Se a chamada for bem-sucedida, atualiza nossa variável reativa com os dados
    friends.value = response.data;
  } catch (err) {
    // Se a chamada falhar, guarda a mensagem de erro
    console.error("Erro ao buscar amigos:", err);
    error.value = "Não foi possível carregar a lista de amigos. Tente novamente mais tarde.";
  } finally {
    // Este bloco é executado sempre, seja com sucesso ou erro.
    // Usamos para garantir que a mensagem de "Carregando..." desapareça.
    isLoading.value = false;
  }
};

// 3. Gancho de Ciclo de Vida
// onMounted garante que a função fetchFriends será chamada
// uma única vez, assim que o componente for montado na tela.
onMounted(fetchFriends);

// 4. Função de Ação (Placeholder)
const removeFriend = (friendId) => {
  // A lógica para remover um amigo seria implementada aqui.
  // Envolveria uma chamada à API, como por exemplo: api.delete(`/api/friends/${friendId}`)
  alert(`Lógica para remover o amigo com ID ${friendId} seria implementada aqui.`);
};
</script>

<style scoped>
.friends-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
}

.loading, .error-message, .no-friends-message {
  text-align: center;
  padding: 1rem;
  font-size: 1.1rem;
}

.error-message {
  color: #f44336;
}

.friends-list {
  list-style: none;
  padding: 0;
}

.friend-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  margin-bottom: 1rem;
  background-color: var(--color-background-soft);
}

.friend-name {
  font-weight: bold;
}

.remove-button {
  padding: 0.5rem 1rem;
  border: 1px solid #f44336;
  border-radius: 4px;
  background-color: transparent;
  color: #f44336;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
}

.remove-button:hover {
  background-color: #f44336;
  color: white;
}
</style>
