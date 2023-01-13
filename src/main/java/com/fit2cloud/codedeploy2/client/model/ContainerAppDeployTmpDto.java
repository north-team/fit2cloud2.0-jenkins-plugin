package com.fit2cloud.codedeploy2.client.model;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class ContainerAppDeployTmpDto {
    private String organizationId;

    //应用版本名称
    private String name;
    //应用ID
    private String applicationId;
    //应用版本描述
    private String description;
    //应用版本镜像
    private List<com.alibaba.fastjson.JSONObject> images;

    private String workFlowJobId;

    private String runtimeEnvId;
    private String clusterId;
    private String namespaceId;
    private boolean autoDeploy;
    private String storageName;
    private String storageType;

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public boolean isAutoDeploy() {
        return autoDeploy;
    }

    public void setAutoDeploy(boolean autoDeploy) {
        this.autoDeploy = autoDeploy;
    }

    public String getRuntimeEnvId() {
        return runtimeEnvId;
    }

    public void setRuntimeEnvId(String runtimeEnvId) {
        this.runtimeEnvId = runtimeEnvId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<JSONObject> getImages() {
        return images;
    }

    public void setImages(List<JSONObject> images) {
        this.images = images;
    }

    public String getWorkFlowJobId() {
        return workFlowJobId;
    }

    public void setWorkFlowJobId(String workFlowJobId) {
        this.workFlowJobId = workFlowJobId;
    }
}
