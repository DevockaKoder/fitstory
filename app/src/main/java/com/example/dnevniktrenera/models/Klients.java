package com.example.dnevniktrenera.models;

public class Klients {
    private String name, phone, age, weight, growth, kid, date, time, imageOne, imageTwo, imageThree;

    public Klients() {}

    public Klients(String name, String phone, String age, String weight, String growth, String kid, String date, String time, String imageOne, String imageTwo, String imageThree) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.weight = weight;
        this.growth = growth;
        this.kid = kid;
        this.date = date;
        this.time = time;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getWeight() { return weight; }

    public void setWeight(String weight) { this.weight = weight; }

    public String getGrowth() { return growth; }

    public void setGrowth(String growth) { this.growth = growth; }

    public String getKid() { return kid; }

    public void setKid(String kid) { this.kid = kid; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getImageOne() { return imageOne; }

    public void setImageOne(String imageOne) { this.imageOne = imageOne; }

    public String getImageTwo() { return imageTwo; }

    public void setImageTwo(String imageTwo) { this.imageTwo = imageTwo; }

    public String getImageThree() { return imageThree; }

    public void setImageThree(String imageThree) { this.imageThree = imageThree; }
}
