<script setup>
import { onMounted } from 'vue';
import { Form, Field } from 'vee-validate';
import * as Yup from 'yup';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';

import { usePokemonsStore, useAlertStore } from '@/stores';

const pokemonsStore = usePokemonsStore();
const alertStore = useAlertStore();
const router = useRouter();
const route = useRoute();
const id = route.params.id;

const { types } = storeToRefs(pokemonsStore);

let title = 'Add Pokemon';
let pokemon = null;
if (id) {
    console.log('Edit Mode = ' + id)
    // edit mode
    title = 'Edit Pokemon';
    pokemonsStore.getById(id);
    ({ pokemon } = storeToRefs(pokemonsStore));    
}

onMounted(() => {
    pokemonsStore.getTypes();
});

const schema = Yup.object().shape({
    name: Yup.string()
        .required('Pokemon Name is required'),
    type: Yup.string()
        .required('Pokemon Type is required'),
});

async function onSubmit(values) {
    try {
        let message;
        if (pokemon) {
            await pokemonsStore.update(pokemon.value.id, values)
            message = 'Pokemon updated';
        } else {
            await pokemonsStore.register(values);
            message = 'Pokemon added';
        }
        await router.push('/pokemons');
        alertStore.success(message);
    } catch (error) {
        alertStore.error(error);
    }
}
</script>

<template>
    <h1>{{title}}</h1>
    <template v-if="!(pokemon?.loading || pokemon?.error)">
        <Form @submit="onSubmit" :validation-schema="schema" :initial-values="pokemon" v-slot="{ errors, isSubmitting }">
            <div class="form-row">
                <div class="form-group col">
                    <label>Pokemon Name</label>
                    <Field name="name" type="text" class="form-control" :class="{ 'is-invalid': errors.name }" />
                    <div class="invalid-feedback">{{ errors.name }}</div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col">
                    <label>Pokemon Type</label>
                    <Field name="type" as="select" class="form-control" :class="{ 'is-invalid': errors.type }">
                        <option v-for="type in types" :key="type" :value="type">{{type}}</option>
                    </Field>    
                    <div class="invalid-feedback">{{ errors.type }}</div>
                </div>
            </div>
            <div class="form-group">
                <button class="btn btn-primary" :disabled="isSubmitting">
                    <span v-show="isSubmitting" class="spinner-border spinner-border-sm mr-1"></span>
                    Save
                </button>
                <router-link to="/pokemons" class="btn btn-link">Cancel</router-link>
            </div>
        </Form>
    </template>
    <template v-if="pokemon?.loading">
        <div class="text-center m-5">
            <span class="spinner-border spinner-border-lg align-center"></span>
        </div>
    </template>
    <template v-if="pokemon?.error">
        <div class="text-center m-5">
            <div class="text-danger">Error loading pokemon: {{pokemon.error}}</div>
        </div>
    </template>
</template>