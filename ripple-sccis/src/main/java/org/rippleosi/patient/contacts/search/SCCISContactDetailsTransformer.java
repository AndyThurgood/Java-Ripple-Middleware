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
package org.rippleosi.patient.contacts.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.contacts.model.ContactDetails;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class SCCISContactDetailsTransformer implements Transformer<Node, ContactDetails> {

    private String contactId;

    public SCCISContactDetailsTransformer(String contactId) {
        this.contactId = contactId;
    }

    @Override
    public ContactDetails transform(Node xml) {

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        ContactDetails contact = new ContactDetails();
        boolean foundContact = false;

        try {
            // Search contacts from Carers section of XML
            NodeList nodeSet =  (NodeList) xpath.evaluate("/LCR/Carers/List/RelatedPerson", xml, XPathConstants.NODESET);
            for (int i = 0; i < nodeSet.getLength(); i++) {
                Node node = nodeSet.item(i);
                String sourceId = (String) xpath.evaluate("identifier/value/@value", node, XPathConstants.STRING);

                if (sourceId.equalsIgnoreCase(contactId)) {

                    contact.setSource("SC-CIS");

                    String name = (String) xpath.evaluate("name/text/@value", node, XPathConstants.STRING);
                    String relationshipTeam = (String) xpath.evaluate("relationship/coding/display/@value", node, XPathConstants.STRING);
                    String address = (String) xpath.evaluate("address/text/@value", node, XPathConstants.STRING);
                    String phone = (String) xpath.evaluate("telecom[1]/value/@value", node, XPathConstants.STRING);

                    contact.setSourceId(sourceId);
                    contact.setName(name);
                    contact.setRelationship(relationshipTeam);

                    contact.setAddress(address);
                    contact.setPhone(phone);
                    contact.setAuthor("Adult Social Care System");
                    foundContact = true;

                    break;
                }

            }

            if (!foundContact) {
                // Search contacts from Allocations section of XML
                nodeSet = (NodeList) xpath.evaluate("/LCR/Allocations/List/Practitioner", xml, XPathConstants.NODESET);
                for (int i = 0; i < nodeSet.getLength(); i++) {
                    Node node = nodeSet.item(i);
                    String sourceId = (String) xpath.evaluate("identifier/value/@value", node, XPathConstants.STRING);

                    if (sourceId.equalsIgnoreCase(contactId)) {

                        contact.setSource("SC-CIS");

                        String name = (String) xpath.evaluate("name/text/@value", node, XPathConstants.STRING);
                        String relationshipTeam = (String) xpath.evaluate("practitionerRole/role/coding/display/@value", node, XPathConstants.STRING);
                        String phone = (String) xpath.evaluate("telecom[1]/value/@value", node, XPathConstants.STRING);

                        contact.setSourceId(sourceId);
                        contact.setName(name);
                        contact.setRelationship(relationshipTeam);

                        contact.setPhone(phone);
                        contact.setAuthor("Adult Social Care System");

                        break;
                    }

                }
            }


        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }


        return contact;
    }
}
