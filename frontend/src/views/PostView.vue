<template>
  <div>
    <div class="container">
      <header>
        <h2>Escreva algo para seus amigos!</h2>
        <div class="nav-buttons">
          <input
            v-model="postContent"
            type="text"
            placeholder="Escreva algo para seus amigos!"
          />
          <button @click="sendPost" :disabled="!postContent.trim()">
            Postar
          </button>
        </div>
      </header>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import api from "@/services/api.js";

const postContent = ref('');
const error = ref(null);

async function sendPost() {
  if (!postContent.value.trim()) return;

  try {
    await api.post('/api/postagens', { descricao: postContent.value });
    alert('Post enviado!');
    postContent.value = ''; // clear the input after posting
  } catch (err) {
    console.error('Erro ao enviar post:', err);
    error.value = 'Não foi possível enviar o post.';
    alert(error.value);
  }
}
</script>

<style scoped>
</style>