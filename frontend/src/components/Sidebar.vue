<script>
import SidebarLink from './SidebarLink.vue';
import { collapsed, toggleSidebar, sidebarWidth } from './state'

export default {
  props: {},
  components: {SidebarLink},
  setup() {
    return {collapsed, toggleSidebar, sidebarWidth}
  }
}

</script>


<template >
  <div class="sidebar" :style="{width: sidebarWidth}">
    <h1>
      <span v-if="collapsed">
        <div>DMR</div>
      </span>
      <span v-else>Demorô</span>
    </h1>
    <span 
      class="collapse-icon"
      :class= "{ 'rotate-180': collapsed}"
      @click="toggleSidebar"
    >
      <font-awesome-icon icon="fa-solid fa-angle-double-left" />
    </span>
   <SidebarLink to="/dashboard" icon="fas fa-tachometer-alt">Dashboard</SidebarLink>
    <SidebarLink to="/login" icon="fas fa-sign-in-alt">Realizar Login</SidebarLink>
    <SidebarLink to="/register" icon="fas fa-user-plus">Realizar Registro</SidebarLink>
    <SidebarLink to="/amigos" icon="fas fa-users">Ver Amigos</SidebarLink>
  </div>
</template>

<style scoped>

.sidebar{
  margin: 0;
  padding: 0;
  width: 0;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  position: fixed;
  height: 100%;
  overflow: auto;

  transition: 0.3s ease;


  display: flex;
  flex-direction: column;
}

.collapse-icon{
  position: absolute;
  bottom: 0;
  padding: 0.75em;
  color: wheat;
  transition: 0.2s linear;

}


.rotate-180{
  transform: rotate(180deg);
  transition: 0.2s linear;
}

</style>


