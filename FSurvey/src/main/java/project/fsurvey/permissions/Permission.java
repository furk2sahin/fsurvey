package project.fsurvey.permissions;

public enum Permission {
    READ_SURVEY("read:survey"),
    READ_PARTICIPANT("read:participant"),
    READ_OPTION("read:option"),
    READ_ISSUE("read:issue"),
    READ_ANSWER("read:answer");

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
