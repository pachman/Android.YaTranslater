package com.example.alexander.yatranslater;

public class Motor {

    private int rpm;

    public Motor(){
        this.rpm = 1110;
    }

    public int getRpm(){
        return rpm;
    }

    public void accelerate(int value){
        rpm = rpm + value;
    }

    public void brake(){
        rpm = 0;
    }
}
