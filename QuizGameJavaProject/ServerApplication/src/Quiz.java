import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String question;
    private int rightAnswerID = 0;
    private List<String> answerList = new ArrayList<>();

    public Quiz() {}

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setRightAnswerID(int rightAnswerID) {
        this.rightAnswerID = rightAnswerID;
    }

    public void addAnswer(String answer) {
        answerList.add(answer.trim());
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public int getRightAnswerID() {
        return rightAnswerID;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "question='" + question + '\'' +
                ", rightAnswerID=" + rightAnswerID +
                ", answerList=" + answerList +
                '}';
    }
}
