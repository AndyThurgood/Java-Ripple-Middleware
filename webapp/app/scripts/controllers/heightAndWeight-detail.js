'use strict';

angular.module('rippleDemonstrator')
  .controller('HeightAndWeightsDetailCtrl', function ($scope, $stateParams, SearchInput, $modal, $location, $state, Helper, usSpinnerService, PatientService, HeightAndWeight) {

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    HeightAndWeight.get($stateParams.patientId, $stateParams.heightAndWeightIndex).then(function (result) {
      $scope.heightAndWeight = result.data;
      usSpinnerService.stop('heightAndWeightsDetail-spinner');
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/heightAndWeights/heightAndWeight-modal.html',
        size: 'lg',
        controller: 'HeightAndWeightsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Height And Weight'
            };
          },
          heightAndWeight: function () {
            return angular.copy($scope.heightAndWeight);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (heightAndWeight) {
        heightAndWeight.heightRecorded = new Date(heightAndWeight.heightRecorded);
        heightAndWeight.weightRecorded = new Date(heightAndWeight.weightRecorded);

        var toUpdate = {
          sourceId: heightAndWeight.sourceId,
          height: heightAndWeight.height,
          weight: heightAndWeight.weight,
          heightRecorded : heightAndWeight.heightRecorded,
          weightRecorded : heightAndWeight.weightRecorded,
          source: heightAndWeight.source
        };

        HeightAndWeight.update($scope.patient.id, toUpdate).then(function () {
          setTimeout(function () {
            $state.go('heightAndWeights-detail', {
              patientId: $scope.patient.id,
              heightAndWeightIndex: Helper.updateId(heightAndWeight.sourceId),
              page: $scope.currentPage,
              reportType: $stateParams.reportType,
              searchString: $stateParams.searchString,
              queryType: $stateParams.queryType
            });
          }, 2000);
        });
      });
    };

  });
