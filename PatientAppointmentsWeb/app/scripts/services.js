'use strict';

angular.module('patientAppointmentsApp')
        .constant("baseURL","http://localhost:3000/")//localhost 192.168.0.13
        .service('appointmentFactory', ['baseURL', '$http', function(baseURL, $http) {
    
            this.getAppointments = function(mrn){

                return $http.get(baseURL + 'appointments/' + mrn);           
            };

            this.bookAppointment = function(appointment) {

                return $http.post(baseURL + 'newappointment', appointment)
                .then(function (res) {

                    return res.data; 
                }); 
            };
                
            
            this.checkNearAppointments = function(appointment) {

                return $http.get(baseURL + 'checknearappointments/' + appointment.medicalRecordNumber + '/' + appointment.dayRequiredForAppointment + '/' + appointment.appointmentTimeHours )
                .then(function (res) {

                    return res.data; 
                }); 
            };
            
            this.cancelAppointment = function(booking) {

                return $http.post(baseURL + 'cancelappointment', booking)
                .then(function (res) {

                    return res.data; 
                }); 
            };
                
            this.rescheduleAppointment = function(modifiedAppointment) {

                return $http.post(baseURL + 'rescheduleappointment', modifiedAppointment)
                .then(function (res) {

                    return res.data; 
                }); 
            };
            
        }])

        .factory('corporateFactory', ['$resource', 'baseURL', '$http', function($resource, baseURL, $http) {
    
            var corpfac = {};

            corpfac.getDoctors = function(){

                return $http.get(baseURL + 'doctors');           
            };
            
            corpfac.getDoctorSchedule = function(doctorId) {

                return $http.get(baseURL + 'doctorSchedule/' + doctorId);   
            };
            
            corpfac.getBookedSlots = function(appointment) {

                return $http.get(baseURL + 'bookedSlots/' + appointment.doctorId + '/' + appointment.dayRequiredForAppointment);   
            };
            
            return corpfac;
        }])
        .factory('authService', ['$http', 'baseURL', function ($http, baseURL) {
            
            var authService = {};
 
            authService.login = function (credentials) {
            return $http
              .post(baseURL + 'login', credentials)
              .then(function (res) {
                
                return res.data; 
              });
            };
            
            authService.registerUser = function (user) {
            return $http
              .post(baseURL + 'registeruser', user)
              .then(function (res) {
                
                return res.data; 
              });
                
            };
            return authService;
        }])
        

;
