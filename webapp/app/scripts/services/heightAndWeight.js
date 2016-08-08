'use strict';

angular.module('rippleDemonstrator')
  .factory('HeightAndWeight', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/heightAndWeight/');
    };

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/heightAndWeight/' + compositionId);
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/heightAndWeight', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/heightAndWeight', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });
