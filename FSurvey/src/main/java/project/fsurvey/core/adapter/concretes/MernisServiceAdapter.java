package project.fsurvey.core.adapter.concretes;

import org.springframework.stereotype.Service;
import project.fsurvey.core.adapter.abstracts.UserVerificationService;
import project.fsurvey.mernis.CUKKPSPublicSoap;

import java.util.Locale;

@Service
public class MernisServiceAdapter implements UserVerificationService {

    @Override
    public boolean validate(String nationalIdentity,
                            String name,
                            String surname,
                            String birthYear){
        CUKKPSPublicSoap mernis = new CUKKPSPublicSoap();
        Locale tr = new Locale("tr", "TR");
        try {
            return mernis.TCKimlikNoDogrula(Long.parseLong(nationalIdentity),
                    name.toUpperCase(tr),
                    surname.toUpperCase(tr),
                    Integer.valueOf(birthYear));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
