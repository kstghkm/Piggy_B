package company.co.kr.piggy_b;

/**
 * Created by user on 2016-06-01.
 */
public class Contact {

    String name, phone, username, password;

    public Contact(String name, String phone, String username, String password){
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    public Contact(String username, String password){
        this.username = username;
        this.password = password;
    }
}
