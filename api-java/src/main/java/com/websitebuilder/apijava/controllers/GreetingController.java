package com.websitebuilder.apijava.controllers;

import com.websitebuilder.apijava.dto.Greeting;
import com.websitebuilder.apijava.models.AuthenticationRequest;
import com.websitebuilder.apijava.models.AuthenticationResponse;
import com.websitebuilder.apijava.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class GreetingController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private JwtUtil jwtTokenUtil;

  public final static String template = "Hello %s";
  public final AtomicLong counter = new AtomicLong();

  @GetMapping("/greeting")
  public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(template, name));
  }

  @RequestMapping(value="/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
      );
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password", e);
    }
    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String jwt = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}
