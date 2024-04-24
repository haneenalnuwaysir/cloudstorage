package org.springframework.boot.cloudstorage.services;

import org.springframework.boot.cloudstorage.mapper.CredentialMapper;
import org.springframework.boot.cloudstorage.mapper.UserMapper;
import org.springframework.boot.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final UserMapper userMapper;
    private final CredentialMapper credentialMapper;

    public CredentialService(UserMapper userMapper, CredentialMapper credentialMapper) {
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
    }

    public void addCredential(String url, String userName, String credentialUserName, String key, String credentialPassword) {
        Integer id = userMapper.getUsername(userName).getUserId();
        Credential credential = new Credential(0, url, credentialUserName, key, credentialPassword, id);
        credentialMapper.insert(credential);
    }

    public Credential[] getCredentialLists(Integer userId) {
        return credentialMapper.getCredentialLists(userId);
    }

    public Credential getCredential(Integer noteId) {
        return credentialMapper.getCredential(noteId);
    }

    public void deleteCredential(Integer noteId) {
        credentialMapper.deleteCredential(noteId);
    }

    public void editCredential(Integer credentialId, String newUserName, String url, String key, String newPassword) {
        credentialMapper.editCredential(credentialId, newUserName, url, key, newPassword);
    }
}
