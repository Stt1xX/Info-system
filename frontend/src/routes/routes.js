import {createRouter, createWebHistory} from "vue-router";

import LoginPage from '@/views/LoginPage.vue'
import RegistryPage from "@/views/RegistryPage.vue";
import MessagesPage from "@/views/MessagesPage.vue";
import ErrorPage from "@/errors/ErrorPage.vue";
import HumanPage from "@/views/HumanPage.vue";
import CoordinatePage from "@/views/CoordinatePage.vue";
import CarPage from "@/views/CarPage.vue";

const routes = [
    {path: "/humanTable", component: HumanPage},
    {path: "/coordinateTable", component: CoordinatePage},
    {path: "/carTable", component: CarPage},
    {path: "/login", component: LoginPage},
    {path: "/registration", component: RegistryPage},
    {path: "/admin/messages", component: MessagesPage},
    {path: "/error/ErrorPage", component: ErrorPage },
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

    // router.beforeEach(async (to, from, next) => {
    //     to.meta.token = await new Promise((resolve) => {
    //         $.ajax({
    //             type: "GET",
    //             url: "/app/csrf-token",
    //             success: function (response) {
    //                 resolve(response.csrfToken)
    //             }
    //         })
    //     });
    //     next();
    // })

export default router;