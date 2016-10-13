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
package org.rippleosi.patient.mdtreports.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class MDTReportDetails implements Serializable {

    private String source;
    private String sourceId;
    private String serviceTeam;
    private Date dateOfRequest;
    private Date dateOfMeeting;
    private Date timeOfMeeting;
    private String servicePageLink;
    private String question;
    private String notes;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getServiceTeam() {
        return serviceTeam;
    }

    public void setServiceTeam(String serviceTeam) {
        this.serviceTeam = serviceTeam;
    }

    public Date getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(Date dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public Date getDateOfMeeting() {
        return dateOfMeeting;
    }

    public void setDateOfMeeting(Date dateOfMeeting) {
        this.dateOfMeeting = dateOfMeeting;
    }

    public Date getTimeOfMeeting() {
        return timeOfMeeting;
    }

    public void setTimeOfMeeting(Date timeOfMeeting) {
        this.timeOfMeeting = timeOfMeeting;
    }

    public String getServicePageLink() {
        return servicePageLink;
    }

    public void setServicePageLink(String servicePageLink) {
        this.servicePageLink = servicePageLink;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
