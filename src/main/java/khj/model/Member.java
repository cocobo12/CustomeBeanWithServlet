package main.java.khj.model;

public class Member {

    String id;

    String pw;
    public Member(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public Member(){

    }

    public String getId(){
        return id;
    }

    public String getPw(){
        return pw;
    }


}
