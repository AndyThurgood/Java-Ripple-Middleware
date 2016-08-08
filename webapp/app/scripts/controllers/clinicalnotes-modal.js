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
  .controller('ClinicalNotesModalCtrl', function ($scope, $modalInstance, UserService, clinicalNote, patient, modal) {

    $scope.currentUser = UserService.findCurrentUser();
    $scope.clinicalNote = clinicalNote;
    $scope.patient = patient;
    $scope.modal = modal;

    if (modal.title === 'Edit Clinical Note') {
      $scope.clinicalNote.dateCreated = new Date($scope.clinicalNote.dateCreated).toISOString().slice(0, 10);
    }
    else {
      $scope.clinicalNote.dateCreated = new Date().toISOString().slice(0, 10);
      $scope.clinicalNote.author = 'Dr Tony Shannon';
    }

    $scope.ok = function (clinicalNoteForm, clinicalNote) {
      $scope.formSubmitted = true;

      if (clinicalNoteForm.$valid) {
        $modalInstance.close(clinicalNote);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
