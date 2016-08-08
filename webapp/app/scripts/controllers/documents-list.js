/*
 *   Copyright 2016 Ripple OSI
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
  .controller('DocumentsListCtrl', function ($scope, $state, $stateParams, SearchInput, usSpinnerService, PatientService, DocumentService) {

    SearchInput.update();
    $scope.currentPage = 1;

    $scope.search = function (row) {
      return (
        angular.lowercase(row.documentType).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.documentDate).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    DocumentService.all($stateParams.patientId).then(function (result) {
      $scope.documents = result.data;

      for (var i = 0; i < $scope.documents.length; i++) {
        $scope.documents[i].documentDate = moment($scope.documents[i].documentDate).format('DD-MMM-YYYY');
      }

      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id, documentType, source) {
      $state.go('documents-detail', {
        patientId: $scope.patient.id,
        documentType: documentType,
        documentIndex: id,
        filter: $scope.query,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType,
        source: source
      });
    };

    $scope.selected = function (documentIndex) {
      return documentIndex === $stateParams.documentIndex;
    };
  });
