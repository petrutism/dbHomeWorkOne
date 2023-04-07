package lt.code.academy.data;

public class Answer {
    private final int id;
    private final int question_id;
    private final String body;
    private final String variant;
    boolean is_correct;

    public Answer(int id, int question_id, String body, String variant, boolean is_correct) {
        this.id = id;
        this.question_id = question_id;
        this.body = body;
        this.variant = variant;
        this.is_correct = is_correct;
    }

    public int getId() {
        return id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public String getVariant() {
        return variant;
    }

    @Override
    public String toString() {
        return variant + ": " + body;
    }
}