package org.rippleosi.patient.referral.search;


import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.referral.model.ReferralDetails;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class SCCISReferralDetailsTransformer implements Transformer<Node, ReferralDetails> {

    private String referralRef;

    public SCCISReferralDetailsTransformer(String referralRef) {
        this.referralRef = referralRef;
    }

    @Override
    public ReferralDetails transform(Node xml) {

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        ReferralDetails referral = new ReferralDetails();

        try {
            xml.normalize();
            NodeList nodeSet = (NodeList) xpath.evaluate("/LCR/ReferralRequest", xml, XPathConstants.NODESET);

            for (int i = 0; i < nodeSet.getLength(); i++) {
                Node node = nodeSet.item(i);
                String sourceId = (String) xpath.evaluate("identifier/value/@value", node, XPathConstants.STRING);
                if (sourceId.equalsIgnoreCase(referralRef)) {
                    referral.setSourceId(sourceId);
                    referral.setReference(sourceId);  // Use Source ID for the Referral Ref at present
                    referral.setSource("SC-CIS");
                    referral.setReferralState("Planned");
                    String referralFrom1 = (String) xpath.evaluate("type/coding/display/@value", node, XPathConstants.STRING);
                    String referralFrom2 = (String) xpath.evaluate("priority/coding/display/@value", node, XPathConstants.STRING);
                    referral.setReferralFrom(referralFrom1 + "/" + referralFrom2);
                    String referralTo = (String) xpath.evaluate("serviceRequested/coding/display/@value", node, XPathConstants.STRING);
                    referral.setReferralTo(referralTo);
                    String dateRequested = (String) xpath.evaluate("dateSent/@value", node, XPathConstants.STRING);
                    referral.setDateOfReferral(DateFormatter.toDate(dateRequested));
                    referral.setAuthor("Adult Social Care System");
                    break;
                }

            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return referral;
    }

}
