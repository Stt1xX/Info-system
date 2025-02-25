import {createRouter, createWebHistory} from "vue-router";

import LoginPage from '@/views/LoginPage.vue'
import RegistryPage from "@/views/RegistryPage.vue";
import MessagesPage from "@/views/MessagesPage.vue";
import ErrorPage from "@/errors/ErrorPage.vue";
import HumanPage from "@/views/HumanPage.vue";
import CoordinatePage from "@/views/CoordinatePage.vue";
import CarPage from "@/views/CarPage.vue";
import ImportPage from "@/views/ImportPage.vue";

const routes = [
    {path: "/humanTable", component: HumanPage},
    {path: "/coordinateTable", component: CoordinatePage},
    {path: "/carTable", component: CarPage},
    {path: "/login", component: LoginPage},
    {path: "/registration", component: RegistryPage},
    {path: "/admin/messages", component: MessagesPage},
    {path: "/error/ErrorPage", component: ErrorPage },
    {path: "/importFile", component: ImportPage },
    {path: "/", redirect: {path: "/humanTable"}},
    {
        path: '/:pathMatch(.*)*',
        redirect: '/error/ErrorPage?message=404',
    },
]

const router = createRouter({
    history:createWebHistory(),
    routes
})

export default router;