import java.util.HashMap;

class User{

    HashMap<String, String> loginInfo = new HashMap<String, String>();

    public void User(){

        loginInfo.put("Hi","JimmyJohn");
        loginInfo.put("Hi12","JimmyJohn23");
        loginInfo.put("Hi1234","JimmyJohn2345");


    }

    protected HashMap getLoginInfo(){
        return loginInfo;
    }
}