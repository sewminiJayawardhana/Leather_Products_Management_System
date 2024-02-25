package lk.ijse.Shop.controller;

import java.util.Random;
public class Service {
    public static String senMail() {
    // Simulating the generation of OTP (One-Time Password)
    String otp = generateOTP();

    // TODO: Add logic to send the OTP via email

    // For this example, we'll just print the OTP
        System.out.println("OTP: " + otp);

        return otp;
}

    private static String generateOTP() {
        // Generating a random 6-digit OTP for demonstration purposes
        Random random = new Random();
        int otpValue = 10000 + random.nextInt(90000);
        return String.valueOf(otpValue);
    }
}
