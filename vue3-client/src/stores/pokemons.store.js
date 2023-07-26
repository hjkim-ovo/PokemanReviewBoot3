import { defineStore } from 'pinia';
import { fetchWrapper } from '@/helpers';
import { useAlertStore } from '@/stores';

const baseUrl = `${import.meta.env.VITE_API_URL}`;

export const usePokemonsStore = defineStore({
    id: 'pokemons',
    state: () => ({
        pageInfo: {},
        pokemons: {},
        pokemon: {},
        types: []
    }),
    actions: {
        async register(pokemon) {
            await fetchWrapper.post(`${baseUrl}/pokemon`, pokemon);
        },
        async getTypes() {
            this.types = { loading: true };
            try {
                this.types = await fetchWrapper.get(`${baseUrl}/pokemon/pokemontypes`);
            } catch (error) {
                this.pokemon = { error };
                const alertStore = useAlertStore();
                alertStore.error(error);
            }
        },
        async getAll(params) {
            this.pokemons = { loading: true };
            try {
                const queryString = new URLSearchParams(params).toString();
                const {content, pageSize, totalElements, totalPages } = 
                    await fetchWrapper.get(`${baseUrl}/pokemon?${queryString}`);
                this.pokemons = content;   
                this.pageInfo = {pageSize, totalElements, totalPages}
            } catch (error) {
                const alertStore = useAlertStore();
                alertStore.error(error);
                this.pokemons = { error };
            }
        },
        async getById(id) {
            this.pokemon = { loading: true };
            try {
                this.pokemon = await fetchWrapper.get(`${baseUrl}/pokemon/${id}`);
            } catch (error) {
                this.pokemon = { error };
                const alertStore = useAlertStore();
                alertStore.error(error);
            }
        },
        async update(id, params) {
            this.pokemon = { loading: true };
            try {
                this.pokemon = await fetchWrapper.put(`${baseUrl}/pokemon/${id}`, params);
            } catch (error) {
                this.pokemon = { error };
                const alertStore = useAlertStore();
                alertStore.error(error);
            }
        },
        async delete(id) {
            // add isDeleting prop to user being deleted
            this.pokemons.find(x => x.id === id).isDeleting = true;

            await fetchWrapper.delete(`${baseUrl}/pokemon/${id}`);

            // remove user from list after deleted
            this.pokemons = this.pokemons.filter(x => x.id !== id);
            
        }
    }
});