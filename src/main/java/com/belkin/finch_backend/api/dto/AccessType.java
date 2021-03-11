package com.belkin.finch_backend.api.dto;

public enum AccessType {
    ME_FULL_ACCESS,
    ME_PARTIAL_ACCESS,
    NOT_ME_FULL_ACCESS,
    NOT_ME_PARTIAL_ACCESS;

    public boolean isMe() {
        return this == ME_FULL_ACCESS || this == ME_PARTIAL_ACCESS;
    }

    public boolean isNotMe() {
        return !isMe();
    }
}
