<script setup>
import { Form, Field } from 'vee-validate';
import * as Yup from 'yup';

import { useUsersStore, useAlertStore } from '@/stores';
import { router } from '@/router';

const schema = Yup.object().shape({
    firstName: Yup.string()
        .required('First Name is required'),
    lastName: Yup.string()
        .required('Last Name is required'),
    username: Yup.string()
        .required('Username is required'),
    password: Yup.string()
        .required('Password is required')
        .min(6, 'Password must be at least 6 characters'),
    role: Yup.string()
        .required('Role is required'),    
});

async function onSubmit(values) {
    console.log(values)
    const usersStore = useUsersStore();
    const alertStore = useAlertStore();
    try {
        await usersStore.register(values);
        await router.push('/account/login');
        alertStore.success('Registration successful');
    } catch (error) {
        console.log(error)
        alertStore.error(error);
    }
}
</script>

<template>
    <div class="card m-3">
        <h4 class="card-header">Register</h4>
        <div class="card-body">
            <Form @submit="onSubmit" :validation-schema="schema" v-slot="{ errors, isSubmitting }">
                <div class="form-group">
                    <label>First Name</label>
                    <Field name="firstName" type="text" class="form-control" :class="{ 'is-invalid': errors.firstName }" />
                    <div class="invalid-feedback">{{ errors.firstName }}</div>
                </div>
                <div class="form-group">
                    <label>Last Name</label>
                    <Field name="lastName" type="text" class="form-control" :class="{ 'is-invalid': errors.lastName }" />
                    <div class="invalid-feedback">{{ errors.lastName }}</div>
                </div>
                <div class="form-group">
                    <label>Username</label>
                    <Field name="username" type="text" class="form-control" :class="{ 'is-invalid': errors.username }" />
                    <div class="invalid-feedback">{{ errors.username }}</div>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <Field name="password" type="password" class="form-control" :class="{ 'is-invalid': errors.password }" />
                    <div class="invalid-feedback">{{ errors.password }}</div>
                </div>
                <div class="form-group">
                    <label>Role</label>
                    <!-- Default checked radio -->
                    <div class="form-check">
                        <!-- <RadioButton inputId="roleId1" name="role" value="1" /> -->
                        <Field name="role" class="form-check-input" type="radio"  id="roleId1" value="ROLE_USER" :class="{ 'is-invalid': errors.role }" />
                        <label class="form-check-label" for="roleId1">ROLE_USER</label>
                        <div class="invalid-feedback">{{ errors.role }}</div>
                    </div>
                    <div class="form-check">
                        <Field name="role" class="form-check-input" type="radio"  id="roleId2" value="ROLE_ADMIN" :class="{ 'is-invalid': errors.role }" />
                        <label class="form-check-label" for="roleId2">ROLE_ADMIN</label>
                        <div class="invalid-feedback">{{ errors.role }}</div>
                    </div>
                    
                </div>
                <div class="form-group">
                    <button class="btn btn-primary" :disabled="isSubmitting">
                        <span v-show="isSubmitting" class="spinner-border spinner-border-sm mr-1"></span>
                        Register
                    </button>
                    <router-link to="login" class="btn btn-link">Cancel</router-link>
                </div>
            </Form>
        </div>
    </div>
</template>