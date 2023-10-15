<template>
    <div class="body-wrapper">
      <div class="container-fluid pt-4 pb-4">
        <div class="row">
          <div class="col-lg-8">
            <div class="card">
              <div class="card-body">
                <form @submit.prevent="submitForm">
                  <h3>Bus Profile</h3>
                  <div class="mb-3">
                    <label for="inputEmail" class="form-label">Name</label>
                    <input type="text" class="form-control" id="inputEmail" v-model="Bus.name" placeholder="Enter Name" />
                    <p class="text-danger" v-if="!isValidname">Please enter a valid name.</p>
                  </div>
                  <div class="mb-3">
                    <label for="inputFirstName" class="form-label">Vehicle Name</label>
                    <input type="text" class="form-control" id="inputFirstName" v-model="Bus.vehicleNum" placeholder="Enter Vehicle Name" />
                    <p class="text-danger" v-if="!isValidvehiclename">Please enter a valid vehicle name.</p>
                  </div>
                  <div class="mb-3">
                    <label for="inputLastName" class="form-label">Bus Type</label>
                    <input type="text" class="form-control" id="inputLastName" v-model="Bus.busType" placeholder="Enter Bus Type" />
                    <p class="text-danger" v-if="!isValidbustype">Please enter a valid bus type.</p>
                  </div>
                  <div class="mb-3">
                    <label for="inputUsername" class="form-label">Seats</label>
                    <input type="text" class="form-control" id="inputUsername" v-model="Bus.seats" placeholder="Enter Seats" />
                    <p class="text-danger" v-if="!isValidseats">Please enter a valid number of seats.</p>
                  </div>
                  <div class="mb-3">
                    <label for="inputGender" class="form-label">Price</label>
                    <input type="text" class="form-control" id="inputGender" v-model="Bus.price" placeholder="Enter Price" />
                    <p class="text-danger" v-if="!isValidprice">Please enter a valid price.</p>
                  </div>
                  <div class="mb-3">
                    <label for="inputBloodGroup" class="form-label">Driver</label>
                    <input type="text" class="form-control" id="inputBloodGroup" v-model="Bus.driver" placeholder="Enter Driver" />
                    <p class="text-danger" v-if="!isValiddriver">Please enter a valid driver name.</p>
                  </div>
                  <div class="mb-3">
                    <label for="inputAge" class="form-label">From Location</label>
                    <input type="text" class="form-control" id="inputAge" v-model="Bus.fromLocation" placeholder="Enter From Location" />
                    <p class="text-danger" v-if="!isValidfrom">Please enter a valid from location.</p>
                  </div>
                  <div class="mb-3">
                    <label for="inputAmount" class="form-label">To Location</label>
                    <input type="text" class="form-control" id="inputAmount" v-model="Bus.toLocation" placeholder="Enter To Location" />
                    <p class="text-danger" v-if="!isValidto">Please enter a valid to location.</p>
                  </div>
                  <div class="mb-3">
                    <label for="inputDistrict" class="form-label">Shedule</label>
                    <input type="text" class="form-control" id="inputDistrict" v-model="Bus.shedule" placeholder="Enter Shedule" />
                    
                  </div>
                  <button type="submit" class="btn btn-primary">Update Details</button>
                </form>
              </div>
            </div>
          </div>
          <!-- ... -->
        </div>
      </div>
    </div>
  </template>
  
  
    
    
    
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        Bus: {
            name: '',
            vehicleNum: '',
            busType: '',
            seats: '',
            price: '',
            driver: '',
            fromLocation: '',
            toLocation: '',
            shedule: ''

        },
        isValidname: true,
        isValidvehiclename: true,
        isValidbustype: true,
        isValidseats: true,
        isValidprice: true,
        isValiddriver: true,
        isValidfrom: true,
        isValidto: true
        
      };
    },
    methods: {
      submitForm() {
        this.validateForm();
  
        if (this.isFormValid) {
          // Send a POST request to your Spring Boot API
          axios
            .post('http://localhost:8090/bus', this.Bus)
            .then((response) => {
              console.log('Bus data sent successfully:', response.data);
  
              // Optionally, you can reset the form fields here
              this.Bus.name="";
              this.Bus.vehicleNum="";
              this.Bus.busType="";
              this.Bus.driver="";
              this.Bus.seats="";
              this.Bus.price="";
              this.Bus.fromLocation="";
              this.Bus.toLocation="";
              this.Bus.shedule="";
  
            })
            .catch((error) => {
              console.error('Error sending doctor data:', error);
            });
        }
      },
      validateForm() {
   
        this.isValidname = this.isValidField( this.Bus.name);
        this.isValidvehiclename = this.isValidField(this.Bus.vehicleNum);
        this.isValidbustype = this.isValidField(this.Bus.busType);
        this.isValidseats = this.isValidField(this.Bus.seats);
        this.isValidprice = this.isValidField(this.Bus.price);
       // this.isValiddriver = this.isValidField(this.doctor.status);
       // this.isValidfrom = this.isValidField(this.doctor.amount);
       // this.isValidto = this.isValidField(this.doctor.cured);
   
  
        this.isFormValid =
        this.isValidname &&
        this.isValidvehiclename &&
        this.isValidbustype &&
        this.isValidseats &&
        this.isValidprice;
      },

      isValidField(value) {
        return typeof value === 'string' && value.trim() !== '';
      }
    }
  };
  </script>
  
      