
<template>
  <div className="body-wrapper ">
    <div className="container-fluid pt-4 pb-0">
      <div className="col">
        <div className="col-lg-8 d-flex align-items-strech">
          <div className="card w-100">
            <div className="card-body">
              <div className="d-sm-flex d-block align-items-center justify-content-between mb-9">
                <div className="mb-3 mb-sm-0">
                  <h5 className="card-title fw-semibold">Booking Overview</h5>
                </div>
                <div>
                  <select className="form-select">
                    <option value="1">March 2023</option>
                    <option value="2">April 2023</option>
                    <option value="3">May 2023</option>
                    <option value="4">June 2023</option>
                  </select>
                </div>
              </div>
              <ChartCompo />
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12 d-flex align-items-stretch">
          <div class="card w-100">
            <div class="card-body p-4">
              <h5 class="card-title fw-semibold mb-4">Complaint Details</h5>
              <div class="table-responsive">
                <table class="table table-flush" id="datatable-basic2">
                  <thead class="thead-light">
                    <tr>
                      <th>Complain_ID</th>
                      <th>Username</th>

                      <th>BusName</th>
                      <th>Inspector_Name</th>
                      <th>UserName</th>
                      <th>Status</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="complaint in Complaints" :key="complaint.cid">
                      <td>
                        <div class="d-flex align-items-center">
                          <p class="text-xs font-weight-bold ms-2 mb-0">
                            {{ complaint.cid }}
                          </p>
                        </div>
                      </td>
                      <td class="font-weight-bold">
                        <span class="my-2 text-xs">{{ complaint.userName }}</span>
                      </td>
                      <td class="font-weight-bold">
                        <span class="my-2 text-xs">{{ complaint.busName }}</span>
                      </td>
                      <td class="font-weight-bold">
                        <span class="my-2 text-xs">{{ complaint.inspector }}</span>
                      </td>
                      <td class="text-xs font-weight-bold">
                        <span class="my-2 text-xs">{{ complaint.description }}</span>
                      </td>
                      <td class="text-xs font-weight-bold">
                        <span class="my-2 text-xs">{{
                          complaint.status
                        }}</span>
                      </td>
                      <td class="text-xs font-weight-bold p-0 pt-1">
                        <button class="btn btn-outline-dark btn-xs p-1 mb-1 me-1" @click="Updatetoresolve(complaint.cid)">
                          Resolved
                        </button>
                        <button class="btn btn-outline-dark btn-xs p-1 mb-1 me-1" @click="Updatetodecline(complaint.cid)">
                          Decline
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ComplaintService from "../services/ComplaintService";
import axios from "axios";
import * as XLSX from "xlsx";
import Swal from "sweetalert2";
import ChartCompo from "./ChartCompo.vue";
import GraphCompo from "./GraphCompo.vue";

export default {
  name: "Complaintlist",
  components: {
    ChartCompo,
    GraphCompo,
  },

  data() {
    return {
      Complaints: [],
    };
  },

  methods: {
    getComplaints() {
      ComplaintService.getComplaints().then((response) => {
        this.Complaints = response.data;
      });
    },


    Updatetoresolve(cid) {

      Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, update it!'
      }).then((result) => {
        if (result.isConfirmed) {
          ComplaintService.Updatetoresolve(cid)
            .then((response) => {
              // Handle success (e.g., show a success message)
              // console.log('bus deleted successfully:', response.data);

              // After deleting, update the Patiens array by removing the deleted patient
              this.Complaints = this.Complaints.filter((Complaint) => Complaint.cid !== cid);

              // Navigate to the desired route (if needed)
              this.$router.push({ name: 'Overview' });
            })
            .catch((error) => {
              // Handle errors (e.g., show an error message)
              console.error('Error updating Inspector:', error);
            });
          Swal.fire(
            'Updated!',
            'Your file has been updated.',
            'success'
          )
        }
      })
      // Send a DELETE request to delete the patient using the same PatientService

    },

    Updatetodecline(cid) {

      Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, update it!'
      }).then((result) => {
        if (result.isConfirmed) {
          ComplaintService.Updatetodecline(cid)
            .then((response) => {
              // Handle success (e.g., show a success message)
              // console.log('bus deleted successfully:', response.data);

              // After deleting, update the Patiens array by removing the deleted patient
              this.Complaints = this.Complaints.filter((Complaint) => Complaint.cid !== cid);

              // Navigate to the desired route (if needed)
              this.$router.push({ name: 'Overview' });
            })
            .catch((error) => {
              // Handle errors (e.g., show an error message)
              console.error('Error updating Inspector:', error);
            });
          Swal.fire(
            'Updated!',
            'Your file has been updated.',
            'success'
          )
        }
      })
      // Send a DELETE request to delete the patient using the same PatientService

    },



    exportToCSV() {
      const Complaints = this.Complaints;
      const ws = XLSX.utils.json_to_sheet(Complaints);
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "Complaints");
      XLSX.writeFile(wb, "Complaints.csv");
    },
  },

  created() {
    this.getComplaints();
    setInterval(this.getComplaints, 1000);
  },
};
</script>


