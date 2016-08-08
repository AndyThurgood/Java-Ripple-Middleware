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
package org.rippleosi.patient.vitals.rest;

import java.util.List;

import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.common.types.lookup.RepoSourceLookupFactory;
import org.rippleosi.patient.vitals.model.VitalsDetails;
import org.rippleosi.patient.vitals.search.VitalsSearch;
import org.rippleosi.patient.vitals.search.VitalsSearchFactory;
import org.rippleosi.patient.vitals.store.VitalsStore;
import org.rippleosi.patient.vitals.store.VitalsStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("patients/{patientId}/vitals")
public class VitalsController {

    @Autowired
    private RepoSourceLookupFactory repoSourceLookup;

    @Autowired
    private VitalsSearchFactory vitalsSearchFactory;

    @Autowired
    private VitalsStoreFactory vitalsStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<VitalsDetails> findAllAllergies(@PathVariable("patientId") String patientId,
                                                @RequestParam(required = false) String source) {
        final RepoSourceType sourceType = repoSourceLookup.lookup(source);
        VitalsSearch search = vitalsSearchFactory.select(sourceType);

        return search.findAllAllergies(patientId);
    }

    @RequestMapping(value = "/{vitalsId}", method = RequestMethod.GET)
    public VitalsDetails findAllergy(@PathVariable("patientId") String patientId,
                                     @PathVariable("vitalsId") String vitalsId,
                                     @RequestParam(required = false) String source) {
        final RepoSourceType sourceType = repoSourceLookup.lookup(source);
        VitalsSearch search = vitalsSearchFactory.select(sourceType);

        return search.findAllergy(patientId, vitalsId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createAllergy(@PathVariable("patientId") String patientId,
                              @RequestParam(required = false) String source,
                              @RequestBody VitalsDetails vitals) {
        final RepoSourceType sourceType = repoSourceLookup.lookup(source);
        VitalsStore store = vitalsStoreFactory.select(sourceType);

        store.create(patientId, vitals);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateAllergy(@PathVariable("patientId") String patientId,
                              @RequestParam(required = false) String source,
                              @RequestBody VitalsDetails vitals) {
        final RepoSourceType sourceType = repoSourceLookup.lookup(source);
        VitalsStore store = vitalsStoreFactory.select(sourceType);

        store.update(patientId, vitals);
    }
}
