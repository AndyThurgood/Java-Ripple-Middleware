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
  .controller('DocumentsDetailCtrl', function ($scope, $stateParams, SearchInput, $state, $modal, usSpinnerService, PatientService, DocumentService, Diagnosis, Medication, Allergy) {

    $scope.documentType = $stateParams.documentType;

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($scope.documentType == 'Healthlink Discharge summary') {
      DocumentService.findDischarge($stateParams.patientId, $stateParams.documentIndex, $stateParams.source).then(function (result) {
        $scope.clinicalDocument = result.data;
      });
    }
    else if ($scope.documentType == 'Healthlink Referral') {
      DocumentService.findReferral($stateParams.patientId, $stateParams.documentIndex, $stateParams.source).then(function (result) {
        $scope.clinicalDocument = result.data;
      });
    }

    $scope.importDiagnosis = function (diagnosis) {
      var document = $scope.clinicalDocument;

      $modal.open({
        templateUrl: 'views/documents/import-record-confirmation.html',
        size: 'md',
        controller: function ($scope) {

          $scope.recordType = 'Diagnosis';
          $scope.documentType = document.documentType;

          $scope.cancel = function () {
            $scope.$close(true);
          };

          $scope.ok = function () {
            $scope.$close(true);

            PatientService.get($stateParams.patientId).then(function (patient) {
              $scope.patient = patient;

              var modalInstance = $modal.open({
                templateUrl: 'views/diagnoses/diagnoses-modal.html',
                size: 'lg',
                controller: 'DiagnosesModalCtrl',
                resolve: {
                  modal: function () {
                    return {
                      title: 'Import Problem / Diagnosis'
                    };
                  },
                  diagnosis: function () {
                    return angular.fromJson(diagnosis);
                  },
                  patient: function () {
                    return $scope.patient;
                  }
                }
              });

              modalInstance.result.then(function (diagnosis) {
                var toAdd = {
                  code: diagnosis.code,
                  dateOfOnset: new Date(diagnosis.dateOfOnset),
                  description: diagnosis.description,
                  problem: diagnosis.problem,
                  terminology: diagnosis.terminology
                };

                Diagnosis.create($scope.patient.id, toAdd).then(function () {
                  setStateBackToDocumentsDetail(document);
                });
              });
            });
          };
        }
      });
    };

    $scope.importMedication = function (medication) {
      var document = $scope.clinicalDocument;

      $modal.open({
        templateUrl: 'views/documents/import-record-confirmation.html',
        size: 'md',
        controller: function ($scope) {

          $scope.recordType = 'Medication';
          $scope.documentType = document.documentType;

          $scope.cancel = function () {
            $scope.$close(true);
          };

          $scope.ok = function () {
            $scope.$close(true);

            PatientService.get($stateParams.patientId).then(function (patient) {
              $scope.patient = patient;

              var modalInstance = $modal.open({
                templateUrl: 'views/medications/medications-modal.html',
                size: 'lg',
                controller: 'MedicationsModalCtrl',
                resolve: {
                  modal: function () {
                    return {
                      title: 'Import Medication'
                    };
                  },
                  medication: function () {
                    return angular.fromJson(medication);
                  },
                  patient: function () {
                    return $scope.patient;
                  }
                }
              });

              modalInstance.result.then(function (medication) {
                var toAdd = {
                  doseAmount: medication.doseAmount,
                  doseDirections: medication.doseDirections,
                  doseTiming: medication.doseTiming,
                  medicationCode: medication.medicationCode,
                  medicationTerminology: medication.medicationTerminology,
                  name: medication.name,
                  route: medication.route,
                  startDate: new Date(medication.startDate),
                  startTime: new Date(medication.startTime.valueOf() - medication.startTime.getTimezoneOffset() * 60000),
                  author: medication.author,
                  dateCreated: medication.dateCreated
                };

                Medication.create($scope.patient.id, toAdd).then(function () {
                  setStateBackToDocumentsDetail(document);
                });
              });
            });
          };
        }
      });
    };

    $scope.importAllergy = function (allergy) {
      var document = $scope.clinicalDocument;

      $modal.open({
        templateUrl: 'views/documents/import-record-confirmation.html',
        size: 'md',
        controller: function ($scope) {

          $scope.recordType = 'Allergy';
          $scope.documentType = document.documentType;

          $scope.cancel = function () {
            $scope.$close(true);
          };

          $scope.ok = function () {
            $scope.$close(true);

            PatientService.get($stateParams.patientId).then(function (patient) {
              $scope.patient = patient;

              var modalInstance = $modal.open({
                templateUrl: 'views/allergies/allergies-modal.html',
                size: 'lg',
                controller: 'AllergiesModalCtrl',
                resolve: {
                  modal: function () {
                    return {
                      title: 'Import Allergy'
                    };
                  },
                  allergy: function () {
                    var copy = angular.fromJson(allergy);
                    copy.causeCode = '1239085';
                    copy.terminologyCode = '12393890';

                    return copy;
                  },
                  patient: function () {
                    return $scope.patient;
                  }
                }
              });

              modalInstance.result.then(function (allergy) {
                var toAdd = {
                  cause: allergy.cause,
                  causeCode: allergy.causeCode,
                  causeTerminology: allergy.causeTerminology,
                  terminologyCode: allergy.terminologyCode,
                  reaction: allergy.reaction
                };

                Allergy.create($scope.patient.id, toAdd).then(function () {
                  setStateBackToDocumentsDetail(document);
                });
              });
            });
          };
        }
      });
    };

    var setStateBackToDocumentsDetail = function (document) {
      setTimeout(function () {
        $state.go('documents-detail', {
          patientId: $scope.patient.id,
          filter: $scope.query,
          page: $scope.currentPage,
          reportType: $stateParams.reportType,
          searchString: $stateParams.searchString,
          queryType: $stateParams.queryType,
          documentIndex: document.sourceId
        }, {
          reload: true
        });
      }, 2000);
    };
  });
