import { Layout, List } from '@/views/users';

export default {
    path: '/users',
    component: Layout,
    children: [
        { path: '', component: List },
    ]
};