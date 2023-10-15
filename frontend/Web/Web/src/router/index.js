import { createRouter, createWebHistory } from 'vue-router'

import Bman from '../components/BusManagement.vue'
import other from '../components/OtherManagement.vue'
import Overview from '../components/OverView.vue'
import Profile from '../components/Profile.vue'
import login from '../components/Login.vue'
import registre from '../components/Register.vue'
///import Pprofile from '../components/PatientProfile.vue'
//import Dprofile from '../components/DoctorProfile.vue'
//import viewPro from '../components/ViewPatientPro.vue'
//import docPro from '../components/ViewDoctorPro.vue';
import log from '../components/Login.vue';
import addbus from '../components/busAdd.vue';
import viewB from '../components/viewBus.vue';



const routes = [
  
  {path: '/',component: log },
  { path: '/Overview',name:'Overview', component: Overview },
  { path: '/BusManagement',name:'Bman', component: Bman },
  { path: '/OtherManagement', component: other },
  { path: '/Profile', component: Profile },
  { path: '/registre', component: registre },
  { path: '/addbus', component: addbus },
  { path: '/viewB', name:'viewB', component: viewB},
  // { path: '/login', component: login },
  // { path: '/Pprofile', component: Pprofile },
  // { path: '/Dprofile', component: Dprofile },
  // { path: '/viewPro/:patientId', name:'viewPro', component: viewPro },
  //  { path: '/docPro/:doctorid', name:'docPro', component: docPro},
  
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router
