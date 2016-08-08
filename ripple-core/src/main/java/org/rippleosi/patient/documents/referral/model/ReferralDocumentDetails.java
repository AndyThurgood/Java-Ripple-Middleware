/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.documents.referral.model;

import java.util.List;
import org.rippleosi.patient.documents.common.model.AbstractDocumentSummary;

/**
 */
public class ReferralDocumentDetails extends AbstractDocumentSummary {
        
    private String documentOrigionalSource;
    private String dischargeDate;
    
    private String composerName;
    private String facility;
    private String providerId;
    
    private String referralDateTime;
    private String referralType;
    private List<String> reasonForReferral;         // Repeating element
    private String referralComments;
    private String priorityOfReferral;
    private String referralReferenceNumber;
    private String referredFrom;
    private String referredTo;
    
    private String providerContact_organisationName;
    private String providerContact_id;
    private String providerContact_workNumber;
    private String providerContact_emergencyNumber;
    private String providerContact_email;
    
    private String referralStatus_code;
    private String referralStatus_value;
    private String referralStatus_mapped; // Mapped from code to origional message value
    
    private String clinicalNarrative;
    private String presentIllness;
    private String clinicalSynopsisComments;
    private String previousHospitalAttendance;
    
    List<NameDateElement> pastIllensses;            // Repeating element
    
    List<NameDateElement> surgicalProcedures;       // Repeating element
    
    List<MedicationDetails> medications;              // Repeating element
    
    private String medication_anticoagulation_use;

    List<AllergyDetails> allergies;                // Repeating element
    
    private String tobaccoUse;
    private String alcholUse;
    private String physicalImparement;

    private String systolicBP;
    private String systolicBP_units;
    private String diastolicBP;
    private String diastolicBP_units;
    private String pulse;
    private String pulse_units;
    private String height;
    private String height_units;
    private String weight;
    private String weight_units;
    private String bodyMass;
    private String bodyMass_units;
    
    private String otherExaminationFindings;
}
