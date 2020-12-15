package ru.kpekepsalt.ruvik.dto;

public class SessionInitialInformationDto {

    private String publicIdentityKey;
    private String encryptedSessionKey;
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
