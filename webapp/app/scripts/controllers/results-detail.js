'use strict';

angular.module('rippleDemonstrator')
  .controller('ResultsDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Result) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Result.get($stateParams.patientId, $stateParams.resultIndex).then(function (result) {
      $scope.result = result.data;
    });

  });
