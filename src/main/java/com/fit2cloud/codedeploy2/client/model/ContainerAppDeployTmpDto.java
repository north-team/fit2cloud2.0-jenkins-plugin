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
