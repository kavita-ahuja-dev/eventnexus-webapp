package com.cdac.eventnexus.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.eventnexus.dao.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/__debug")
@RequiredArgsConstructor
class DebugController {
  private final UserRepository userRepo;
  private final PasswordEncoder encoder;

  @GetMapping("/pwd-match")
  public boolean pwdMatch(@RequestParam String email, @RequestParam String raw) {
    var user = userRepo.findByEmail(email).orElseThrow();
    return encoder.matches(raw, user.getPassword());
  }
}
