package com.fit2cloud.codedeploy2.client.model;



public class ApplicationContainer {

    private String imageTagName;
    private String imageAddress;
    private String imageName;
    private String imageId;
    private String repositoryId;
    //容器名称
    private String name;
    //容器ID
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageTagName() {
        return imageTagName;
    }

    public void setImageTagName(String imageTagName) {
        this.imageTagName = imageTagName;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
