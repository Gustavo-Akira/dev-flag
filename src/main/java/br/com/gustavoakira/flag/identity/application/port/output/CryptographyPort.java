package br.com.gustavoakira.flag.identity.application.port.output;

public interface CryptographyPort {
    String hash(String stringToHash);
    boolean compare(String toCompare, String original);
}
