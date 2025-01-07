package main.java.khj.model;

public class Member {

    Long id;

    String email;
    String pw;
    public Member(String email, String pw) {
        this.email = email;
        this.pw = pw;
    }

    public Member(){

    }

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPw(){
        return pw;
    }

    public void setId(Long id){
        this.id = id;
    }
}
