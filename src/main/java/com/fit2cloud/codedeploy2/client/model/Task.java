package com.fit2cloud.codedeploy2.client.model;


import lombok.Data;

import java.io.Serializable;
@Data
public class Task implements Serializable {

    private Long id;

    private String name;

    private String type;

    private String executeType;

    private String taskType;

    private String hostAllocationType;

    private String status;

    private String description;

    private Long timeout;

    private String failStrategy;

    private String timeoutStrategy;

    private Long createTime;

    private Long updateTime;

    private String runtimeEnvId;

    private String organizationId;

    private String scope;

    private String workspaceId;

    private String createType;

    private String taskTypeId;

    private String data;

}