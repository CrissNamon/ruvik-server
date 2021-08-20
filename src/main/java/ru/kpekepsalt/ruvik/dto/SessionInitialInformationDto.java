package ru.kpekepsalt.ruvik.dto;

import javax.validation.constraints.NotBlank;

public class SessionInitialInformationDto {

    @NotBlank(message = "Public identity key can't be empty")
    private String publicIdentityKey;

    @NotBlank(message = "Session key can't be empty")
    private String encryptedSessionKey;

    @NotBlank(message = "One-time key can't be empty")
    private String oneTImeKey;

    public String getPublicIdentityKey() {
        return publicIdentityKey;
    }

    public void setPublicIdentityKey(String publicIdentityKey) {
        this.publicIdentityKey = publicIdentityKey;
    }

    public String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }

    public void setEncryptedSessionKey(String encryptedSessionKey) {
        this.encryptedSessionKey = encryptedSessionKey;
    }

    public String getOneTImeKey() {
        return oneTImeKey;
    }

    public void setOneTImeKey(String oneTImeKey) {
        this.oneTImeKey = oneTImeKey;
    }
}
