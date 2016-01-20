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
package org.rippleosi.patient.dicom.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.dicom.model.DicomImage;
import org.rippleosi.patient.dicom.model.DicomStudySummary;
import org.rippleosi.patient.dicom.model.DicomSeriesThumbnail;

public class NotConfiguredDicomSearch implements DicomSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<DicomStudySummary> findAllDicomStudies(String patientId, String source) {
        throw ConfigurationException.unimplementedTransaction(DicomSearch.class);
    }

    @Override
    public List<DicomSeriesThumbnail> findAllDicomSeriesThumbnails(String patientId, String seriesId, String source) {
        throw ConfigurationException.unimplementedTransaction(DicomSearch.class);
    }

    @Override
    public DicomImage findDicomImage(String patientId, String imageId, String source) {
        throw ConfigurationException.unimplementedTransaction(DicomSearch.class);
    }
}
