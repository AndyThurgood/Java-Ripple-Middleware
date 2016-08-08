/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
'use strict';

angular.module('rippleDemonstrator')
  .controller('ClinicalNotesListCtrl', function ($scope, $state, $stateParams, SearchInput, $location, $modal, usSpinnerService, PatientService, ClinicalNotes) {

    SearchInput.update();
    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.type).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.author).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.dateCreated).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    ClinicalNotes.all($stateParams.patientId).then(function (result) {
      $scope.clinicalNotes = result.data;

      for (var i = 0; i < $scope.clinicalNotes.length; i++) {
        $scope.clinicalNotes[i].dateCreated = moment($scope.clinicalNotes[i].dateCreated).format('DD-MMM-YYYY');
      }

      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id, clinicalNoteSource) {
      $state.go('clinicalNotes-detail', {
        patientId: $scope.patient.id,
        clinicalNoteIndex: id,
        filter: $scope.query,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType,
        source: clinicalNoteSource
      });
    };

    $scope.selected = function (clinicalNoteIndex) {
      return clinicalNoteIndex === $stateParams.clinicalNoteIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/clinical-notes/clinicalnotes-modal.html',
        size: 'md',
        controller: 'ClinicalNotesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Clinical Note'
            };
          },
          clinicalNote: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (clinicalNote) {
        var toAdd = {
          type: clinicalNote.type,
          note: clinicalNote.note,
          dateCreated: clinicalNote.dateCreated,
          author: clinicalNote.author,
          source: 'openehr'
        };

        ClinicalNotes.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('clinicalNotes', {
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
