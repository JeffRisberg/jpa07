package com.company;

public class ValidateEmail {
  public static boolean isValid(String email) {
    if (email == null) {
      return false;
    }
    return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  }
}
