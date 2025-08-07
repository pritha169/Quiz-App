package Algo_project;


public class Question {
    String questionText;
    int answer;
    String[] options = new String[4];
    Question(String questionText, int answer, String[] options) {
        this.questionText = questionText;
        this.answer = answer;
        System.arraycopy(options, 0, this.options, 0, 4);
    }
}