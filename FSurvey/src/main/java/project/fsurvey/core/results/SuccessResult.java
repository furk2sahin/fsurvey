package project.fsurvey.core.results;


public class SuccessResult extends Result{

    public SuccessResult() {
        super(true);
    }

    public SuccessResult(String message) {
        super(true, message);
    }

    public SuccessResult(boolean success, String message) {
        super(true, message);
    }
}
