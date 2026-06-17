package com.project.FixMyStreet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


// 1. Send OTP
// 2.Complaint Assigned Email
// 3.Complaint Resolved Email
// 4.Complaint Rejected Email


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Async
    public void sendOtp(String email, String otp) {

        sendEmail(
                email,
                "FixMyStreet OTP Verification",
                "Your OTP is : " + otp
        );
    }

    @Async
    public void sendComplaintAssignedEmail(
            String email,
            String complaintTitle,
            String department) {

        String body = """
                Your complaint has been assigned.

                Complaint : %s
                Department : %s

                An officer has been assigned and will review it shortly.
                """
                .formatted(
                        complaintTitle,
                        department);

        sendEmail(
                email,
                "Complaint Assigned",
                body
        );
    }
    @Async
    public void sendComplaintAcceptedEmail(
            String email,
            String complaintTitle) {

        String body = """
                Your complaint has been accepted.

                Complaint : %s

                Work is now in progress.
                """
                .formatted(complaintTitle);

        sendEmail(
                email,
                "Complaint Accepted",
                body
        );
    }
    @Async
    public void sendComplaintResolvedEmail(
            String email,
            String complaintTitle,
            String remarks) {

        String body = """
                Your complaint has been resolved.

                Complaint : %s

                Remarks:
                %s
                """
                .formatted(
                        complaintTitle,
                        remarks);

        sendEmail(
                email,
                "Complaint Resolved",
                body
        );
    }
    @Async
    public void sendComplaintRejectedEmail(
            String email,
            String complaintTitle,
            String reason) {

        String body = """
                Your complaint has been rejected.

                Complaint : %s

                Reason:
                %s
                """
                .formatted(
                        complaintTitle,
                        reason);

        sendEmail(
                email,
                "Complaint Rejected",
                body
        );
    }

    public  void sendEmail(
            String to,
            String subject,
            String body) {

    long start = System.currentTimeMillis();

    System.out.println(
            "Email Thread = "
                    + Thread.currentThread().getName());
        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    System.out.println(
            "Email Time = "
                    + (System.currentTimeMillis() - start));
    }
}