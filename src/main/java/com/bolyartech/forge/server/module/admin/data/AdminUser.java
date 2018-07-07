package com.bolyartech.forge.server.module.admin.data;

import java.util.Objects;


public final class AdminUser {
    private static final int MIN_PASSWORD_LENGTH = 7;

    private final long id;
    private final boolean isDisabled;
    private final boolean isSuperAdmin;
    private final String name;


    public AdminUser(long id, boolean isDisabled, boolean isSuperAdmin, String name) {
        this.id = id;
        this.isDisabled = isDisabled;
        this.isSuperAdmin = isSuperAdmin;
        this.name = name;
    }


    public static boolean isValidName(String screenName) {
        return screenName != null && screenName.matches("^[\\p{L}][\\p{L}\\p{N} ]{1,253}[\\p{L}\\p{N}]$");
    }


    public static boolean isValidPasswordLength(String password) {
        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }

        return password.length() >= MIN_PASSWORD_LENGTH;
    }


    public long getId() {
        return id;
    }


    public boolean isDisabled() {
        return isDisabled;
    }


    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }


    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AdminUser) {
            AdminUser other = (AdminUser) obj;
            return other.getId() == id && other.isDisabled() == isDisabled &&
                    other.isSuperAdmin() == isSuperAdmin && other.getName().equals(name);
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, isDisabled, isSuperAdmin, name);
    }
}
