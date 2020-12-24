package com.example.s1;

public class Trip {
    private String model;
    private String time;
    private String sum;

    public Trip(String model, String time, String sum){
        this.model=model;
        this.time=time;
        this.sum=sum;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSum() {
        return this.sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}
