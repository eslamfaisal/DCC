package com.ibnsaad.thedcc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProfileResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("knownAs")
    @Expose
    private String knownAs;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("lastActive")
    @Expose
    private String lastActive;
    @SerializedName("introducation")
    @Expose
    private String introducation;
    @SerializedName("lookingFor")
    @Expose
    private String lookingFor;
    @SerializedName("interests")
    @Expose
    private String interests;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;
    @SerializedName("likerCount")
    @Expose
    private int likerCount;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getKnownAs() {
        return knownAs;
    }

    public void setKnownAs(String knownAs) {
        this.knownAs = knownAs;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public String getIntroducation() {
        return introducation;
    }

    public void setIntroducation(String introducation) {
        this.introducation = introducation;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getLikerCount() {
        return likerCount;
    }

    public void setLikerCount(Integer likerCount) {
        this.likerCount = likerCount;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "ProfileResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", knownAs='" + knownAs + '\'' +
                ", created='" + created + '\'' +
                ", lastActive='" + lastActive + '\'' +
                ", introducation='" + introducation + '\'' +
                ", lookingFor='" + lookingFor + '\'' +
                ", interests='" + interests + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", userType='" + userType + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", likerCount=" + likerCount +
                ", specialization='" + specialization + '\'' +
                ", photos=" + photos +
                '}';
    }
}