package org.rippleosi.patient.contacts.search;

import org.rippleosi.common.service.AbstractSCCISService;
import org.rippleosi.patient.contacts.model.ContactDetails;
import org.rippleosi.patient.contacts.model.ContactHeadline;
import org.rippleosi.patient.contacts.model.ContactSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SCCISContactSearch extends AbstractSCCISService implements ContactSearch {
    @Override
    public List<ContactHeadline> findContactHeadlines(String patientId) {

        SCCISContactHeadlineTransformer transformer = new SCCISContactHeadlineTransformer();

        return findData(patientId, transformer);
    }

    @Override
    public List<ContactSummary> findAllContacts(String patientId) {

        SCCISContactSummaryTransformer transformer = new SCCISContactSummaryTransformer();

        return findData(patientId, transformer);
    }

    @Override
    public ContactDetails findContact(String patientId, String contactId) {

        SCCISContactDetailsTransformer transformer = new SCCISContactDetailsTransformer(contactId);

        return findData(patientId, transformer);
    }

}
