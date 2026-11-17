package dev.portfolio.authguard.common;
public class DuplicateEmailException extends RuntimeException {
 public DuplicateEmailException() { super("E-mail já cadastrado"); }
}
