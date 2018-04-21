package project.iti.Data.Model;

import android.graphics.Bitmap;

import org.parceler.Parcel;

/**
 * Created by asmaa on 03/03/2018.
 */

@Parcel
public class User {

    private String name;
    private String email;
    private String img;
    private Bitmap img2;
    private String password;
    private String fbToken;
    private String id;
    private String fbID;
    private String phone;

    public User(){

    }

    public User(Bitmap img2){
        this.img2=img2;
    }
    public User(String id , String name , String email, String password, String fbToken , String fbID){
        this.id=id;
        this.name=name;
        this.email=email;
        this.password =password;
        this.fbToken=fbToken;
        this.fbID=fbID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getImg2() {
        return img2;
    }

    public void setImg2(Bitmap img2) {
        this.img2 = img2;
    }
}
