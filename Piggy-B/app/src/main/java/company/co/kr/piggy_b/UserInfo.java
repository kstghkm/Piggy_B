package company.co.kr.piggy_b;

/**
 * Created by user on 2016-06-01.
 */
public class UserInfo {

    String name, phone, username, password, bank, account;
    int coin;

    public UserInfo(String name, String phone, String username, String password, String bank, String account, int coin){
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.bank = bank;
        this.account = account;
        this.coin = coin;
    }

    public UserInfo(String username, String password){
        this.username = username;
        this.password = password;
    }
}