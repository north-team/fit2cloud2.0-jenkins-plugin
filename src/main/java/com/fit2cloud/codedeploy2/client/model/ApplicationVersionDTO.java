package com.fit2cloud.codedeploy2.client.model;


public class ApplicationVersionDTO extends ApplicationVersion {
    private String applicationName;
    private String environmentValueId;
    private String deployType;
    private String resourcePath;
    private String fileMd5;

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    private String appId;

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getDeployType() {
        return deployType;
    }

    public void setDeployType(String deployType) {
        this.deployType = deployType;
    }

    public String getEnvironmentValueId() {
        return environmentValueId;
    }

    public void setEnvironmentValueId(String environmentValueId) {
        this.environmentValueId = environmentValueId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
