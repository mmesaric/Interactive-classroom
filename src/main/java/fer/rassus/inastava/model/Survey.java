package fer.rassus.inastava.model;

import java.util.ArrayList;
import java.util.List;

public class Survey {
    private String name;
    private String description;
    private String isActiveString;
    private List<String> answers = new ArrayList<>();
    private List<String> questions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsActiveString() {
        return isActiveString;
    }

    public void setIsActiveString(String isActiveString) {
        this.isActiveString = isActiveString;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
