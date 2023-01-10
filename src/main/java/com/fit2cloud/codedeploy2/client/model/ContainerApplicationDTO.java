package com.fit2cloud.codedeploy2.client.model;


public class ContainerApplicationDTO extends Application {

    private String deployInfo;

    public String getDeployInfo() {
        return deployInfo;
    }

    public void setDeployInfo(String deployInfo) {
        this.deployInfo = deployInfo;
    }
}
