import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue'),
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('../views/DashboardView.vue'),
    },
    {
      path: '/amigos',
      name: 'amigos',
      component: () => import('../views/AmigosView.vue'),
    },
    {
      path: '/amigos',
      name: 'amigos',
      component: () => import('../views/AmigosView.vue'),
    },
    {
      path: '/posts',
      name: 'posts',
      component: () => import('../views/PostView.vue'),
    },
    {
      path: '/',
      redirect: '/dashboard',
    },
  ],
})

export default router
