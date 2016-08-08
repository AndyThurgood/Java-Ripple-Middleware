'use strict';

angular.module('rippleDemonstrator')
  .controller('HeightAndWeightsListCtrl', function ($scope, $location, $stateParams, SearchInput, $modal, $state, usSpinnerService, PatientService, HeightAndWeight) {

    $scope.currentPage = 1;

    SearchInput.update();

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.weight).toString().indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.height).toString().indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.weightRecorded).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.heightRecorded).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    HeightAndWeight.all($stateParams.patientId).then(function (result) {
      $scope.heightAndWeights = result.data;

      for (var i = 0; i < $scope.heightAndWeights.length; i++) {
        $scope.heightAndWeights[i].weightRecorded = moment($scope.heightAndWeights[i].weightRecorded).format('DD-MMM-YYYY');
        $scope.heightAndWeights[i].heightRecorded = moment($scope.heightAndWeights[i].heightRecorded).format('DD-MMM-YYYY');
      }

      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
      $state.go('heightAndWeights-detail', {
        patientId: $scope.patient.id,
        heightAndWeightIndex: id,
        filter: $scope.query,
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
              filter: $scope.query,
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
