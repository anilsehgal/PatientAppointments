'use strict';

angular.module('patientAppointmentsApp')
        .controller('NewBookingController', ['$scope', '$timeout', '$sessionStorage', 'corporateFactory', 'appointmentFactory', 'notify', '$rootScope', function($scope, $timeout, $sessionStorage, corporateFactory, appointmentFactory,notify,$rootScope) {
            
            $scope.newbooking = {
                    'medicalRecordNumber': $sessionStorage.sessionUser.medicalRecordNumber,
                    'firstName': $sessionStorage.sessionUser.firstName,
                    'lastName': $sessionStorage.sessionUser.lastName,
                    'dateOfBirth': new Date($sessionStorage.sessionUser.dateOfBirth),
                    'userEmailAddress': $sessionStorage.sessionUser.userEmailAddress,
                    'doctorId': "",
                    'dayRequiredForAppointment': "",
                    'appointmentTimeHours': "",
                    'appointmentTimeMinutes': "",
                    'ailmentDescription': ""
                };
            $scope.appointmentDateTime = {};
            $scope.currentDate = new Date();
            $scope.isSuccess = false;
            $scope.availableDoctors = corporateFactory.getDoctors()
            .then(
            function(response){

                $scope.availableDoctors = response.data;
            });
            
            $scope.setTime = function(slot){
                
                $scope.bookingTime = new Date(1970, 1, 1, parseInt(slot, 10), 0);
                $scope.suggestTime = false;
            };
            
            $scope.suggestTime = false;
            $scope.availableSlots = [];
            $scope.listAvailableSlots = function() {
                
                $scope.availableSlots = [];
                if ($scope.newbooking.doctorId.length > 0 && $scope.newbooking.dayRequiredForAppointment !== null) {
                    
                    corporateFactory.getBookedSlots($scope.newbooking)
                    .then(
                    function(response){

                        var busySlots = response.data;
                        var allSlots = [9, 10, 11, 12, 13, 14, 15, 16, 17];

                        var seen = [], diff = [];
                        for ( var i = 0; i < busySlots.length; i++){
                            seen[busySlots[i]] = true;
                        }
                        for ( var i = 0; i < allSlots.length; i++){

                            if (!seen[allSlots[i]]){

                                $scope.availableSlots.push(allSlots[i]);
                            }
                        }
                        $scope.suggestTime = true;
                    });
                }
            };
            
            $scope.dynamicPopover = {
                templateUrl: 'myPopoverTemplate.html',
                title: 'Title'
            };

            
            $scope.bookAppointment = function() {

                $scope.newbooking.appointmentTimeHours = $scope.bookingTime.getHours();
                $scope.newbooking.appointmentTimeMinutes = $scope.bookingTime.getMinutes();
                
                if ($scope.availableSlots.indexOf($scope.newbooking.appointmentTimeHours) === -1) {
                    
                    notify({
                        messageTemplate: '<span><b>Error!</b> Please choose appoinment time from available slots.</span>',
                        classes: 'alert-danger',
                        duration: 3000
                    });
                    return;
                }

                appointmentFactory.checkNearAppointments($scope.newbooking).then(
                function(resp){
                    
                    if( resp.length > 0 ) {
                        
                        var tableData = '';
                        for(var i =0; i < resp.length; i++) {
                            
                            var appointment = resp[i];
                            var hrs = appointment.appointmentTimeHours > 9 ? appointment.appointmentTimeHours : '0' + appointment.appointmentTimeHours;
                            var min = appointment.appointmentTimeMinutes > 9 ? appointment.appointmentTimeMinutes : '0' + appointment.appointmentTimeMinutes;
                            tableData += '<li class="list-group-item">At ' + hrs + ':' + min + ' With ' + appointment.doctorId + '</li>';
                        }
                        
                        notify({
                            messageTemplate: '<span><b>Error!</b> You have conflicting Appointments in a 2 hour timespan.<br><br>'
                            + '<ul class="list-group">'
                            + tableData
                            + '</ul>'
                            + '</span>',
                            classes: 'white-bg',
                            duration: (tableData.length * 2000)
                        });

                    } else {
                        
                        appointmentFactory.bookAppointment($scope.newbooking).then(
                        function(response){
                            
                            console.log(response);
                            if ( response[0] === 1 ) {

                                $scope.newbooking.appointmentTimeHours = 0;
                                $scope.newbooking.appointmentTimeMinutes = 0;
                                $scope.newbooking.doctorId = "";
                                $scope.newbooking.ailmentDescription = "";
                                $scope.newbooking.dayRequiredForAppointment = null;
                                $scope.bookingTime = null;
                                
                                $scope.availableSlots = [];
                                notify({
                                    messageTemplate: '<span><b>Success!</b> Your Appointment ID is ' + response[1] + '. You will recieve an email once the appointment is acknowledged. Kindly note the appointment ID for your reference.</span>',
                                    classes: 'alert-success'
                                });
                                $scope.bookingForm.$setPristine();
                            }
                        });
                    }
                });
            };

            $scope.fetchDoctorSchedule = function() {

                if ($scope.newbooking.doctorId.length > 0) {
                    
                    corporateFactory.getDoctorSchedule( $scope.newbooking.doctorId )
                    .then(
                    function(response){

                        $rootScope.selectedDoctorSchedule = response.data;
                    });
                }
                 
            };
             
            // time picker functions
            $scope.bookingTime = null;
            
            
            // date picker functions
            $scope.disabled = function(date, mode) {
                
                var doctorHoliday = 0;
                for (var i = 0; i < $scope.availableDoctors.length; i++) {

                    if ( $scope.newbooking.doctorId === $scope.availableDoctors[i].doctorId ) {
                        
                        doctorHoliday = $scope.availableDoctors[i].weeklyHolidayCode - 1;
                    }
                }
                return mode === 'day' && (date.getDay() === doctorHoliday);
            };
            $scope.popup1 = {
                opened: false
            };
            $scope.altInputFormats = ['M!/d!/yyyy'];
            $scope.open1 = function() {
                if ( $scope.newbooking.doctorId.length > 0 ) {
                    
                    $scope.popup1.opened = true;
                } else {

                    $scope.bookingForm.doctorPicker.$setValidity('required', false);
                    $scope.bookingForm.doctorPicker.$setDirty();
                }
            };
            $scope.maxDate = new Date(2020, 5, 22);
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1
            };

            $scope.getProgress = function(inputDate) {
                
                var inputDateObj = new Date(inputDate);
                var progressStruct = {};
                
                progressStruct.percentageValue = 0;
                progressStruct.progressClass = 'progress-bar-success';
                
                if ($rootScope.selectedDoctorSchedule.length > 0) {
                    for(var i = 0; i < $rootScope.selectedDoctorSchedule.length; i++ ) {

                        var scheduledDate = new Date($rootScope.selectedDoctorSchedule[i].scheduleDate);
                        
                        if ( (scheduledDate.getDate()+1) === inputDateObj.getDate() && scheduledDate.getMonth() === inputDateObj.getMonth() && scheduledDate.getFullYear() === inputDateObj.getFullYear() ) {
                            
                            var progressMax = 5;
                            var progressCurrent = $rootScope.selectedDoctorSchedule[i].appointmentCount > 5?5:$rootScope.selectedDoctorSchedule[i].appointmentCount;
                            
                            progressStruct.percentageValue = (progressCurrent/progressMax) * 100;

                            if ( progressStruct.percentageValue <= 30 ) {

                                progressStruct.progressClass = 'progress-bar-success';
                            } else if( progressStruct.percentageValue > 30 && progressStruct.percentageValue <= 45 ) {

                                progressStruct.progressClass = 'progress-bar-info';
                            } else if( progressStruct.percentageValue > 45 && progressStruct.percentageValue <= 65 ) {

                                progressStruct.progressClass = 'progress-bar-warning';
                            } else if( progressStruct.percentageValue > 65 ) {

                                progressStruct.progressClass = 'progress-bar-danger';
                            }
                        }
                    }
                }
                return progressStruct;
            };
        }])

        .controller('AboutController', ['$scope', 'corporateFactory', function($scope, corporateFactory) {
            

        }])
        .controller('IndexController', ['$scope', function($scope) {

        }])  
        .controller('BookingsController', ['$scope', 'appointmentFactory', '$sessionStorage', 'notify', function($scope, appointmentFactory, $sessionStorage, notify) {

            $scope.bookings = appointmentFactory.getAppointments(parseInt($sessionStorage.sessionUser.medicalRecordNumber,10))
                .then(
                function(response){

                    $scope.bookings = response.data;
                });
            $scope.cancelAppointment = function(booking) {

                appointmentFactory.cancelAppointment(booking).then(
                function(response){

                    if ( response === '1' ) {

                        notify({
                            messageTemplate: '<span><b>Success!</b> Appointment cancelled Successfully</span>',
                            classes: 'alert-success',
                            duration: 3000
                        });
                        appointmentFactory.getAppointments(parseInt($sessionStorage.sessionUser.medicalRecordNumber,10))
                        .then(
                        function(response){

                            $scope.bookings = response.data;
                        });
                    }
                });
            };
            
            $scope.modifiedAppointment = {
                'inputAppointmentId': '',
                'inputAppointmentDate': null,
                'inputAppointmentTime': null,
                'currentDate': new Date(),
                'inputAppointmentHrs': 0,
                'inputAppointmentMin': 0
            };
            
            $scope.rescheduleAppointment = function() {
                
                $scope.modifiedAppointment.inputAppointmentHrs = $scope.modifiedAppointment.inputAppointmentTime.getHours();
                $scope.modifiedAppointment.inputAppointmentMin = $scope.modifiedAppointment.inputAppointmentTime.getMinutes();
  
                
                $scope.rescheduleForm.$setPristine();
                
                appointmentFactory.rescheduleAppointment($scope.modifiedAppointment).then(
                function(response){

                    if ( response === '1' ) {

                        notify({
                            messageTemplate: '<span><b>Success!</b> Appointment Rescheduled Successfully</span>',
                            classes: 'alert-success',
                            duration: 3000
                        });
                        $scope.rescheduleForm.$setPristine();
                        appointmentFactory.getAppointments(parseInt($sessionStorage.sessionUser.medicalRecordNumber,10))
                        .then(
                        function(response){

                            $scope.bookings = response.data;
                        });
                    }
                });
                
            };
            
        }])
        .controller('LoginController', ['$scope', 'authService', '$sessionStorage', '$location', 'notify', function ($scope, authService, $sessionStorage, $location, notify) {
            $scope.credentials = {
            email: '',
            password: ''
            };

            $scope.newUser = {
                firstName: '',
                lastName: '',
                dob: null,
                email: '',
                password: '',
                repassword: ''
            };
            
            $scope.registerUser = function(){

                authService.registerUser($scope.newUser).then(
                function(response){

                    if ( response === '1' ) {
                        
                        notify({
                            message: 'Thanks For Registering! You may now login with your email and password to book appointments!',
                            classes: 'alert-success',
                            duration: 3000
                        });
                        jQuery('#reserveModal').modal('hide');
                        $scope.registrationForm.$setPristine();
                    }
                });
                
            };
            
            $scope.isAuthenticated = (!angular.isUndefined($sessionStorage.sessionUser) && $sessionStorage.sessionUser.hasOwnProperty('medicalRecordNumber')); 
            $scope.login = function (credentials) {

            authService.login(credentials).then(function (user) {

                if (user.hasOwnProperty('medicalRecordNumber')) {

                    $sessionStorage.sessionUser = user;
                    $scope.isAuthenticated = (!angular.isUndefined($sessionStorage.sessionUser) && $sessionStorage.sessionUser.hasOwnProperty('medicalRecordNumber')); 
                    notify({
                            message: 'Welcome ' + user.firstName,
                            classes: 'alert-success',
                            duration: 3000
                        });
                } else {

                    $sessionStorage.sessionUser = {};
                    $scope.isAuthenticated = false;
                    notify({
                            message: 'Login failed!',
                            classes: 'alert-danger',
                            duration: 3000
                        });
                }

            }, function () {

                $sessionStorage.sessionUser = {};
                $scope.isAuthenticated = false;
                $location.path('/');
            });
            };

            $scope.logout = function() {

                $sessionStorage.sessionUser = {};
                $scope.isAuthenticated = false;
                $location.path('/');
            };
            
            window.onbeforeunload = function() {
                
                return $scope.logout();
            }
        }]) 
;
