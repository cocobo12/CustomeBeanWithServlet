package main.java.khj.test;

import main.java.khj.annotation.CustomAutowired;

public class DoTest {

    @CustomAutowired
    public TestClass testClass;

    public void doTest(){
        testClass.test1();
    }
}
