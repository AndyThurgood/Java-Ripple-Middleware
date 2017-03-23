package org.rippleosi.patient.referral.search;


import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.referral.model.ReferralSummary;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

public class SCCISReferralSummaryTransformer implements Transformer<Node, List<ReferralSummary>> {
    @Override
    public List<ReferralSummary> transform(Node xml) {

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();

        List<ReferralSummary> referralList = new ArrayList<ReferralSummary>();

        try {
            xml.normalize();
            NodeList nodeSet = (NodeList) xpath.evaluate("/LCR/ReferralRequest", xml, XPathConstants.NODESET);
            for (int i = 0; i < nodeSet.getLength(); i++) {
                ReferralSummary referral = new ReferralSummary();
                referral.setSource("SC-CIS");

                Node node = nodeSet.item(i);
                String sourceId = (String) xpath.evaluate("identifier/value/@value", node, XPathConstants.STRING);
                referral.setSourceId(sourceId);
                referral.setReferralState("Planned");
                String referralFrom1 = (String) xpath.evaluate("type/coding/display/@value", node, XPathConstants.STRING);
                String referralFrom2 = (String) xpath.evaluate("priority/coding/display/@value", node, XPathConstants.STRING);
                referral.setReferralFrom(referralFrom1 + "/" + referralFrom2);
                String referralTo = (String) xpath.evaluate("serviceRequested/coding/display/@value", node, XPathConstants.STRING);
                referral.setReferralTo(referralTo);
                String dateRequested = (String) xpath.evaluate("dateSent/@value", node, XPathConstants.STRING);
                referral.setDateOfReferral(DateFormatter.toDate(dateRequested));
                referralList.add(referral);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return referralList;
    }


}
