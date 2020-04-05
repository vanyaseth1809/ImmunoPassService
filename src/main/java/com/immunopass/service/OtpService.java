package com.immunopass.service;

import com.immunopass.jwt.JwtRequest;
import com.immunopass.jwt.JwtResponse;
import com.immunopass.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.immunopass.controller.OtpController;

import java.util.Collection;
import java.util.Objects;


@Service
public class OtpService implements OtpController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @Override
    public boolean createOtp(@RequestParam("action") String action,
            @RequestParam("identifier") String identifier,
            @RequestParam("identifier_type") String identifier_type,
            @RequestParam(value = "Otp", required = false) String otp) {
        if (action.equalsIgnoreCase("send")) {
            return sendSMS(identifier, identifier_type);
        } else if (action.equalsIgnoreCase("resend")) {
            return resendSMS(identifier, identifier_type);
        } else {
            return false;
        }
    }

    public boolean sendSMS(String identifier, String identifier_type) {
        System.out.println("Sending SMS to " + identifier);
        return true;
    }

    public boolean resendSMS(String identifier, String identifier_type) {
        System.out.println("Resending SMS to " + identifier);
        return true;
    }

    public ResponseEntity<?> verifyOtp(@RequestParam("identifier") String identifier,
                                       @RequestParam("identifier_type") String identifier_type,
                                       @RequestParam(value = "Otp", required = false) String otp) throws Exception {
        return createAuthenticationToken(otp, identifier);
    }

    public ResponseEntity<?> createAuthenticationToken(String otp, String identifier)
            throws Exception {

        // todo : change this authentication to otp authentication from DB
        authenticate(identifier, otp);
        // todo : using hardcoded values for username "test", see class JwtUserDetailsService
        final String token = jwtTokenUtil.generateToken(jwtInMemoryUserDetailsService.loadUserByUsername(identifier));
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String otp) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(otp);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, otp));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
