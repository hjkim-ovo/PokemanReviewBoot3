import { defineStore } from 'pinia';
import { fetchWrapper } from '@/helpers';
import { useAuthStore } from '@/stores';

const baseUrl = `${import.meta.env.VITE_API_URL}`;

export const useUsersStore = defineStore({
    id: 'users',
    state: () => ({
        pageInfo: {},
        users: {},
        user: {}
    }),
    actions: {
        async register(user) {
            await fetchWrapper.post(`${baseUrl}/auth/register`, user);
        },
        async getAll(params) {
            this.users = { loading: true };
            try {
                const queryString = new URLSearchParams(params).toString();
                const {content, pageSize, totalElements, totalPages } = 
                    await fetchWrapper.get(`${baseUrl}/admin/users?${queryString}`);
                this.users = content;   
                this.pageInfo = {pageSize, totalElements, totalPages}
            } catch (error) {
                this.users = { error };
            }
        },
        async getById(id) {
            this.user = { loading: true };
            try {
                this.user = await fetchWrapper.get(`${baseUrl}/admin/users/${id}`);
            } catch (error) {
                this.user = { error };
            }
        },
        async update(id, params) {
            await fetchWrapper.put(`${baseUrl}/admin/users/${id}`, params);

            // update stored user if the logged in user updated their own record
            const authStore = useAuthStore();
            if (id === authStore.user.id) {
                // update local storage
                const user = { ...authStore.user, ...params };
                localStorage.setItem('user', JSON.stringify(user));

                // update auth user in pinia state
                authStore.user = user;
            }
        },
    }
});