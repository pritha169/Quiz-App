package Algo_project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Quiz {
    private static final int MAX_QUESTIONS = 5;
    private static final int MAX_OPTIONS = 4;

    private static Algo_project.User user;
    private static int classNumber;
    private static String firstSubject = "";
    private static int quizzesTaken = 0;
    private static JFrame frame;
    private static JPanel panel;
    private static CardLayout cardLayout;
    private static Algo_project.Question[] questions;
    private static int currentQuestionIndex;
    private static int score;
    private static Random rand;
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font TEXT_FIELD_FONT = new Font("Arial", Font.PLAIN, 14);
    public static void main(String[] args) {
        rand = new Random();
        // Show welcome message
        JOptionPane.showMessageDialog(null, "Welcome to the Quiz Application!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);
        // User login panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JButton loginButton = new JButton("Login");
        nameField.setFont(TEXT_FIELD_FONT);
        contactField.setFont(TEXT_FIELD_FONT);
        loginButton.setFont(BUTTON_FONT);
        loginPanel.add(new JLabel("Enter your name:"));
        loginPanel.add(nameField);
        loginPanel.add(new JLabel("Enter your phone number (11 digits):"));
        loginPanel.add(contactField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);
        for (Component component : loginPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setFont(LABEL_FONT);
            }
        }
        panel.add(loginPanel, "login");

        // Class selection panel
        JPanel classPanel = new JPanel(new GridLayout(4, 1));
        JButton class1Button = new JButton("Class 1");
        JButton class2Button = new JButton("Class 2");
        JButton class3Button = new JButton("Class 3");
        classPanel.add(new JLabel("Enter your class number:"));
        classPanel.add(class1Button);
        classPanel.add(class2Button);
        classPanel.add(class3Button);
        for (Component component : classPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setFont(LABEL_FONT);
            } else if (component instanceof JButton) {
                component.setFont(BUTTON_FONT);
            }
        }
        panel.add(classPanel, "classSelection");
        // Subject selection panel
        JPanel subjectPanel = new JPanel(new GridLayout(3, 1));
        JButton mathButton = new JButton("Math");
        JButton scienceButton = new JButton("Science");
        subjectPanel.add(new JLabel("Enter the subject:"));
        subjectPanel.add(mathButton);
        subjectPanel.add(scienceButton);

        for (Component component : subjectPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setFont(LABEL_FONT);
            } else if (component instanceof JButton) {
                component.setFont(BUTTON_FONT);
            }
        }
        panel.add(subjectPanel, "subjectSelection");
        // Quiz panel
        JPanel quizPanel = new JPanel(new GridLayout(6, 1));
        JLabel questionLabel = new JLabel();
        JRadioButton option1 = new JRadioButton();
        JRadioButton option2 = new JRadioButton();
        JRadioButton option3 = new JRadioButton();
        JRadioButton option4 = new JRadioButton();
        ButtonGroup optionsGroup = new ButtonGroup();
        JButton submitAnswerButton = new JButton("Submit Answer");
        questionLabel.setFont(LABEL_FONT);
        option1.setFont(TEXT_FIELD_FONT);
        option2.setFont(TEXT_FIELD_FONT);
        option3.setFont(TEXT_FIELD_FONT);
        option4.setFont(TEXT_FIELD_FONT);
        submitAnswerButton.setFont(BUTTON_FONT);
        optionsGroup.add(option1);
        optionsGroup.add(option2);
        optionsGroup.add(option3);
        optionsGroup.add(option4);
        quizPanel.add(questionLabel);
        quizPanel.add(option1);
        quizPanel.add(option2);
        quizPanel.add(option3);
        quizPanel.add(option4);
        quizPanel.add(submitAnswerButton);
        for (Component component : quizPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setFont(LABEL_FONT);
            } else if (component instanceof JRadioButton) {
                component.setFont(TEXT_FIELD_FONT);
            } else if (component instanceof JButton) {
                component.setFont(BUTTON_FONT);
            }
        }
        panel.add(quizPanel, "quiz");
        frame.add(panel);
        frame.setVisible(true);
        // Action Listeners
        loginButton.addActionListener(e -> {
            String name = nameField.getText();
            String contact = contactField.getText();
            if (isValidPhoneNumber(contact)) {
                user = new Algo_project.User(name, contact);
                cardLayout.show(panel, "classSelection");
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid 11-digit phone number.", "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);
            }
        });
        class1Button.addActionListener(e -> {
            classNumber = 1;
            cardLayout.show(panel, "subjectSelection");
        });

        class2Button.addActionListener(e -> {
            classNumber = 2;
            cardLayout.show(panel, "subjectSelection");
        });
        class3Button.addActionListener(e -> {
            classNumber = 3;
            cardLayout.show(panel, "subjectSelection");
        });
        ActionListener subjectListener = e -> {
            String subject = ((JButton) e.getSource()).getText();
            if (quizzesTaken == 0) {
                firstSubject = subject;
            }
            startQuiz(subject);
        };
        mathButton.addActionListener(subjectListener);
        scienceButton.addActionListener(subjectListener);
        submitAnswerButton.addActionListener(e -> {
            JRadioButton selectedButton = null;
            if (option1.isSelected()) {
                selectedButton = option1;
            } else if (option2.isSelected()) {
                selectedButton = option2;
            } else if (option3.isSelected()) {
                selectedButton = option3;
            } else if (option4.isSelected()) {
                selectedButton = option4;
            }

            if (selectedButton != null) {
                int selectedOption = Integer.parseInt(selectedButton.getActionCommand());
                if (selectedOption == questions[currentQuestionIndex].answer) {
                    score++;
                }
                currentQuestionIndex++;
                if (currentQuestionIndex < MAX_QUESTIONS) {
                    displayQuestion(currentQuestionIndex, questionLabel, option1, option2, option3, option4);
                } else {
                    quizzesTaken++;
                    JOptionPane.showMessageDialog(frame, "You answered " + score + " out of " + MAX_QUESTIONS + " questions correctly.");
                    if (quizzesTaken < 2) {
                        String nextSubject = firstSubject.equalsIgnoreCase("Math") ? "Science" : "Math";
                        int result = JOptionPane.showConfirmDialog(frame, "Do you want to take another quiz in " + nextSubject + "?", "Quiz Result", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            startQuiz(nextSubject);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Thank you for taking the quizzes, " + user.getName() + "!");
                            frame.dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "You answered " + score + " out of " + MAX_QUESTIONS + " questions correctly in the second quiz.\nThank you for taking the quizzes, " + user.getName() + "!");
                        frame.dispose();
                    }
                }
            }
        });
    }

    private static void startQuiz(String subject) {
        questions = new Algo_project.Question[MAX_QUESTIONS];
        generateQuestions(classNumber, subject, questions, rand);
        currentQuestionIndex = 0;
        score = 0;
        cardLayout.show(panel, "quiz");
        displayQuestion(currentQuestionIndex, (JLabel) ((JPanel) panel.getComponent(3)).getComponent(0),
                (JRadioButton) ((JPanel) panel.getComponent(3)).getComponent(1),
                (JRadioButton) ((JPanel) panel.getComponent(3)).getComponent(2),
                (JRadioButton) ((JPanel) panel.getComponent(3)).getComponent(3),
                (JRadioButton) ((JPanel) panel.getComponent(3)).getComponent(4));
    }
    private static void displayQuestion(int questionIndex, JLabel questionLabel, JRadioButton option1, JRadioButton option2, JRadioButton option3, JRadioButton option4) {
        Algo_project.Question question = questions[questionIndex];
        questionLabel.setText("Question " + (questionIndex + 1) + ": " + question.questionText);
        option1.setText(question.options[0]);
        option1.setActionCommand("0");
        option2.setText(question.options[1]);
        option2.setActionCommand("1");
        option3.setText(question.options[2]);
        option3.setActionCommand("2");
        option4.setText(question.options[3]);
        option4.setActionCommand("3");
        option1.setSelected(false);
        option2.setSelected(false);
        option3.setSelected(false);
        option4.setSelected(false);
    }

    private static void generateQuestions(int classNumber, String subject, Algo_project.Question[] questions, Random rand) {
        int setNumber = rand.nextInt(2);
        System.out.println("Generating questions for " + subject + " in class " + classNumber + ", Set number: " + (setNumber + 1)); // Print the set number
        if (classNumber == 1 && subject.equalsIgnoreCase("Math")) {
            if (setNumber == 0) {
                getClass1MathQuestionsSet1(questions);
            } else {
                getClass1MathQuestionsSet2(questions);
            }
        } else if (classNumber == 2 && subject.equalsIgnoreCase("Math")) {
            if (setNumber == 0) {
                getClass2MathQuestionsSet1(questions);
            } else {
                getClass2MathQuestionsSet2(questions);
            }
        } else if (classNumber == 3 && subject.equalsIgnoreCase("Math")) {
            if (setNumber == 0) {
                getClass3MathQuestionsSet1(questions);
            } else {
                getClass3MathQuestionsSet2(questions);
            }
        } else if (classNumber == 1 && subject.equalsIgnoreCase("Science")) {
            if (setNumber == 0) {
                getClass1ScienceQuestionsSet1(questions);
            } else {
                getClass1ScienceQuestionsSet2(questions);
            }
        } else if (classNumber == 2 && subject.equalsIgnoreCase("Science")) {
            if (setNumber == 0) {
                getClass2ScienceQuestionsSet1(questions);
            } else {
                getClass2ScienceQuestionsSet2(questions);
            }
        } else if (classNumber == 3 && subject.equalsIgnoreCase("Science")) {
            if (setNumber == 0) {
                getClass3ScienceQuestionsSet1(questions);
            } else {
                getClass3ScienceQuestionsSet2(questions);
            }
        }
    }
    private static void getClass1MathQuestionsSet1(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 1 Math Question: What is 2 + 2?", 1, new String[]{"1", "4", "3", "2"});
        questions[1] = new Algo_project.Question("Class 1 Math Question: What is 3 * 5?", 1, new String[]{"12", "15", "18", "20"});
        questions[2] = new Algo_project.Question("Class 1 Math Question: What is 10 - 7?", 2, new String[]{"4", "2", "3", "1"});
        questions[3] = new Algo_project.Question("Class 1 Math Question: What is 20 / 5?", 3, new String[]{"1", "2", "3", "4"});
        questions[4] = new Algo_project.Question("Class 1 Math Question: What is 10 + 7?", 0, new String[]{"17", "13", "14", "12"});
    }
    private static void getClass1MathQuestionsSet2(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 1 Math Question: What is 5 + 3?", 0, new String[]{"8", "9", "7", "10"});
        questions[1] = new Algo_project.Question("Class 1 Math Question: What is 7 - 2?", 1, new String[]{"4", "5", "6", "3"});
        questions[2] = new Algo_project.Question("Class 1 Math Question: What is 4 * 2?", 2, new String[]{"6", "7", "8", "9"});
        questions[3] = new Algo_project.Question("Class 1 Math Question: What is 16 * 2?", 2, new String[]{"30", "16", "32", "36"});
        questions[4] = new Algo_project.Question("Class 1 Math Question: What is 50 / 2?", 2, new String[]{"15", "18", "25", "20"});
    }
    private static void getClass1ScienceQuestionsSet1(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 1 Science Question: What is the chemical symbol for water?", 0, new String[]{"H2O", "O2", "CO2", "H2"});
        questions[1] = new Algo_project.Question("Class 1 Science Question: What planet is known as the Red Planet?", 3, new String[]{"Venus", "Jupiter", "Saturn", "Mars"});
        questions[2] = new Algo_project.Question("Class 1 Science Question: What do bees produce?", 2, new String[]{"Milk", "Sugar", "Honey", "Wax"});
        questions[3] = new Algo_project.Question("Class 1 Science Question: Which of these animals lays eggs?", 3, new String[]{"Sheep", "Cat", "Dog", "Chicken"});
        questions[4] = new Algo_project.Question("Class 1 Science Question: A female horse is called a ________ .", 2, new String[]{"Stallion", "Horse", "Mare", "Pony"});
    }
    private static void getClass1ScienceQuestionsSet2(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 1 Science Question: What is the main source of energy for the Earth?", 1, new String[]{"Moon", "Sun", "Stars", "Ocean"});
        questions[1] = new Algo_project.Question("Class 1 Science Question: What gas do plants need for photosynthesis?", 2, new String[]{"Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"});
        questions[2] = new Algo_project.Question("Class 1 Science Question: What is the tallest animal on Earth?", 0, new String[]{"Giraffe", "Elephant", "Lion", "Tiger"});
        questions[3] = new Algo_project.Question("Class 1 Science Question: All living things need food, air, and ____ to survive.", 1, new String[]{"Home", "Water", "Vegetables", "Fruits"});
        questions[4] = new Algo_project.Question("Class 1 Science Question: Which of these has feathers?", 0, new String[]{"Hen", "Crocodile", "Tortoise", "Tiger"});
    }
    private static void getClass2MathQuestionsSet1(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 2 Math Question: What is 12 + 15?", 1, new String[]{"26", "27", "28", "29"});
        questions[1] = new Algo_project.Question("Class 2 Math Question: What is 7 * 6?", 2, new String[]{"40", "41", "42", "43"});
        questions[2] = new Algo_project.Question("Class 2 Math Question: What is 15 / 3?", 0, new String[]{"5", "6", "7", "8"});
        questions[3] = new Algo_project.Question("Class 2 Math Question: 1 + __ = 5", 0, new String[]{"4", "3", "7", "2"});
        questions[4] = new Algo_project.Question("Class 2 Math Question: __ – 6 = 8", 1, new String[]{"12", "14", "15", "2"});
    }
    private static void getClass2MathQuestionsSet2(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 2 Math Question: What is 8 + 7?", 2, new String[]{"12", "13", "15", "14"});
        questions[1] = new Algo_project.Question("Class 2 Math Question: What is 9 * 3?", 1, new String[]{"25", "27", "29", "28"});
        questions[2] = new Algo_project.Question("Class 2 Math Question: What is 16 - 8?", 0, new String[]{"8", "9", "10", "11"});
        questions[3] = new Algo_project.Question("Class 2 Math Question: 13 – 5 = __ ?", 0, new String[]{"8", "6", "10", "11"});
        questions[4] = new Algo_project.Question("Class 2 Math Question: 2 + 12 = ___ ?", 1, new String[]{"12", "14", "10", "11"});
    }

    private static void getClass2ScienceQuestionsSet1(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 2 Science Question: What is the chemical symbol for carbon dioxide?", 2, new String[]{"CO", "C2O", "CO2", "C2"});
        questions[1] = new Algo_project.Question("Class 2 Science Question: Which planet is known as the Blue Planet?", 0, new String[]{"Earth", "Mars", "Jupiter", "Saturn"});
        questions[2] = new Algo_project.Question("Class 2 Science Question: What do plants produce during photosynthesis?", 1, new String[]{"Water", "Oxygen", "Carbon Dioxide", "Glucose"});
        questions[3] = new Algo_project.Question("Class 2 Science Question: Which is the only natural satellite of Earth?", 1, new String[]{"Sun", "Moon", "Stars", "Mars"});
        questions[4] = new Algo_project.Question("Class 2 Science Question: A tree is fixed to the soil with its _____ .", 1, new String[]{"Branches", "Roots", "leaves", "Glucose"});
    }
    private static void getClass2ScienceQuestionsSet2(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 2 Science Question: What is the boiling point of water?", 3, new String[]{"50°C", "75°C", "90°C", "100°C"});
        questions[1] = new Algo_project.Question("Class 2 Science Question: What gas do humans exhale?", 2, new String[]{"Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"});
        questions[2] = new Algo_project.Question("Class 2 Science Question: Which planet is the closest to the Sun?", 0, new String[]{"Mercury", "Venus", "Earth", "Mars"});
        questions[3] = new Algo_project.Question("Class 2 Science Question: The covers of the eyes are called __________ .", 0, new String[]{"Eyelids", "Nostrils", "Nails", "Hair"});
        questions[4] = new Algo_project.Question("Class 2 Science Question: We breathe with the help of our ________ .", 1, new String[]{"Heart", "Lungs", "Pancreas", "Muscles"});
    }
    private static void getClass3MathQuestionsSet1(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 3 Math Question: What is 25 + 37?", 1, new String[]{"50", "62", "65", "68"});
        questions[1] = new Algo_project.Question("Class 3 Math Question: What is 9 * 8?", 3, new String[]{"70", "71", "72", "73"});
        questions[2] = new Algo_project.Question("Class 3 Math Question: What is 45 / 9?", 0, new String[]{"5", "6", "7", "8"});
        questions[3] = new Algo_project.Question("Class 3 Math Question: 10 less than 34 is = ?", 2, new String[]{"44", "21", "24", "30"});
        questions[4] = new Algo_project.Question("Class 3 Math Question: 11 more than 31 is = ?", 2, new String[]{"45", "61", "42", "28"});
    }
    private static void getClass3MathQuestionsSet2(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 3 Math Question: What is 14 + 29?", 2, new String[]{"41", "42", "43", "44"});
        questions[1] = new Algo_project.Question("Class 3 Math Question: What is 8 * 7?", 1, new String[]{"55", "56", "57", "58"});
        questions[2] = new Algo_project.Question("Class 3 Math Question: What is 81 / 9?", 0, new String[]{"9", "10", "11", "12"});
        questions[3] = new Algo_project.Question("Class 3 Math Question: 23 less than 89 is = ?", 0, new String[]{"66", "23", "89", "122"});
        questions[4] = new Algo_project.Question("Class 3 Math Question:  9 added to 28 gives = ?", 0, new String[]{"37", "10", "35", "12"});
    }

    private static void getClass3ScienceQuestionsSet1(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 3 Science Question: What is the chemical formula for water?", 0, new String[]{"H2O", "O2", "CO2", "H2"});
        questions[1] = new Algo_project.Question("Class 3 Science Question: What planet is known as the Red Planet?", 3, new String[]{"Venus", "Jupiter", "Saturn", "Mars"});
        questions[2] = new Algo_project.Question("Class 3 Science Question: What do bees produce?", 2, new String[]{"Milk", "Sugar", "Honey", "Wax"});
        questions[3] = new Algo_project.Question("Class 3 Science Question: Arms have hands and legs have ___.?", 1, new String[]{"Ankles", "Feet", "Elbows", "Knees"});
        questions[4] = new Algo_project.Question("Class 3 Science Question: The young one of a sheep is called a _______ .?", 3, new String[]{"Puppy", "Kid", "Calf", "Lamb"});

    }

    private static void getClass3ScienceQuestionsSet2(Algo_project.Question[] questions) {
        questions[0] = new Algo_project.Question("Class 3 Science Question: What is the main source of energy for the Earth?", 1, new String[]{"Moon", "Sun", "Stars", "Ocean"});
        questions[1] = new Algo_project.Question("Class 3 Science Question: What gas do plants need for photosynthesis?", 2, new String[]{"Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"});
        questions[2] = new Algo_project.Question("Class 3 Science Question: What is the tallest animal on Earth?", 0, new String[]{"Giraffe", "Elephant", "Lion", "Tiger"});
        questions[3] = new Algo_project.Question("Class 3 Science Question: Animals that lay eggs to give birth to their young one are called ____.?", 2, new String[]{"Reptiles", "Elephants", "Birds", "Mammals"});
        questions[4] = new Algo_project.Question("Class 3 Science Question: A frog is a ___________ .?", 0, new String[]{"Mammal", "Amphibian", "Reptile", "Bird"});

    }
    private static boolean isValidPhoneNumber(String phoneNumber) {
        // Check if the phone number is exactly 11 digits and consists only of digits
        return phoneNumber.matches("\\d{11}");
    }
}
