package com.fit2cloud.codedeploy2.client.model;

public class ContainerDeployDto {
    private String applicationVersionId;
    private String namespaceId;
    private String secret;
    private String containerClusterId;
    private String podsId;
    private String portType;
    private boolean headless;
    private Integer nodePort;
    private Integer containerPort;

    public String getApplicationVersionId() {
        return applicationVersionId;
    }

    public void setApplicationVersionId(String applicationVersionId) {
        this.applicationVersionId = applicationVersionId;
    }

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getContainerClusterId() {
        return containerClusterId;
    }

    public void setContainerClusterId(String containerClusterId) {
        this.containerClusterId = containerClusterId;
    }

    public String getPodsId() {
        return podsId;
    }

    public void setPodsId(String podsId) {
        this.podsId = podsId;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public Integer getNodePort() {
        return nodePort;
    }

    public void setNodePort(Integer nodePort) {
        this.nodePort = nodePort;
    }

    public Integer getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(Integer containerPort) {
        this.containerPort = containerPort;
    }
}
