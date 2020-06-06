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
}