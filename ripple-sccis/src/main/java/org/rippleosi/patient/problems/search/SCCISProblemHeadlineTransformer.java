package org.rippleosi.patient.problems.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;


public class SCCISProblemHeadlineTransformer implements Transformer<Node, List<ProblemHeadline>> {
    @Override
    public List<ProblemHeadline> transform(Node xml) {

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();

        List<ProblemHeadline> problemList = new ArrayList<ProblemHeadline>();


        try {
            xml.normalize();
            NodeList nodeSet = (NodeList) xpath.evaluate("/LCR/Disabilities/List/Condition", xml, XPathConstants.NODESET);
            for (int i = 0; i < nodeSet.getLength(); i++) {
                ProblemHeadline problem = new ProblemHeadline();
                problem.setSource("SC-CIS");

                Node node = nodeSet.item(i);
                String sourceId = (String) xpath.evaluate("identifier/value/@value", node, XPathConstants.STRING);
                problem.setSourceId(sourceId);
                String problemDiagnosis = (String) xpath.evaluate("code/coding/display/@value", node, XPathConstants.STRING);
                problem.setProblem(problemDiagnosis);
                problemList.add(problem);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return problemList;
    }
}

