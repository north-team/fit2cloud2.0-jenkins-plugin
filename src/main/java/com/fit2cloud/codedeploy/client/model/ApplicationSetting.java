package com.fit2cloud.codedeploy.client.model;

import java.io.Serializable;

public class ApplicationSetting implements Serializable {

    private String id;


    private String repositoryId;


    private String path;


    private String applicationId;


    private String nameRule;


    private Long createdTime;


    private String environmentValueId;

    public String getEnvValue() {
        return envValue;
    }

    public void setEnvValue(String envValue) {
        this.envValue = envValue;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryType() {
        return repositoryType;
    }

    public void setRepositoryType(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    public String getRepositoryAddr() {
        return repositoryAddr;
    }

    public void setRepositoryAddr(String repositoryAddr) {
        this.repositoryAddr = repositoryAddr;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    private String envValue;
    private String repositoryName;
    private String repositoryType;
    private String repositoryAddr;
    private String accessId;
    private String accessPassword;


    private static final long serialVersionUID = 1L;


    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_setting.id
     *
     * @param id the value for devops_application_setting.id
     *
     *
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_setting.repository_id
     *
     * @return the value of devops_application_setting.repository_id
     *
     *
     */
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_setting.repository_id
     *
     * @param repositoryId the value for devops_application_setting.repository_id
     *
     *
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId == null ? null : repositoryId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_setting.path
     *
     * @return the value of devops_application_setting.path
     *
     *
     */
    public String getPath() {
        return path;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_setting.path
     *
     * @param path the value for devops_application_setting.path
     *
     *
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_setting.application_id
     *
     * @return the value of devops_application_setting.application_id
     *
     *
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_setting.application_id
     *
     * @param applicationId the value for devops_application_setting.application_id
     *
     *
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId == null ? null : applicationId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_setting.name_rule
     *
     * @return the value of devops_application_setting.name_rule
     *
     *
     */
    public String getNameRule() {
        return nameRule;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_setting.name_rule
     *
     * @param nameRule the value for devops_application_setting.name_rule
     *
     *
     */
    public void setNameRule(String nameRule) {
        this.nameRule = nameRule == null ? null : nameRule.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_setting.created_time
     *
     * @return the value of devops_application_setting.created_time
     *
     *
     */
    public Long getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_setting.created_time
     *
     * @param createdTime the value for devops_application_setting.created_time
     *
     *
     */
    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_setting.environment_value_id
     *
     * @return the value of devops_application_setting.environment_value_id
     *
     *
     */
    public String getEnvironmentValueId() {
        return environmentValueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_setting.environment_value_id
     *
     * @param environmentValueId the value for devops_application_setting.environment_value_id
     *
     *
     */
    public void setEnvironmentValueId(String environmentValueId) {
        this.environmentValueId = environmentValueId == null ? null : environmentValueId.trim();
    }
}