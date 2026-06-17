package com.project.FixMyStreet.Service;

import com.project.FixMyStreet.Models.OTP;
import com.project.FixMyStreet.Repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepo;
    public String generateOtp()
    {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000,999999));

    }

    public void saveOtp(String email,String otp)
    {
        otpRepo.findByEmail(email).ifPresent(existingOtp->
                otpRepo.delete(existingOtp));

        OTP otpEntity = OTP.builder()
                .email(email)
                .otp(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        otpRepo.save(otpEntity);
    }

    public boolean verifyOtp(String email, String otp) {

        System.out.println("Verifying OTP");
        System.out.println("Email entered: " + email);
        System.out.println("OTP entered: " + otp);

        Optional<OTP> otpOptional =
                otpRepo.findByEmail(email);

        if (otpOptional.isEmpty()) {
            System.out.println("No OTP found in DB");
            return false;
        }

        OTP storedOtp = otpOptional.get();

        System.out.println("Stored OTP: " + storedOtp.getOtp());
        System.out.println("Expires At: " + storedOtp.getExpiresAt());
        System.out.println("Current Time: " + LocalDateTime.now());

        if (storedOtp.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            System.out.println("OTP expired");
            otpRepo.delete(storedOtp);
            return false;
        }

        boolean match = storedOtp.getOtp().equals(otp);

        System.out.println("OTP Match: " + match);

        return match;
    }
/*     Original
    public boolean verifyOtp(String email, String otp) {

        Optional<OTP> otpOptional =
                otpRepo.findByEmail(email);

        if (otpOptional.isEmpty()) {
            return false;
        }

        OTP storedOtp = otpOptional.get();

        if (storedOtp.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            otpRepo.delete(storedOtp);
            return false;
        }

        return storedOtp.getOtp().equals(otp);
    }


*/
    public void deleteOtp(String email) {

        otpRepo.findByEmail(email)
                .ifPresent(otpRepo::delete);
    }


}
