'use strict';

angular.module('patientAppointmentsApp', ['ui.router','ngResource','ngStorage','cgNotify','ngAnimate', 'ui.bootstrap'])
.config(function($stateProvider, $urlRouterProvider) {
        $stateProvider
        
            // route for the home page
            .state('app', {
                url:'/',
                views: {
                    'header': {
                        templateUrl : 'views/header.html',
                    },
                    'content': {
                        templateUrl : 'views/welcome.html',
                        controller  : 'IndexController'
                    },
                    'footer': {
                        templateUrl : 'views/footer.html',
                    }
                }

            })
            // route for the aboutus page
            .state('app.bookings', {
                url:'bookings/:mrn',
                views: {
                    'content@': {
                        templateUrl : 'views/bookings.html',
                        controller  : 'BookingsController'                  
                    }
                }
            })
            // route for the aboutus page
            .state('app.newbooking', {
                url:'newbooking',
                views: {
                    'content@': {
                        templateUrl : 'views/newbooking.html',
                        controller  : 'NewBookingController'                  
                    }
                }
            })
            // route for the aboutus page
            .state('app.aboutus', {
                url:'aboutus',
                views: {
                    'content@': {
                        templateUrl : 'views/aboutus.html',
                        controller  : 'AboutController'                  
                    }
                }
            })
    
        $urlRouterProvider.otherwise('/');
    })
;