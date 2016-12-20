package just.yt.controller.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by yt on 2016/12/19.
 */
@XmlRootElement
public class User {

    String name;
    String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public User() {
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
