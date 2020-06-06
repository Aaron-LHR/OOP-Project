package Client;

public class Flag {
    private static Flag flag = new Flag();
    private Flag() {};
    public static Flag getInstance() {
        return flag;
    }
    boolean modify;
    int login;
    int register;
    int logout;
    int checkOnline;
    int sendPrivateMessage;
    int createGroup;
    int activateGroup;
    int sendGroupMessage;
    int onlineListFlag;
    String[] onlineList;
    String curToUsername;
    String privateContent;

    public boolean isModify() {
        return modify;
    }

    public int getLogin() {
        return login;
    }

    public int getRegister() {
        return register;
    }

    public int getLogout() {
        return logout;
    }

    public int getCheckOnline() {
        return checkOnline;
    }

    public int getSendPrivateMessage() {
        return sendPrivateMessage;
    }

    public int getCreateGroup() {
        return createGroup;
    }

    public int getActivateGroup() {
        return activateGroup;
    }

    public int getSendGroupMessage() {
        return sendGroupMessage;
    }

    public int getOnlineListFlag() {
        return onlineListFlag;
    }

    public String[] getOnlineList() {
        return onlineList;
    }

    public String getCurToUsername() {
        return curToUsername;
    }

    public String getPrivateContent() {
        return privateContent;
    }
}