import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuizHandler {
    private List<Quiz> quizzes;
    private static final char delimiter = '@';
    private static final File xmlFileName = new File("src\\file.xml");

    /**
     * Format: question@answer1@answer2@...@answer_i
     *
     * @param quizID: quiz id
     * @return String
     */
    public String packUpQuiz(int quizID) {
        StringBuilder finalObject = new StringBuilder();
        Quiz quiz = quizzes.get(quizID);
        finalObject.append(quizID).append(delimiter);   // ID intrebare
        finalObject.append(quiz.getQuestion()).append(delimiter);   // intrebare String

        for (String answer : quiz.getAnswerList()) {
            finalObject.append(answer).append(delimiter);
        }

        return finalObject.toString();
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public QuizHandler() {
        quizzes = new ArrayList<>();
        parseXMLDocument();

        for (Quiz q : quizzes) {
            System.out.println(q.toString());
        }
    }

    public boolean isRightAnswer(int quizID, int difficulty, String ra) {
        Quiz quiz = getQuizzes().get(quizID);
        if (difficulty == 1) {
            return quiz.getRightAnswerID() == (ra.charAt(0) - 65);
        } else {
            for (String answer : quiz.getAnswerList()) {
                if (answer.equals(ra))
                    return true;
            }
            return false;
        }
    }

    private void parseXMLDocument() {

        try
        {
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFileName);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("quiz");

            printNodeList(nodeList, doc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void printNodeList(NodeList nodeList, Document doc)
    {
        // nodeList is not iterable, so we are using for loop
        for (int itr = 0; itr < nodeList.getLength(); itr++)
        {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Quiz quiz = new Quiz();
                Element eElement = (Element) node;

                quiz.setQuestion(eElement.getElementsByTagName("question").item(0).getTextContent());
                //System.out.println("question: " + eElement.getElementsByTagName("question").item(0).getTextContent());

                NodeList fieldNodes = eElement.getElementsByTagName("question");
                Node fieldNode = fieldNodes.item(0);
                NamedNodeMap attributes = fieldNode.getAttributes();
                quiz.setRightAnswerID(Integer.parseInt(attributes.getNamedItem("right_answer").getTextContent()));

                fieldNodes = eElement.getElementsByTagName("answers");
                for(int j = 0; j < fieldNodes.getLength(); j++) {
                    String content = fieldNodes.item(j).getTextContent();
                    quiz.addAnswer(content);
                }
                quizzes.add(quiz);
            }
        }
    }
}