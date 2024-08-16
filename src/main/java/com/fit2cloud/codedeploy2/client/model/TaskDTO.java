package com.fit2cloud.codedeploy2.client.model;

import com.alibaba.fastjson.JSONArray;

import java.util.List;


public class TaskDTO extends Task {

    private String orchestrateWorkId;
    private String applicationId;
    private String applicationVersionId;
    private String clusterId;
    private String clusterRoleId;
    private String applicationVersion;
    private String applicationName;
    private String applicationVersionName;
    private String distribution;
    List<String> clusterNames;
    private String clusterName;
    List<String> clusterRoleNames;
    private String clusterRoleName;
    private String scriptId;
    private String shellContent;
    private String scriptSource;
    private String scriptVars;
    private String remoteRepoId;
    private String scriptType;
    private String branch;
    private List<Object> server;
    private JSONArray batchServerSettings;
    private JSONArray batchServerKVArray;

    public String getOrchestrateWorkId() {
        return orchestrateWorkId;
    }

    public void setOrchestrateWorkId(String orchestrateWorkId) {
        this.orchestrateWorkId = orchestrateWorkId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationVersionId() {
        return applicationVersionId;
    }

    public void setApplicationVersionId(String applicationVersionId) {
        this.applicationVersionId = applicationVersionId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterRoleId() {
        return clusterRoleId;
    }

    public void setClusterRoleId(String clusterRoleId) {
        this.clusterRoleId = clusterRoleId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersionName() {
        return applicationVersionName;
    }

    public void setApplicationVersionName(String applicationVersionName) {
        this.applicationVersionName = applicationVersionName;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public List<String> getClusterNames() {
        return clusterNames;
    }

    public void setClusterNames(List<String> clusterNames) {
        this.clusterNames = clusterNames;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public List<String> getClusterRoleNames() {
        return clusterRoleNames;
    }

    public void setClusterRoleNames(List<String> clusterRoleNames) {
        this.clusterRoleNames = clusterRoleNames;
    }

    public String getClusterRoleName() {
        return clusterRoleName;
    }

    public void setClusterRoleName(String clusterRoleName) {
        this.clusterRoleName = clusterRoleName;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getShellContent() {
        return shellContent;
    }

    public void setShellContent(String shellContent) {
        this.shellContent = shellContent;
    }

    public String getScriptSource() {
        return scriptSource;
    }

    public void setScriptSource(String scriptSource) {
        this.scriptSource = scriptSource;
    }

    public String getScriptVars() {
        return scriptVars;
    }

    public void setScriptVars(String scriptVars) {
        this.scriptVars = scriptVars;
    }

    public String getRemoteRepoId() {
        return remoteRepoId;
    }

    public void setRemoteRepoId(String remoteRepoId) {
        this.remoteRepoId = remoteRepoId;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<Object> getServer() {
        return server;
    }

    public void setServer(List<Object> server) {
        this.server = server;
    }

    public JSONArray getBatchServerSettings() {
        return batchServerSettings;
    }

    public void setBatchServerSettings(JSONArray batchServerSettings) {
        this.batchServerSettings = batchServerSettings;
    }

    public JSONArray getBatchServerKVArray() {
        return batchServerKVArray;
    }

    public void setBatchServerKVArray(JSONArray batchServerKVArray) {
        this.batchServerKVArray = batchServerKVArray;
    }
}
