package org.springframework.boot.cloudstorage.services;

import org.springframework.boot.cloudstorage.mapper.CredentialMapper;
import org.springframework.boot.cloudstorage.mapper.UserMapper;
import org.springframework.boot.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserService userService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    public int createCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        return credentialMapper.insert(
                new Credential(
                        null,
                        credential.getUrl(),
                        credential.getUserName(),
                        encodedKey,
                        encryptedPassword,
                        userService.getCurrentUserId()
                )
        );
    }

    public int editCredential(Credential newCredential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(newCredential.getPassword(), encodedKey);

        newCredential.setKey(encodedKey);
        newCredential.setPassword(encryptedPassword);

        return credentialMapper.edit(newCredential);
    }

    public List<Credential> getCredentials() {
        return credentialMapper.getCredentials(userService.getCurrentUserId());
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }

    public String decryptedPassword(String encryptedPassword, String key) {
        return encryptionService.decryptValue(encryptedPassword, key);
    }

}
