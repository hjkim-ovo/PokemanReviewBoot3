import { Layout, List, AddEdit } from '@/views/pokemons';

export default {
    path: '/pokemons',
    component: Layout,
    children: [
        { path: '', component: List },
        { path: 'add', component: AddEdit },
        { path: 'edit/:id', component: AddEdit }
    ]
};