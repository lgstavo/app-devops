<script>

import { computed } from 'vue'
import { useRoute } from 'vue-router';
import { collapsed } from './state';

export default{
    props: {
        to: {type: String, required: true},
        icon: {type: String, required: true}
    },
    setup(props){
        const route = useRoute()
        const isActive = computed(() => route.path === props.to)
        return {isActive, collapsed}
    }
}
</script>

<template>
    <router-link :to="to" class="link" :class="{ active : isActive}">
        <font-awesome-icon :icon="icon" class="icon" />
        <transition name="fade">
            <span v-if ="!collapsed">
                <slot />
            </span>
        </transition>    
    </router-link>
</template>

<style scoped>

.fade-enter-active,
.fade-leave-active{
    transition: opacity 0.1s;
}

.fade-enter-from,
.fade-leave-to{
    opacity: 0;
}

.link{
    display: flex;
    align-items: center;

    cursor: pointer;
    position: relative;
    font-weight: 400;
    user-select: none;

    margin: 0.1cm 0;
    padding: 0.4em;
    border-radius: 0.25em;
    height: 1.5cm;

    color: white;
    text-decoration: none;
}

.link:hover{
    background-color: var(--color-background-soft)
}

.link:active{
    background-color: blue;
}

.link .icon{
    flex-shrink: 0;
    width: 25px;
    margin-right: 10px;
}

</style>