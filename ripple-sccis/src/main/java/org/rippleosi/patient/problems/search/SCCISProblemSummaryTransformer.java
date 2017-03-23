package org.rippleosi.patient.problems.search;


import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.problems.model.ProblemSummary;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

public class SCCISProblemSummaryTransformer implements Transformer<Node, List<ProblemSummary>> {
    @Override
    public List<ProblemSummary> transform(Node xml) {

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();

        List<ProblemSummary> problemList = new ArrayList<ProblemSummary>();

        try {
            xml.normalize();
            NodeList nodeSet = (NodeList) xpath.evaluate("/LCR/Disabilities/List/Condition", xml, XPathConstants.NODESET);
            for (int i = 0; i < nodeSet.getLength(); i++) {
                ProblemSummary problem = new ProblemSummary();
                problem.setSource("SC-CIS");

                Node node = nodeSet.item(i);
                String sourceId = (String) xpath.evaluate("identifier/value/@value", node, XPathConstants.STRING);
                problem.setSourceId(sourceId);
                String problemDiagnosis = (String) xpath.evaluate("code/coding/display/@value", node, XPathConstants.STRING);
                problem.setProblem(problemDiagnosis);
                String dateOfOnset = (String) xpath.evaluate("onsetDateTime/@value", node, XPathConstants.STRING);
                problem.setDateOfOnset(DateFormatter.toDate(dateOfOnset));
                problemList.add(problem);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return problemList;
    }

}
