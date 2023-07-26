<script setup>
import { storeToRefs } from 'pinia';
import { useAuthStore } from '@/stores';

const authStore = useAuthStore();
const { user } = storeToRefs(authStore);
</script>

<template>
    <nav v-show="authStore.user" class="navbar navbar-expand navbar-dark bg-dark">
        <div class="navbar-nav">
            <router-link to="/" class="nav-item nav-link">Home</router-link>
            <div v-if="user && user.role == 'ROLE_ADMIN'">
                <router-link to="/users" class="nav-item nav-link">Users</router-link>
            </div>
            <router-link to="/pokemons" class="nav-item nav-link">Pokemons</router-link>
            <button @click="authStore.logout()" class="btn btn-link nav-item nav-link">Logout</button>
        </div>
    </nav>
</template>