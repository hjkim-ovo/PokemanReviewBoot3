import { defineStore } from 'pinia';
import { router } from '@/router';
import { useAlertStore } from '@/stores';
import { fetchWrapper } from '@/helpers';

const baseUrl = `${import.meta.env.VITE_API_URL}`;

export const useAuthStore = defineStore({
    id: 'auth',
    state: () => ({
        // initialize state from local storage to enable user to stay logged in
        user: JSON.parse(localStorage.getItem('user')),
        returnUrl: null
    }),
    actions: {
        async login(username, password) {
            try {
                // const config = {
                //     method: 'POST',
                //     headers: {
                //         'Accept': 'application/json',
                //         'Content-Type': 'application/json',
                //     },
                //     body: JSON.stringify({ username, password })
                // }
                //const user = await fetch(`${baseUrl}/auth/login`, config);    
                const user = await fetchWrapper.post(`${baseUrl}/auth/login`, { username, password });   
                console.log('>>>> user = ')
                console.log(user)

                // update pinia state
                this.user = user;

                // store user details and jwt in local storage to keep user logged in between page refreshes
                localStorage.setItem('user', JSON.stringify(user));

                // redirect to previous url or default to home page
                router.push(this.returnUrl || '/');
            } catch (error) {
                const alertStore = useAlertStore();
                alertStore.error(error);                
            }
        },
        logout() {
            this.user = null;
            localStorage.removeItem('user');
            router.push('/account/login');
        }
    }
});