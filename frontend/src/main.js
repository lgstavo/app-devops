import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

/* Importe os ícones que você quer usar */
import { faTachometerAlt, faSignInAlt, faUserPlus, faUsers, faAngleDoubleLeft } from '@fortawesome/free-solid-svg-icons'

/* Adicione os ícones à biblioteca */
library.add(faTachometerAlt, faSignInAlt, faUserPlus, faUsers, faAngleDoubleLeft)

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.component('font-awesome-icon', FontAwesomeIcon)

app.mount('#app')
