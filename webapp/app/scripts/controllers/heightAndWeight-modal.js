'use strict';

angular.module('rippleDemonstrator')
  .controller('HeightAndWeightsModalCtrl', function ($scope, $modalInstance, PatientService, heightAndWeight, patient, modal) {

    $scope.currentUser = PatientService.getCurrentUser();
    $scope.heightAndWeight = heightAndWeight;
    $scope.patient = patient;
    $scope.modal = modal;

    if (modal.title === 'Edit Height And Weight'){
      $scope.heightAndWeight.heightRecorded = new Date($scope.heightAndWeight.heightRecorded).toISOString().slice(0, 10);
      $scope.heightAndWeight.weightRecorded = new Date($scope.heightAndWeight.weightRecorded).toISOString().slice(0, 10);
    }else {
      $scope.heightAndWeight.heightRecorded = new Date().toISOString().slice(0, 10);
      $scope.heightAndWeight.weightRecorded = new Date().toISOString().slice(0, 10);
    }

    $scope.ok = function (heightAndWeightForm, heightAndWeight) {
      $scope.formSubmitted = true;

      if (heightAndWeightForm.$valid) {
        $modalInstance.close(heightAndWeight);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

  });
