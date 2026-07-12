package br.com.gustavoakira.flag.identity.adapter.output.security;

import br.com.gustavoakira.flag.identity.application.port.output.CryptographyPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptCryptographyAdapter implements CryptographyPort {

    private final BCryptPasswordEncoder passwordEncoder;

    public BcryptCryptographyAdapter(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String stringToHash) {
        return passwordEncoder.encode(stringToHash);
    }

    @Override
    public boolean compare(String toCompare, String original) {
        return passwordEncoder.matches(toCompare,original);
    }
}
