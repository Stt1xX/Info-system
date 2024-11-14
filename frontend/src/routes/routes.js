import {createRouter, createWebHistory} from "vue-router";

import LoginPage from '../views/LoginPage.vue'
import MainPage from '../views/MainPage.vue'
import RegistryPage from "@/views/RegistryPage.vue";

const routes = [
    {path: "/greeting", component: MainPage, props: (route) => ({ token: route.meta.token })},
    {path: "/login", component: LoginPage, props: (route) => ({ token: route.meta.token })},
    {path: "/registration", component: RegistryPage, props: (route) => ({ token: route.meta.token })},
    {path: "/", redirect: {path: "/greeting"}}
]

const router = createRouter({
    history:createWebHistory(),
    routes
})

router.beforeEach(async (to, from, next) => {
    to.meta.token = await new Promise((resolve) => {
        $.ajax({
            type: "GET",
            url: "/app/csrf-token",
            success: function (response) {
                resolve(response.csrfToken)
            }
        })
    });
    next();
})

export default router;