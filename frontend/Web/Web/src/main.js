import './assets/css/styles.css'
import '../node_modules/apexcharts/dist/apexcharts.min'
import '../node_modules/bootstrap/dist/js/bootstrap.bundle.min'

import VueApexCharts from 'vue3-apexcharts'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index'
import VueSweetalert2 from 'vue-sweetalert2'; 


const app = createApp(App)
app.use(VueApexCharts)
app.use(router)
app.use(VueSweetalert2);

app.mount('#app')
