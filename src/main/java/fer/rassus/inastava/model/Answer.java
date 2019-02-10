package fer.rassus.inastava.model;

import java.util.HashMap;
import java.util.Map;

public class Answer {

    Map<String, String> priorities = new HashMap<>();

    public Map<String, String> getPriorities() {
        return priorities;
    }

    public void setPriorities(Map<String, String> priorities) {
        this.priorities = priorities;
    }

}
