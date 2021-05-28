package project.fsurvey.core.adapter.abstracts;

public interface UserVerificationService {

    boolean validate(String nationalIdentity, String name, String surname, String birthYear);
}
