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
  .controller('DocumentsDetailCtrl', function ($scope, $stateParams, SearchInput, $state, $modal, usSpinnerService, PatientService, DocumentService) {

    $scope.documentType = $stateParams.documentType;

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($scope.documentType == 'Discharge summary') {
      DocumentService.findDischarge($stateParams.patientId, $stateParams.documentIndex, $stateParams.source).then(function (result) {
        $scope.clinicalDocument = result.data;
      });
    }
    else if ($scope.documentType == 'Referral') {
      DocumentService.findReferral($stateParams.patientId, $stateParams.documentIndex, $stateParams.source).then(function (result) {
        $scope.clinicalDocument = result.data;
      });
    }

    $scope.import = function (diagnosis) {
      //var modalInstance = $modal.open({
      //  templateUrl: 'views/diagnoses/diagnoses-modal.html',
      //  size: 'lg',
      //  controller: 'DiagnosesModalCtrl',
      //  resolve: {
      //    modal: function () {
      //      return {
      //        title: 'Import Problem / Diagnosis'
      //      };
      //    },
      //    diagnosis: function () {
      //      return diagnosis;
      //    },
      //    patient: function () {
      //      return $scope.patient;
      //    }
      //  }
      //});
      //
      //modalInstance.result.then(function (diagnosis) {
      //  diagnosis.dateOfOnset = new Date(diagnosis.dateOfOnset);
      //
      //  var toAdd = {
      //    code: diagnosis.code,
      //    dateOfOnset: diagnosis.dateOfOnset,
      //    description: diagnosis.description,
      //    problem: diagnosis.problem,
      //    terminology: diagnosis.terminology
      //  };
      //
      //  Diagnosis.create($scope.patient.id, toAdd).then(function () {
      //    setTimeout(function () {
      //      $state.go('documents-details', {
      //        patientId: $scope.patient.id,
      //        filter: $scope.query,
      //        page: $scope.currentPage,
      //        reportType: $stateParams.reportType,
      //        searchString: $stateParams.searchString,
      //        queryType: $stateParams.queryType,
      //        documentIndex: $scope.clinicalDocument.sourceId
      //      }, {
      //        reload: true
      //      });
      //    }, 2000);
      //  });
      //});
    };

  });
