<script>
import { RouterView } from 'vue-router'
import Sidebar from './components/Sidebar.vue'
import { sidebarWidth } from './components/state'

export default {
  components: { Sidebar },
  setup() {
    return { sidebarWidth }
  }
}
</script>

<template>
  <Sidebar />

  <main class="main-content" :style="{ 'margin-left': sidebarWidth }">
    <RouterView />
  </main>
</template>

<style>
/* Você pode adicionar este estilo globalmente ou no App.vue (sem 'scoped') */

/* Garante que a transição da margem seja suave, igual à da sidebar */
.main-content {
  transition: margin-left 0.3s ease-in-out;
  padding: 2rem; /* Adiciona um espaçamento interno para o conteúdo não ficar colado nas bordas */
}
</style>