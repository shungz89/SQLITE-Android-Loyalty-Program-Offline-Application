package my.com.durianz;

/**
 * Created by 007 on 08-Mar-18.
 */

public class Users {
    String Name, IC, Mobile, Email, Points;

    Users(String name, String ic, String mobile, String email, String points){
        this.Name = name;
        this.IC = ic;
        this.Mobile = mobile;
        this.Email = email;
        this.Points = points;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIC() {
        return IC;
    }

    public void setIC(String IC) {
        this.IC = IC;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }




}
