<template>
    <h1>Users</h1>
    <router-link to="/pokemons/add" class="btn btn-sm btn-success mb-2">Add Pokemon</router-link>
    <table class="table table-striped">
        <thead>
            <tr>
                <th style="width: 40%">Name</th>
                <th style="width: 40%">Type</th>
                <th style="width: 20%"></th>
            </tr>
        </thead>
        <tbody>
            <template v-if="pokemons.length > 0">
                <div>{{ params.pageNo }} /{{ pageCount }}</div>
                <tr v-for="pokemon in pokemons" :key="pokemon.id">
                    <td>{{ pokemon.name }}</td>
                    <td>{{ pokemon.type }}</td>
                    <td style="white-space: nowrap">
                        <router-link :to="`/pokemons/edit/${pokemon.id}`" class="btn btn-sm btn-primary mr-1">Edit</router-link>
                        <button @click="pokemonsStore.delete(pokemon.id)" class="btn btn-sm btn-danger btn-delete-user" :disabled="pokemon.isDeleting">
                            <span v-if="pokemon.isDeleting" class="spinner-border spinner-border-sm"></span>
                            <span v-else>Delete</span>
                        </button>
                    </td>
                </tr>
            </template>
            <tr v-if="pokemons.loading">
                <td colspan="4" class="text-center">
                    <span class="spinner-border spinner-border-lg align-center"></span>
                </td>
            </tr>
            <tr v-if="pokemons.error">
                <td colspan="4">
                    <div class="text-danger">Error loading pokemons: {{pokemons.error}}</div>
                </td>
            </tr>            
        </tbody>
    </table>
    <nav class="mt-5" aria-label="Page navigation example">
			<ul class="pagination justify-content-center">
				<li class="page-item" :class="{ disabled: !(params.pageNo > 1) }">
					<a
						class="page-link"
						href="#"
						aria-label="Previous"
						@click.prevent="--params.pageNo">
						<span aria-hidden="true">&laquo;</span>
					</a>
				</li>
        <template v-if="pageCount >= 1">
				<li
					v-for="page in pageCount"
					:key="page"
					class="page-item"
					:class="{ active: params.pageNo === page }">
					<a class="page-link" href="#" @click.prevent="params.pageNo = page">{{
						page
					}}</a>
				</li>
        </template>
				<li
					class="page-item"
					:class="{ disabled: !(params.pageNo < pageCount) }">
					<a
						class="page-link"
						href="#"
						aria-label="Next"
						@click.prevent="++params.pageNo"
					>
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
			</ul>
	</nav>
</template>

<script setup>
import { reactive, watch, computed,onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { usePokemonsStore } from '@/stores';

const params = reactive({
	pageNo: 0,
	pageSize: 5
});

const pokemonsStore = usePokemonsStore();
const { pokemons, pageInfo } = storeToRefs(pokemonsStore);

//const totalCount = pageInfo.totalElements;
const pageCount = computed(() =>
	Math.ceil(pageInfo.value.totalElements / params.pageSize),
);

onMounted(() => {
    fetchData();
});

const fetchData = () => {
    if(params.pageNo >= 1){
        console.log('pageNo가 1보다 크면 빼기')
        const newParams = {
            ...params,
            pageNo: params.pageNo - 1
        }
        pokemonsStore.getAll(newParams)
    }else {
        pokemonsStore.getAll(params)
    }    
}

watch(() => params.pageNo, fetchData)

</script>