'use strict';

angular.module('rippleDemonstrator')
  .controller('DiagnosesDetailCtrl', function ($scope, $stateParams, $location, $modal, PatientService, Diagnosis) {

    $scope.UnlockedSources = [
      'handi.ehrscape.com'
    ];

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Diagnosis.get($stateParams.patientId, $stateParams.diagnosisIndex).then(function (result) {
      $scope.diagnosis = result.data;
    });

    $scope.formDisabled = true;

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/diagnoses/diagnoses-modal.html',
        size: 'lg',
        controller: 'DiagnosesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Problem / Diagnosis'
            };
          },
          diagnosis: function () {
            return angular.copy($scope.diagnosis);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (diagnosis) {
        var toUpdate = {
          code: diagnosis.code,
          dateOfOnset: diagnosis.dateOfOnset,
          description: diagnosis.description,
          problem: diagnosis.problem,
          source: 'openehr',
          sourceId: diagnosis.sourceId,
          terminology: diagnosis.terminology
        };

        Diagnosis.update($scope.patient.id, toUpdate).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/diagnoses');
        });
      });
    };

    $scope.isLocked = function (diagnosis) {
      if (!(diagnosis && diagnosis.id)) {
        return true;
      }

      var diagnosisIdSegments = diagnosis.id.toString().split('::');
      if (diagnosisIdSegments.length > 1) {
        return ($scope.UnlockedSources.indexOf(diagnosisIdSegments[1]) < 0);
      }

      return true;
    };

    $scope.convertToLabel = function (text) {
      var result = text.replace(/([A-Z])/g, ' $1');
      return result.charAt(0).toUpperCase() + result.slice(1);
    };

  });
