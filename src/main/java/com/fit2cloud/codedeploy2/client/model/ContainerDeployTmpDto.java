package com.fit2cloud.codedeploy2.client.model;

public class ContainerDeployTmpDto {
    private String organizationId;
    private String applicationVersionId;
    private String workFlowJobId;

    public String getWorkFlowJobId() {
        return workFlowJobId;
    }

    public void setWorkFlowJobId(String workFlowJobId) {
        this.workFlowJobId = workFlowJobId;
    }

    public String getApplicationVersionId() {
        return applicationVersionId;
    }

    public void setApplicationVersionId(String applicationVersionId) {
        this.applicationVersionId = applicationVersionId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
