'use strict';

angular.module('rippleDemonstrator')
  .controller('HeightAndWeightsListCtrl', function ($scope, $location, $stateParams, SearchInput, $modal, $state, usSpinnerService, PatientService, HeightAndWeight) {

    $scope.query = {};
    $scope.queryBy = '$';
    SearchInput.update();


    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    }

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($stateParams.filter) {
      $scope.query.$ = $stateParams.filter;
    }

    HeightAndWeight.all($stateParams.patientId).then(function (result) {
      $scope.heightAndWeights = result.data;
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
      $state.go('heightAndWeights-detail', {
        patientId: $scope.patient.id,
        heightAndWeightIndex: id,
        filter: $scope.query.$,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType
      });
    };

    $scope.selected = function (heightAndWeightIndex) {
      return heightAndWeightIndex === $stateParams.heightAndWeightIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/heightAndWeights/heightAndWeight-modal.html',
        size: 'lg',
        controller: 'HeightAndWeightsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Height And Weight'
            };
          },
          heightAndWeight: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (heightAndWeight) {
        heightAndWeight.heightRecorded = new Date(heightAndWeight.heightRecorded);
        heightAndWeight.weightRecorded = new Date(heightAndWeight.weightRecorded);

        var toAdd = {
          sourceId: '',
          height: heightAndWeight.height,
          weight: heightAndWeight.weight,
          heightRecorded : heightAndWeight.heightRecorded,
          weightRecorded : heightAndWeight.weightRecorded,
          source: heightAndWeight.source
        };

        HeightAndWeight.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('heightAndWeights', {
              patientId: $scope.patient.id,
              filter: $scope.query.$,
              page: $scope.currentPage,
              reportType: $stateParams.reportType,
              searchString: $stateParams.searchString,
              queryType: $stateParams.queryType
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
