package org.rippleosi.patient.problems.search;


import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class SCCISProblemDetailsTransformer implements Transformer<Node, ProblemDetails> {

    private String problemId;

    public SCCISProblemDetailsTransformer(String problemId) {
        this.problemId = problemId;
    }

    @Override
    public ProblemDetails transform(Node xml) {

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        ProblemDetails problem = new ProblemDetails();

        try {
            xml.normalize();
            NodeList nodeSet = (NodeList) xpath.evaluate("/LCR/Disabilities/List/Condition", xml, XPathConstants.NODESET);

            for (int i = 0; i < nodeSet.getLength(); i++) {


                Node node = nodeSet.item(i);
                String sourceId = (String) xpath.evaluate("identifier/value/@value", node, XPathConstants.STRING);
                if (sourceId.equalsIgnoreCase(problemId)) {
                    problem.setSource("SC-CIS");
                    problem.setSourceId(sourceId);
                    String problemDiagnosis = (String) xpath.evaluate("code/coding/display/@value", node, XPathConstants.STRING);
                    problem.setProblem(problemDiagnosis);
                    String dateOfOnset = (String) xpath.evaluate("onsetDateTime/@value", node, XPathConstants.STRING);
                    problem.setDateOfOnset(DateFormatter.toDate(dateOfOnset));
                    problem.setAuthor("Adult Social Care System");
                    break;
                }
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return problem;
    }

}
