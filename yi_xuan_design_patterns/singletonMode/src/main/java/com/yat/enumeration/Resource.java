package com.yat.enumeration;

/**
 * <p>Description: 要应用单例模式的资源类</p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/24 - 16:13
 */
public class Resource {

    private String user;
    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
