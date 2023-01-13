package com.fit2cloud.codedeploy2;

import com.alibaba.fastjson.JSON;
import com.fit2cloud.codedeploy2.client.Fit2cloudClient;
import com.fit2cloud.codedeploy2.client.model.*;
import com.fit2cloud.codedeploy2.oss.AWSS3Client;
import com.fit2cloud.codedeploy2.oss.AliyunOSSClient;
import com.fit2cloud.codedeploy2.oss.ArtifactoryUploader;
import com.fit2cloud.codedeploy2.oss.NexusUploader;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.util.DirScanner;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class F2CCodeDeployPublisher extends Publisher {
    private static final String LOG_PREFIX = "[FIT2CLOUD 代码部署]";
    private final String f2cEndpoint;
    private final String f2cAccessKey;
    private final String f2cSecretKey;
    private final String workspaceId;
    private final String applicationId;
    private final String containerApplicationId;
    private final String applicationSettingId;
    private final String applicationRepositoryId;
    private final String clusterId;
    private final String clusterRoleId;
    private final String cloudServerId;
    private final String deployPolicy;
    private final String applicationVersionName;
    private final boolean autoDeploy;
    private final String includes;
    private final String excludes;
    private final String appspecFilePath;
    private final String description;
    private final boolean waitForCompletion;
    private final Long pollingTimeoutSec;
    private final Long pollingFreqSec;
    private final String nexusGroupId;
    private final String nexusArtifactId;
    private final String nexusArtifactVersion;
    private final boolean nexusChecked;
    private final boolean ossChecked;
    private final boolean s3Checked;
    private final boolean artifactoryChecked;
    private final String failStrategy;
    private final String executeType;


    private final String path;
    //上传到阿里云参数
    private final String objectPrefixAliyun;
    //上传到亚马逊参数
    private final String objectPrefixAWS;
    private final String repositorySettingId;
    private final String artifactType;
    private final String deployType;
    private final boolean containerChecked;
    private final boolean otherChecked;
    private final boolean containerAppChecked;
    private final boolean containerAppAutoDeploy;

    private final boolean customZip;
    private final boolean noCustomZip;
    private final String zipFilePath;
    private final String imageUrl;
    private final Integer containerPort;
    private final String deployNamespaceId;
    private final String deployClusterId;
    private final String deployDeploymentsId;
    private final String portType;
    private final boolean clusterIpChecked;
    private final boolean nodePortChecked;
    private final Integer nodePort;
    private final boolean headless;
    private final boolean privateImage;
    private final String secret;

    private PrintStream logger;

    private final String containerAppId;
    private final String containerAppVersion;
    private final String containerAppVersionDesc;
    private final String applicationImage;
    private final String applicationImageTag;

    private final String containerAppRuntimeEnvId;
    private String containerAppClusterId;
    private String containerAppNamespaceId;
    private String containerAppPv;
    private String containerAppStorageClass;
    private String containerAppStorageType;

    private boolean checkContainerAppStoragePV;
    private boolean checkContainerAppStorageClass;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public F2CCodeDeployPublisher(String f2cEndpoint,
                                  String f2cAccessKey,
                                  String f2cSecretKey,
                                  String applicationId,
                                  String containerApplicationId,
                                  String applicationRepositoryId,
                                  String clusterId,
                                  String clusterRoleId,
                                  String workspaceId,
                                  String applicationSettingId,
                                  String cloudServerId,
                                  String deployPolicy,
                                  String applicationVersionName,
                                  boolean waitForCompletion,
                                  boolean nexusChecked,
                                  boolean ossChecked,
                                  boolean s3Checked,
                                  boolean artifactoryChecked,
                                  boolean autoDeploy,
                                  Long pollingTimeoutSec,
                                  Long pollingFreqSec,
                                  String includes,
                                  String excludes,
                                  String appspecFilePath,
                                  String description,
                                  String artifactType,
                                  String deployType,
                                  boolean containerChecked,
                                  boolean otherChecked,
                                  String repositorySettingId,
                                  String objectPrefixAliyun,
                                  String objectPrefixAWS,
                                  String path,
                                  String nexusGroupId,
                                  String nexusArtifactId,
                                  String failStrategy,
                                  String executeType,
                                  String nexusArtifactVersion,
                                  Boolean customZip,
                                  String zipFilePath,
                                  String imageUrl,
                                  Integer containerPort,
                                  String deployNamespaceId,
                                  String deployClusterId,
                                  String deployDeploymentsId,
                                  String portType,
                                  boolean clusterIpChecked,
                                  boolean nodePortChecked,
                                  Integer nodePort,
                                  boolean headless,
                                  boolean privateImage,
                                  String secret,
                                  String containerAppId,
                                  String containerAppVersion,
                                  String containerAppVersionDesc,
                                  String applicationImageTag,
                                  String applicationImage,
                                  boolean containerAppAutoDeploy,
                                  String containerAppRuntimeEnvId,
                                  boolean checkContainerAppStoragePV,
                                  boolean checkContainerAppStorageClass,
                                  String containerAppStorageType) {
        this.f2cEndpoint = f2cEndpoint;
        this.f2cAccessKey = f2cAccessKey;
        this.artifactType = StringUtils.isBlank(artifactType) ? ArtifactType.NEXUS : artifactType;
        this.deployType = StringUtils.isBlank(deployType) ? CommonConstants.CONTAINER : deployType;
        this.repositorySettingId = repositorySettingId;
        this.f2cSecretKey = f2cSecretKey;
        this.applicationId = applicationId;
        this.containerApplicationId = containerApplicationId;
        this.clusterId = clusterId;
        this.clusterRoleId = clusterRoleId;
        this.workspaceId = workspaceId;
        this.applicationSettingId = applicationSettingId;
        this.cloudServerId = cloudServerId;
        this.applicationRepositoryId = applicationRepositoryId;
        this.applicationVersionName = applicationVersionName;
        this.deployPolicy = deployPolicy;
        this.autoDeploy = autoDeploy;
        this.includes = includes;
        this.excludes = excludes;
        this.appspecFilePath = StringUtils.isBlank(appspecFilePath) ? "appspec.yml" : appspecFilePath;
        this.description = description;
        this.pollingFreqSec = pollingFreqSec;
        this.pollingTimeoutSec = pollingTimeoutSec;
        this.waitForCompletion = waitForCompletion;
        this.objectPrefixAliyun = objectPrefixAliyun;
        this.objectPrefixAWS = objectPrefixAWS;
        this.path = path;
        this.nexusGroupId = nexusGroupId;
        this.nexusArtifactId = nexusArtifactId;
        this.nexusArtifactVersion = nexusArtifactVersion;
        this.failStrategy = failStrategy;
        this.executeType = executeType;
        this.nexusChecked = StringUtils.equals(artifactType, ArtifactType.NEXUS);
        this.artifactoryChecked = StringUtils.equals(artifactType, ArtifactType.ARTIFACTORY);
        this.ossChecked = StringUtils.equals(artifactType, ArtifactType.OSS);
        this.s3Checked = StringUtils.equals(artifactType, ArtifactType.S3);
        this.containerChecked = StringUtils.equals(deployType, CommonConstants.CONTAINER);
        this.otherChecked = StringUtils.equals(deployType, CommonConstants.OTHER);
        this.containerAppChecked = StringUtils.equals(deployType, CommonConstants.CONTAINER_APP);
        this.customZip = customZip != null && customZip;
        this.noCustomZip = customZip != null && !customZip;
        this.zipFilePath = zipFilePath;
        this.imageUrl = imageUrl;
        this.containerPort = containerPort;
        this.deployNamespaceId = deployNamespaceId;
        this.deployClusterId = deployClusterId;
        this.deployDeploymentsId = deployDeploymentsId;
        this.portType = portType;
        this.clusterIpChecked = StringUtils.equals(portType, CommonConstants.CLUSTER_IP);
        this.nodePortChecked = StringUtils.equals(portType, CommonConstants.NODE_PORT);
        this.nodePort = nodePort;
        this.headless = headless;
        this.privateImage = privateImage;
        this.secret = secret;
        this.containerAppId = containerAppId;
        this.containerAppVersion = containerAppVersion;
        this.containerAppVersionDesc = containerAppVersionDesc;
        this.applicationImage = applicationImage;
        this.applicationImageTag = applicationImageTag;
        this.containerAppAutoDeploy = containerAppAutoDeploy;
        this.containerAppRuntimeEnvId = containerAppRuntimeEnvId;
        this.checkContainerAppStoragePV = StringUtils.equals(containerAppStorageType, CommonConstants.CONTAINER_PV);
        this.checkContainerAppStorageClass = StringUtils.equals(containerAppStorageType, CommonConstants.CONTAINER_STORAGE_CLASS);
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        this.logger = listener.getLogger();
        int builtNumber = build.getNumber();
        String projectName = build.getProject().getName();

        final boolean buildFailed = build.getResult() == Result.FAILURE;
        if (buildFailed) {
            log("Skipping CodeDeploy publisher as build failed");
            return true;
        }
        final Fit2cloudClient fit2cloudClient = new Fit2cloudClient(this.f2cAccessKey, this.f2cSecretKey, this.f2cEndpoint);

        log("开始校验参数...");
        //容器应用部署
        if (StringUtils.equals(deployType, CommonConstants.CONTAINER)) {
            try {
                ContainerDeployTmpDto dto = new ContainerDeployTmpDto();
                //1 检查应用部署参数
                this.checkContainerDeployParams(fit2cloudClient, dto);
                //2 根据应用和镜像的标签，检查应用版本是否存在。不存在则新建应用版本。
                this.saveOrUpdateContainerApplicationVersion(fit2cloudClient, dto);
                //3 根据应用版本创建容器应用任务。
                this.createContainerTaskAndRun(fit2cloudClient, dto);
                //4 检测任务是否执行成功
                this.checkTaskStatus(fit2cloudClient, dto);

                return true;
            } catch (Exception e) {
                log(e.getMessage());
                return false;
            }
        }
        //容器应用版本部署
        if (StringUtils.equals(deployType, CommonConstants.CONTAINER_APP)) {
            try {
                ContainerAppDeployTmpDto dto = new ContainerAppDeployTmpDto();
                //1 检查应用部署参数
                this.checkContainerAppDeployParams(fit2cloudClient, dto, build, listener);
                //2 创建应用版本。
                this.createContainerAppTaskAndRun(fit2cloudClient, dto);
                return true;
            } catch (Exception e) {
                log(e.getMessage());
                return false;
            }
        }
        try {
            boolean findWorkspace = false;
            List<Workspace> workspaces = fit2cloudClient.getWorkspace();
            for (Workspace workspace : workspaces) {
                if (workspace.getId().equals(this.workspaceId)) {
                    findWorkspace = true;
                }
            }
            if (!findWorkspace) {
                throw new CodeDeployException("工作空间不存在！");
            }

            boolean findApplication = false;
            List<ApplicationDTO> applications = fit2cloudClient.getApplications(this.workspaceId, CommonConstants.OTHER);
            for (ApplicationDTO applicationDTO : applications) {
                if (applicationDTO.getId().equals(this.applicationId)) {
                    findApplication = true;
                }
            }
            if (!findApplication) {
                throw new CodeDeployException("应用不存在！");
            }


            if (autoDeploy) {
                boolean findCluster = false;
                List<ClusterDTO> clusters = fit2cloudClient.getClusters(this.workspaceId);
                for (ClusterDTO clusterDTO : clusters) {
                    if (clusterDTO.getId().equals(this.clusterId)) {
                        findCluster = true;
                    }
                }
                if (!findCluster) {
                    throw new CodeDeployException("集群不存在! ");
                }


                List<ClusterRole> clusterRoles = fit2cloudClient.getClusterRoles(this.workspaceId, this.clusterId);

                if (clusterRoles.size() == 0) {
                    throw new CodeDeployException("此集群下主机组为空！");
                }

                if (!clusterRoleId.equalsIgnoreCase("ALL")) {
                    boolean findClusterRole = false;
                    for (ClusterRole clusterRole : clusterRoles) {
                        if (clusterRole.getId().equals(this.clusterRoleId)) {
                            findClusterRole = true;
                        }
                    }
                    if (!findClusterRole) {
                        throw new CodeDeployException("主机组不存在! ");
                    }
                }

                List<CloudServer> cloudServers = fit2cloudClient.getCloudServers(this.workspaceId, this.clusterRoleId, this.clusterId);
                if (cloudServers.size() == 0) {
                    throw new CodeDeployException("此主机组下主机为空！");
                }
                if (!cloudServerId.equalsIgnoreCase("ALL")) {
                    boolean findCLoudServer = false;
                    for (CloudServer cloudServer : cloudServers) {
                        if (cloudServer.getId().equals(this.cloudServerId)) {
                            findCLoudServer = true;
                        }
                    }
                    if (!findCLoudServer) {
                        throw new CodeDeployException("主机组不存在! ");
                    }
                }

            }
        } catch (Exception e) {
            log(e.getMessage());
            return false;
        }


        // 查询仓库
        ApplicationRepository applicationRepository = null;
        ApplicationRepositorySetting repSetting = null;
//        ApplicationRepository rep = null;
        try {
            ApplicationDTO app = null;
            List<ApplicationDTO> applicationDTOS = fit2cloudClient.getApplications(workspaceId, CommonConstants.OTHER);
            for (ApplicationDTO applicationDTO : applicationDTOS) {
                if (applicationDTO.getId().equals(this.applicationId)) {
                    app = applicationDTO;
                }
            }
//            if (app != null) {
////                for (ApplicationRepositorySetting setting : app.getApplicationRepositorySettings()) {
////                    if (setting.getId().equals(repositorySettingId)) {
//                        repSetting = app.getApplicationRepositorySetting();
////                    }
////                }
//            }
//            if (repSetting != null) {
            List<ApplicationRepository> repositories = fit2cloudClient.getApplicationRepositorys(workspaceId);
            for (ApplicationRepository re : repositories) {
                if (app.getApplicationRepositoryId().equals(re.getId())) {
                    applicationRepository = re;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log("加载仓库失败！" + e.getMessage());
            return false;
        }


        FilePath workspace = build.getWorkspace();
        File zipFile = null;
        String zipFileName = null;
        String newAddress = null;

        String fileMd5 = "";

        try {
            zipFileName = projectName + "-" + builtNumber + ".zip";
            String includesNew = Utils.replaceTokens(build, listener, this.includes);
            String excludesNew = Utils.replaceTokens(build, listener, this.excludes);
            String appspecFilePathNew = Utils.replaceTokens(build, listener, this.appspecFilePath);
            String zipFilePathNew = Utils.replaceTokens(build, listener, this.zipFilePath);
            if (this.isCustomZip()) {
                if (StringUtils.isBlank(zipFilePathNew)) {
                    log("zip文件路径不能为空！");
                }

                File tmpFile = new File("/tmp/" + UUID.randomUUID());
                tmpFile.createNewFile();
                FilePath fp = new FilePath(workspace, zipFilePathNew);
                fp.copyTo(new FileOutputStream(tmpFile));
                zipFile = tmpFile;
            } else {
                zipFile = zipFile(zipFileName, workspace, includesNew, excludesNew, appspecFilePathNew);
            }
            log("zipFileName: " + zipFileName);

            log("----->workspace" + workspace.toString());

            log("开始计算文件文件MD5值");
            fileMd5 = DigestUtils.md5Hex(new FileInputStream(zipFile));
            log("fileMd5: " + fileMd5);

            switch (artifactType) {
                case ArtifactType.OSS:
                    log("开始上传zip文件到OSS服务器");
                    //getBucketLocation
                    String expFP = Utils.replaceTokens(build, listener, zipFile.toString());

                    if (expFP != null) {
                        expFP = expFP.trim();
                    }

                    // Resolve virtual path
                    String expVP = Utils.replaceTokens(build, listener, objectPrefixAliyun);
                    if (Utils.isNullOrEmpty(expVP)) {
                        expVP = null;
                    }
                    if (!Utils.isNullOrEmpty(expVP) && !expVP.endsWith(Utils.FWD_SLASH)) {
                        expVP = expVP.trim() + Utils.FWD_SLASH;
                    }
                    try {
                        int filesUploaded = AliyunOSSClient.upload(build, listener,
                                applicationRepository.getAccessId(),
                                applicationRepository.getAccessPassword(),
                                ".aliyuncs.com",
                                applicationRepository.getRepository().replace("bucket:", ""), expFP, expVP, zipFile);
                        if (filesUploaded > 0) {
                            log("上传Artifacts到阿里云OSS成功!");
                        }
                    } catch (Exception e) {
                        log("上传Artifact到阿里云OSS失败，错误消息如下:");
                        log(e.getMessage());
                        e.printStackTrace(this.logger);
                        return false;
                    }
                    log("上传zip文件到oss服务器成功!");
                    if (expVP == null) {
                        newAddress = zipFile.getName();
                    } else {
                        newAddress = objectPrefixAliyun + "/" + zipFile.getName();
                    }
                    log("文件路径" + newAddress);
                    break;
                case ArtifactType.ARTIFACTORY:
                    log("开始上传zip文件到Artifactory服务器");
                    if (StringUtils.isBlank(path)) {
                        log("请输入上传至 Artifactory 的 Path");
                        return false;
                    }
                    String pathNew = Utils.replaceTokens(build, listener, path);
                    try {

                        String r = applicationRepository.getRepository();
                        String server = r.substring(0, r.indexOf("/artifactory"));
                        newAddress = ArtifactoryUploader.uploadArtifactory(zipFile, server.trim(),
                                applicationRepository.getAccessId(), applicationRepository.getAccessPassword(), r, pathNew);
                    } catch (Exception e) {
                        log("上传文件到 Artifactory 服务器失败！错误消息如下:");
                        log(e.getMessage());
                        e.printStackTrace(this.logger);
                        return false;
                    }
                    log("上传zip文件到Artifactory服务器成功!");
                    break;
                case ArtifactType.NEXUS:
                    if (StringUtils.isBlank(nexusArtifactId) || StringUtils.isBlank(nexusGroupId) || StringUtils.isBlank(nexusArtifactVersion)) {
                        log("请输入上传至 Nexus 的 GroupId、 ArtifactId 和 NexusArtifactVersion");
                        return false;
                    }
                    String nexusGroupIdNew = Utils.replaceTokens(build, listener, nexusGroupId);
                    String nexusArtifactIdNew = Utils.replaceTokens(build, listener, nexusArtifactId);
                    String nexusArtifactVersionNew = Utils.replaceTokens(build, listener, nexusArtifactVersion);

                    log("开始上传zip文件到nexus服务器");
                    try {
                        newAddress = NexusUploader.upload(zipFile, applicationRepository.getAccessId(), applicationRepository.getAccessPassword(), applicationRepository.getRepository(),
                                nexusGroupIdNew, nexusArtifactIdNew, String.valueOf(builtNumber), "zip", nexusArtifactVersionNew);
                        log("上传zip包" + zipFile.getName());
                        log(newAddress);
                    } catch (Exception e) {
                        log("上传文件到 Nexus 服务器失败！错误消息如下:");
                        log(e.getMessage());
                        e.printStackTrace(this.logger);
                        return false;
                    }
                    log("上传zip文件到nexus服务器成功!");

                    break;
                case ArtifactType.S3:
                    log("开始上传zip文件到AWS服务器");
                    //getBucketLocation
                    String expFPAws = Utils.replaceTokens(build, listener, zipFile.toString());

                    if (expFPAws != null) {
                        expFPAws = expFPAws.trim();
                    }

                    // Resolve virtual path
                    String expVPAws = Utils.replaceTokens(build, listener, objectPrefixAWS);
                    if (Utils.isNullOrEmpty(expVPAws)) {
                        expVPAws = null;
                    }
                    if (!Utils.isNullOrEmpty(expVPAws) && !expVPAws.endsWith(Utils.FWD_SLASH)) {
                        expVPAws = expVPAws.trim() + Utils.FWD_SLASH;
                    }
                    try {
                        AWSS3Client.upload(build, listener,
                                applicationRepository.getAccessId(),
                                applicationRepository.getAccessPassword(),
                                null,
                                applicationRepository.getRepository(), expFPAws, expVPAws, zipFile);
                        log("上传Artifacts到亚马逊AWS成功!");
                    } catch (Exception e) {
                        log("上传Artifact到亚马逊AWS失败，错误消息如下:");
                        log(e.getMessage());
                        e.printStackTrace(this.logger);
                        return false;
                    }
                    log("上传zip文件到亚马逊AWS服务器成功!");
                    if (expVPAws == null) {
                        newAddress = zipFile.getName();
                    } else {
                        newAddress = objectPrefixAWS + "/" + zipFile.getName();
                    }
                    log("文件路径:" + newAddress);
                    break;
                default:
                    log("暂时不支持 " + artifactType + " 类型制品库");
                    return false;
            }

            if (this.isCustomZip()) {
                zipFile.delete();
            }
        } catch (Exception e) {
            log("生成ZIP包失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (zipFile != null && zipFile.exists()) {
                try {
                    log("删除 Zip 文件 " + zipFile.getAbsolutePath());
                    zipFile.delete();
                } catch (Exception e) {
                }
            }
        }


        ApplicationVersion appVersion = null;
//        try {
        log("注册应用版本中...");
        String newAppVersion = null;
        try {
            newAppVersion = Utils.replaceTokens(build, listener, this.applicationVersionName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ApplicationVersionDTO applicationVersion = new ApplicationVersionDTO();
        applicationVersion.setAppId(this.applicationId);
        applicationVersion.setName(newAppVersion);
//            assert repSetting != null;
//            applicationVersion.setEnvironmentValueId(repSetting.getEnvId());
        applicationVersion.setApplicationRepositoryId(applicationRepository.getId());
        applicationVersion.setResourcePath(newAddress);
        applicationVersion.setDeployType("add");
        try {
            String zipName = newAddress.split("/")[newAddress.split("/").length - 1];
            String fileName = workspace.toString() + "/target/" + zipName.replaceAll("-" + zipName.split("-")[zipName.split("-").length - 1], ".zip");
            log("文件名称: " + fileName);
            log("zip 文件名称: " + zipFile.getAbsolutePath());

            // applicationVersion.setFileMd5(DigestUtils.md5Hex(new FileInputStream(new File(workspace.toString() + "/target/" + zipName.replaceAll("-"+zipName.split("-")[zipName.split("-").length-1],".zip")))));
            // applicationVersion.setFileMd5(DigestUtils.md5Hex(new FileInputStream(new File(workspace.toString() + "/target/" + zipName.replaceAll("-"+zipName.split("-")[zipName.split("-").length-1],".zip")))));
            applicationVersion.setFileMd5(fileMd5);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        File f = new File(workspace.toString() + newAddress.split("/")[newAddress.split("/").length-1]);
//        if(f == null){
//            log("f为空");
//        }else{
//            log("f不为空");
//            log(f.getName());
//            log(f.getPath());
//        }
        log("应用id: " + applicationVersion.getAppId());
        log("版本名称: " + applicationVersion.getName());
        log("url: " + applicationVersion.getResourcePath());
        log("zip: " + zipFile);
        log("MD5: " + applicationVersion.getFileMd5());
        appVersion = fit2cloudClient.createApplicationVersion(applicationVersion, this.workspaceId);
//        } catch (Exception e) {
//            log("版本注册失败！ 原因：" + e.getMessage());
//            return false;
//        }
        log("注册版本成功！");

        ApplicationDeployment applicationDeploy = null;
        try {
            if (this.autoDeploy) {
                log("创建代码部署任务...");
                ApplicationDeployment applicationDeployment = new ApplicationDeployment();
                applicationDeployment.setClusterId(this.clusterId);
                applicationDeployment.setClusterRoleId(this.clusterRoleId);
                applicationDeployment.setCloudServerId(this.cloudServerId);
                applicationDeployment.setApplicationVersionId(appVersion.getId());
                applicationDeployment.setExecuteType(this.executeType);
                applicationDeployment.setFailStrategy(this.failStrategy);
//                applicationDeployment.setPolicy(this.deployPolicy);
                applicationDeployment.setDescription("Jenkins 触发");
                String result = fit2cloudClient.deployAppVersion(applicationDeployment, this.workspaceId);
//                applicationDeploy = fit2cloudClient.createApplicationDeployment(applicationDeployment, this.workspaceId);
                if (!StringUtils.equalsIgnoreCase("success", result)) {
                    log("创建代码部署任务失败" + result);
                    return false;
                }
                log("代码部署任务创建成功，请到devops平台查看任务执行结果");
            }
        } catch (Exception e) {
            log("创建代码部署任务异常: " + e.getMessage());
            return false;
        }

//        try {
//            int i = 0;
//            if (this.autoDeploy && this.waitForCompletion) {
//                log("执行代码部署...");
//                while (true) {
//                    Thread.sleep(1000 * pollingFreqSec);
//                    ApplicationDeployment applicationDeployment = fit2cloudClient.getApplicationDeployment(applicationDeploy.getId());
//                    if (applicationDeployment.getStatus().equalsIgnoreCase("success")
//                            || applicationDeployment.getStatus().equalsIgnoreCase("fail")) {
//                        log("部署完成！");
//                        if (applicationDeployment.getStatus().equalsIgnoreCase("success")) {
//                            log("部署结果: 成功");
//                        } else {
//                            throw new Exception("部署任务执行失败，具体结果请登录FIT2CLOUD控制台查看！");
//                        }
//                        break;
//                    } else {
//                        log("部署任务运行中...");
//                    }
//                }
//                if (pollingFreqSec * ++i > pollingTimeoutSec) {
//                    throw new Exception("部署超时,请查看FIT2CLOUD控制台！");
//                }
//            }
//        } catch (Exception e) {
//            log("执行代码部署失败: " + e.getMessage());
//            return false;
//        }
//

        return true;
    }

    private void checkTaskStatus(Fit2cloudClient fit2cloudClient, ContainerDeployTmpDto dto) {
        String status;
        while (true) {
            status = fit2cloudClient.selectWorkJobStatus(dto.getWorkFlowJobId());
            if (StringUtils.equalsAnyIgnoreCase(status, CommonConstants.SUCCESS, CommonConstants.FAILED, CommonConstants.OVERTIME)) {
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.equalsIgnoreCase(CommonConstants.FAILED, status)) {
            throw new RuntimeException("部署失败，详情请登录云管，查看错误日志");
        }
        if (StringUtils.equalsIgnoreCase(CommonConstants.OVERTIME, status)) {
            throw new RuntimeException("部署超时，详情请登录云管，查看超时日志");
        }
    }

    private void createContainerTaskAndRun(Fit2cloudClient fit2cloudClient, ContainerDeployTmpDto dto) {
        log("开始部署应用版本");
        ContainerDeployDto deployDto = new ContainerDeployDto();
        deployDto.setApplicationVersionId(dto.getApplicationVersionId());
        deployDto.setContainerClusterId(this.deployClusterId);
        deployDto.setPodsId(this.deployDeploymentsId);
        deployDto.setPortType(this.portType);
        deployDto.setNamespaceId(this.deployNamespaceId);
        deployDto.setSecret(this.secret);
        deployDto.setNodePort(this.nodePort);
        deployDto.setHeadless(this.headless);
        deployDto.setContainerPort(this.containerPort);

        String workFlowId = fit2cloudClient.createContainerTaskAndRun(this.workspaceId, deployDto);
        dto.setWorkFlowJobId(workFlowId);
    }

    private void createContainerAppTaskAndRun(Fit2cloudClient fit2cloudClient, ContainerAppDeployTmpDto dto) {
        log("开始部署集群容器应用版本: " + JSON.toJSONString(dto));
        log("工作空间ID: " + JSON.toJSONString(this.workspaceId));
        String workFlowId = fit2cloudClient.createContainerAppTaskAndRun(this.workspaceId, dto);
        dto.setWorkFlowJobId(workFlowId);
        log("部署集群容器应用版本结束");
    }

    private void saveOrUpdateContainerApplicationVersion(Fit2cloudClient fit2cloudClient, ContainerDeployTmpDto tmpDto) throws Exception {
        // 获取镜像的tag
        final String regex = "(https?://)?(.+?)/(.+?)/(.*):(.*)";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(this.imageUrl);
        String tag = null;
        if (matcher.find()) {
            tag = matcher.group(5);
        }
        if (tag == null) {
            throw new RuntimeException("无法获取镜像的标签tag");
        }
        log(String.format("开始检查应用版本和镜像标签[%s]是否存在", tag));
        CheckVersionAndTagDTO checkVersionAndTagDTO = fit2cloudClient.checkVersionAndTasIsExist(this.imageUrl,
                this.containerApplicationId);
        if (!checkVersionAndTagDTO.isTag()) {
            log(String.format("镜像标签[%s]在Devops上不存在,开始同步此标签", tag));
            if (fit2cloudClient.syncHarborSingleTag(tag, this.containerApplicationId, tmpDto.getOrganizationId())) {
                log(String.format("镜像标签[%s]同步成功", tag));
            }
        }
        if (StringUtils.isNotEmpty(checkVersionAndTagDTO.getVersionId())) {
            log("开始更新容器应用版本");
        } else {
            log("开始新增容器应用版本");
        }
        //构造更新/新增容器应用版本参数
        SaveOrUpdateApplicationVersionDto request = new SaveOrUpdateApplicationVersionDto();
        request.setContainerPort(this.containerPort);
        request.setPrimaryImage(this.privateImage);
        request.setApplicationId(this.containerApplicationId);
        request.setTag(tag);
        request.setVersionId(checkVersionAndTagDTO.getVersionId());
        String applicationVersionId = fit2cloudClient.saveOrUpdateApplicationVersion(request);
        tmpDto.setApplicationVersionId(applicationVersionId);
        if (StringUtils.isNotEmpty(checkVersionAndTagDTO.getVersionId())) {
            log("更新容器应用版本完成");
        } else {
            log("新增容器应用版本完成");
        }
    }


    private void checkContainerAppDeployParams(final Fit2cloudClient fit2cloudClient, ContainerAppDeployTmpDto tmpDto, AbstractBuild build, BuildListener listener) throws Exception {
        if (StringUtils.isBlank(this.workspaceId)) {
            throw new CodeDeployException("工作空间不能为空！");
        }
        if (StringUtils.isBlank(this.containerAppId)) {
            throw new CodeDeployException("容器集群应用不能为空！");
        }
        if (StringUtils.isBlank(this.containerAppVersion)) {
            throw new CodeDeployException("容器集群应用版本不能为空！");
        }

        String imageTag = "";
        String csAppVersion = "";
        try {
            imageTag = Utils.replaceTokens(build, listener, this.applicationImageTag);
            csAppVersion = Utils.replaceTokens(build, listener, this.containerAppVersion);
        } catch (Exception e) {
            throw new CodeDeployException("转换镜像标签失败！：" + this.applicationImageTag);
        }

        boolean findWorkspace = false;
        List<Workspace> workspaces = fit2cloudClient.getWorkspace();
        for (Workspace workspace : workspaces) {
            if (workspace.getId().equals(this.workspaceId)) {
                findWorkspace = true;
                tmpDto.setOrganizationId(workspace.getOrganizationId());
            }
        }
        if (!findWorkspace) {
            throw new CodeDeployException("工作空间不存在！");
        }

        List<ContainerApplicationDTO> containerApplications = fit2cloudClient.getContainerApplications(this.workspaceId, CommonConstants.CONTAINER_APP);
        Optional<ContainerApplicationDTO> anyApp = containerApplications.stream().filter(item -> item.getId().equalsIgnoreCase(this.containerAppId)).findAny();
        if (!anyApp.isPresent()) {
            throw new CodeDeployException("容器集群应用不存在！");
        }

        ContainerApplicationDTO containerApplicationDTO = anyApp.get();
        tmpDto.setApplicationId(containerApplicationDTO.getId());
        //应用版本名称
        tmpDto.setName(csAppVersion);
        tmpDto.setDescription(this.containerAppVersionDesc);

        log("查找对应的应用容器");
        List<ApplicationContainer> applicationContainers = getApplicationContainers(this.f2cAccessKey, this.f2cSecretKey, this.f2cEndpoint, this.workspaceId, this.containerAppId);
        Optional<ApplicationContainer> currentApplicationContainerOptional = applicationContainers.stream().filter(image -> Objects.equals(image.getName(), this.applicationImage)).findAny();
        if (currentApplicationContainerOptional.isPresent()) {
            log("查找对应的应用容器成功");
        } else {
            log(String.format("获取集群应用容器失败，容器名：[%s]不存在", this.applicationImage));
        }
        ApplicationContainer applicationContainer = currentApplicationContainerOptional.get();

        com.alibaba.fastjson.JSONObject image = new com.alibaba.fastjson.JSONObject();
        image.put("imageId", applicationContainer.getImageId());
        image.put("repositoryId", applicationContainer.getRepositoryId());
        image.put("containerId", applicationContainer.getId());

        image.put("imageTag", imageTag);
        List<com.alibaba.fastjson.JSONObject> images = new ArrayList<>();
        images.add(image);
        tmpDto.setImages(images);

        if (!this.containerAppAutoDeploy) {
            return;
        }
        if (StringUtils.isBlank(this.containerAppRuntimeEnvId)) {
            throw new CodeDeployException("容器集群运行环境不能为空！");
        }
        if (StringUtils.isBlank(this.containerAppClusterId)) {
            throw new CodeDeployException("容器集群不能为空！");
        }
        if (StringUtils.isBlank(this.containerAppNamespaceId)) {
            throw new CodeDeployException("容器集群命名空间不能为空！");
        }

        tmpDto.setAutoDeploy(this.containerAppAutoDeploy);
        tmpDto.setRuntimeEnvId(this.containerAppRuntimeEnvId);
        tmpDto.setClusterId(this.containerAppClusterId);
        tmpDto.setNamespaceId(this.containerAppNamespaceId);

        boolean appIsExistsPVC = true;
        try {
            List<com.alibaba.fastjson.JSONObject> list = new ArrayList<>();
            Fit2cloudClient fit2CloudClient = new Fit2cloudClient(this.f2cAccessKey, this.f2cSecretKey, this.f2cEndpoint);
            appIsExistsPVC = fit2CloudClient.getAppIsExistsPVC(workspaceId, CommonConstants.CONTAINER, containerAppClusterId);

        } catch (Exception e) {
            // e.printStackTrace();
            // return FormValidation.error(e.getMessage());
        }

        //容器是否存在PVC
        if (!appIsExistsPVC) {
            return;
        }
        log("pv : " + this.checkContainerAppStoragePV);
        log("storageClass : " + this.checkContainerAppStorageClass);

        log("containerAppPv : " + this.containerAppPv);
        log("containerAppStorageClass : " + this.containerAppStorageClass);

        if (this.checkContainerAppStoragePV) {
            tmpDto.setStorageType("pv");
            tmpDto.setStorageName(this.containerAppPv);
        }

        if (this.checkContainerAppStorageClass) {
            tmpDto.setStorageType("storageClass");
            tmpDto.setStorageName(this.containerAppStorageClass);
        }

        if (StringUtils.isBlank(tmpDto.getStorageType()) || StringUtils.isBlank(tmpDto.getStorageName())) {
            throw new CodeDeployException("存储不能为空，请检查配置参数！");
        }
    }

    public List<ApplicationContainer> getApplicationContainers(String f2cAccessKey,
                                                               String f2cSecretKey,
                                                               String f2cEndpoint,
                                                               String workspaceId,
                                                               String containerAppId) throws CodeDeployException {
        List<ApplicationContainer> versions = new ArrayList<>();
        try {
            List<com.alibaba.fastjson.JSONObject> containers = new ArrayList<>();
            Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
            if (workspaceId != null && !workspaceId.equals("")) {
                containers = fit2CloudClient.getApplicationContainers(containerAppId, workspaceId, CommonConstants.CONTAINER_APP);
            }
            if (Objects.isNull(containers) || containers.size() <= 0) {
                return versions;
            }
            for (com.alibaba.fastjson.JSONObject container : containers) {
                versions.add(com.alibaba.fastjson.JSONObject.parseObject(container.toJSONString(), ApplicationContainer.class));
            }
        } catch (Exception e) {
            throw new CodeDeployException(e.getMessage());
        }
        return versions;
    }


    private void checkContainerDeployParams(final Fit2cloudClient fit2cloudClient, ContainerDeployTmpDto tmpDto) throws Exception {
        boolean findWorkspace = false;
        List<Workspace> workspaces = fit2cloudClient.getWorkspace();
        for (Workspace workspace : workspaces) {
            if (workspace.getId().equals(this.workspaceId)) {
                findWorkspace = true;
                tmpDto.setOrganizationId(workspace.getOrganizationId());
            }
        }
        if (!findWorkspace) {
            throw new CodeDeployException("工作空间不存在！");
        }

        boolean findApplication = false;
        List<ApplicationDTO> applications = fit2cloudClient.getApplications(this.workspaceId, CommonConstants.CONTAINER);
        for (ApplicationDTO applicationDTO : applications) {
            if (applicationDTO.getId().equals(this.containerApplicationId)) {
                findApplication = true;
                break;
            }
        }
        if (!findApplication) {
            throw new CodeDeployException("容器应用不存在！");
        }
        if (StringUtils.isEmpty(this.imageUrl)) {
            throw new CodeDeployException("镜像地址不能为空");
        }
        final String regex = "(https?:\\/\\/)?(.+?)\\/(.+?)\\/(.*):(.*)";

        if (!this.imageUrl.matches(regex)) {
            throw new RuntimeException("镜像格式不正确[域名/项目名/镜像名:标签名]");
        }

        if (this.containerPort == null) {
            throw new CodeDeployException("容器端口不能为空");
        }

        boolean findNamespace = false;
        List<ContainerResourceNamespace> namespaces = fit2cloudClient.getDeployNamespaces(this.workspaceId);
        for (ContainerResourceNamespace namespace : namespaces) {
            if (namespace.getId().equals(this.deployNamespaceId)) {
                findNamespace = true;
                break;
            }
        }
        if (!findNamespace) {
            throw new CodeDeployException("命名空间不能为空！");
        }

        if (this.privateImage) {
            boolean findSecret = false;
            List<ContainerResourceSecret> secrets = fit2cloudClient.getDockerSecret(this.deployNamespaceId);
            for (ContainerResourceSecret secret : secrets) {
                if (secret.getId().equals(this.secret)) {
                    findSecret = true;
                    break;
                }
            }
            if (!findSecret) {
                throw new CodeDeployException("Secret不能为空！");
            }
        }

        boolean findContainerCluster = false;
        List<ContainerCluster> containerClusters = fit2cloudClient.getDeployContainerClusters(this.workspaceId);
        for (ContainerCluster containerCluster : containerClusters) {
            if (containerCluster.getId().equals(this.deployClusterId)) {
                findContainerCluster = true;
                break;
            }
        }
        if (!findContainerCluster) {
            throw new CodeDeployException("容器集群不能为空！");
        }

        boolean findPod = false;
        List<ContainerPods> ContainerPodList = fit2cloudClient.getDeployPodsByContainerClusterId(this.deployClusterId);
        for (ContainerPods containerPods : ContainerPodList) {
            if (containerPods.getId().equals(this.deployDeploymentsId)) {
                findPod = true;
                break;
            }
        }
        if (!findPod) {
            throw new CodeDeployException("部署组不能为空！");
        }

        if (StringUtils.isEmpty(this.portType)) {
            throw new CodeDeployException("端口类型不能为空");
        }
    }

    private File zipFile(String zipFileName, FilePath sourceDirectory, String includesNew, String excludesNew, String appspecFilePathNew) throws IOException, InterruptedException, IllegalArgumentException {
        FilePath appspecFp = new FilePath(sourceDirectory, appspecFilePathNew);

        log("指定 appspecPath ::::: " + appspecFp.toURI().getPath());
        if (appspecFp.exists()) {
            if (!"appspec.yml".equals(appspecFilePathNew)) {
                FilePath appspecDestFP = new FilePath(sourceDirectory, "appspec.yml");
                log("目标 appspecPath  ::::: " + appspecDestFP.toURI().getPath());
                appspecFp.copyTo(appspecDestFP);
            }
            log("成功添加appspec文件");
        } else {
            throw new IllegalArgumentException("没有找到对应的appspec.yml文件！");
        }

        File zipFile = new File("/tmp/" + zipFileName);
        final boolean fileCreated = zipFile.createNewFile();
        if (!fileCreated) {
            log("Zip文件已存在，开始覆盖 : " + zipFile.getPath());
        }

        log("生成Zip文件 : " + zipFile.getAbsolutePath());
        FileOutputStream outputStream = new FileOutputStream(zipFile);
        try {
            String allIncludes = includesNew + ",appspec.yml";
            sourceDirectory.zip(outputStream, new DirScanner.Glob(allIncludes, excludesNew)
            );
        } finally {
            outputStream.close();
        }
        return zipFile;
    }


    @Override
    public DescriptorImpl getDescriptor() {

        return (DescriptorImpl) super.getDescriptor();
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.STEP;
    }

    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        public FormValidation doCheckAccount(
                @QueryParameter String f2cAccessKey,
                @QueryParameter String f2cSecretKey,
                @QueryParameter String f2cEndpoint) {
            if (StringUtils.isEmpty(f2cAccessKey)) {
                return FormValidation.error("FIT2CLOUD ConsumerKey不能为空！");
            }
            if (StringUtils.isEmpty(f2cSecretKey)) {
                return FormValidation.error("FIT2CLOUD SecretKey不能为空！");
            }
            if (StringUtils.isEmpty(f2cEndpoint)) {
                return FormValidation.error("FIT2CLOUD EndPoint不能为空！");
            }
            try {
                Fit2cloudClient fit2cloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                fit2cloudClient.checkUser();
            } catch (Exception e) {
                return FormValidation.error(e.getMessage());
            }
            return FormValidation.ok("验证FIT2CLOUD帐号成功！");
        }

        public ListBoxModel doFillWorkspaceIdItems(@QueryParameter String f2cAccessKey,
                                                   @QueryParameter String f2cSecretKey,
                                                   @QueryParameter String f2cEndpoint) {
            ListBoxModel items = new ListBoxModel();
            items.add("请选择工作空间", "");
            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                List<Workspace> list = fit2CloudClient.getWorkspace();
                if (list != null && list.size() > 0) {
                    for (Workspace c : list) {
                        items.add(c.getName(), String.valueOf(c.getId()));
                    }
                }
            } catch (Exception e) {
//            		e.printStackTrace();
//                return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillApplicationImageItems(@QueryParameter String f2cAccessKey,
                                                        @QueryParameter String f2cSecretKey,
                                                        @QueryParameter String f2cEndpoint,
                                                        @QueryParameter String workspaceId,
                                                        @QueryParameter String containerAppId) {
            List<ApplicationContainer> versions = new ArrayList<>();
            ListBoxModel items = new ListBoxModel();
            try {
                List<com.alibaba.fastjson.JSONObject> containers = new ArrayList<>();
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (workspaceId != null && !workspaceId.equals("")) {
                    containers = fit2CloudClient.getApplicationContainers(containerAppId, workspaceId, CommonConstants.CONTAINER_APP);
                }
                if (Objects.isNull(containers) || containers.size() <= 0) {
                    return items;
                }
                for (com.alibaba.fastjson.JSONObject container : containers) {
                    versions.add(com.alibaba.fastjson.JSONObject.parseObject(container.toJSONString(), ApplicationContainer.class));
                    items.add(container.getString("name"), container.getString("name"));
                }
                System.out.println("开始获取容器版本结束    " + com.alibaba.fastjson.JSONObject.toJSONString(items));
            } catch (Exception e) {
                e.printStackTrace();
                // return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillContainerAppIdItems(@QueryParameter String f2cAccessKey,
                                                      @QueryParameter String f2cSecretKey,
                                                      @QueryParameter String f2cEndpoint,
                                                      @QueryParameter String workspaceId) {
            ListBoxModel items = new ListBoxModel();

            try {
                List<ContainerApplicationDTO> list = new ArrayList<>();
                items.add("请选择应用", "");
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (workspaceId != null && !workspaceId.equals("")) {
                    list = fit2CloudClient.getContainerApplications(workspaceId, CommonConstants.CONTAINER_APP);
                }
                if (list != null && list.size() > 0) {
                    for (Application c : list) {
                        items.add(c.getName(), String.valueOf(c.getId()));
                    }
                }
            } catch (Exception e) {
//            		e.printStackTrace();
//                return FormValidation.error(e.getMessage());
            }
            return items;
        }


        public ListBoxModel doFillApplicationIdItems(@QueryParameter String f2cAccessKey,
                                                     @QueryParameter String f2cSecretKey,
                                                     @QueryParameter String f2cEndpoint,
                                                     @QueryParameter String workspaceId) {
            ListBoxModel items = new ListBoxModel();
            try {
                List<ApplicationDTO> list = new ArrayList<>();
                items.add("请选择应用", "");
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (workspaceId != null && !workspaceId.equals("")) {
                    list = fit2CloudClient.getApplications(workspaceId, CommonConstants.OTHER);
                }
                if (list != null && list.size() > 0) {
                    for (Application c : list) {
                        items.add(c.getName(), String.valueOf(c.getId()));
                    }
                }
            } catch (Exception e) {
//            		e.printStackTrace();
//                return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillContainerApplicationIdItems(@QueryParameter String f2cAccessKey,
                                                              @QueryParameter String f2cSecretKey,
                                                              @QueryParameter String f2cEndpoint,
                                                              @QueryParameter String workspaceId) {
            ListBoxModel items = new ListBoxModel();
            try {
                List<ApplicationDTO> list = new ArrayList<>();
                items.add("请选择容器应用", "");
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (workspaceId != null && !workspaceId.equals("")) {
                    list = fit2CloudClient.getApplications(workspaceId, CommonConstants.CONTAINER);
                }
                if (list != null && list.size() > 0) {
                    for (Application c : list) {
                        items.add(c.getName(), String.valueOf(c.getId()));
                    }
                }
            } catch (Exception e) {
//            		e.printStackTrace();
//                return FormValidation.error(e.getMessage());
            }
            return items;
        }


        public ListBoxModel doFillContainerAppRuntimeEnvIdItems(@QueryParameter String f2cAccessKey,
                                                              @QueryParameter String f2cSecretKey,
                                                              @QueryParameter String f2cEndpoint,
                                                              @QueryParameter String workspaceId) {
            ListBoxModel items = new ListBoxModel();
            try {
                List<com.alibaba.fastjson.JSONObject> list = new ArrayList<>();
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (workspaceId != null && !workspaceId.equals("")) {
                    list = fit2CloudClient.getContainerAppRuntimeEnvs(workspaceId, CommonConstants.CONTAINER);
                }
                if (list != null && list.size() > 0) {
                    for (com.alibaba.fastjson.JSONObject c : list) {
                        items.add(c.getString("name"), c.getString("id"));
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                // return FormValidation.error(e.getMessage());
            }
            return items;
        }



        public ListBoxModel doFillContainerAppClusterIdItems(@QueryParameter String f2cAccessKey,
                                                              @QueryParameter String f2cSecretKey,
                                                              @QueryParameter String f2cEndpoint,
                                                              @QueryParameter String workspaceId) {
            ListBoxModel items = new ListBoxModel();
            try {
                List<com.alibaba.fastjson.JSONObject> list = new ArrayList<>();
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (workspaceId != null && !workspaceId.equals("")) {
                    list = fit2CloudClient.getContainerAppClusters(workspaceId, CommonConstants.CONTAINER);
                }
                if (list != null && list.size() > 0) {
                    for (com.alibaba.fastjson.JSONObject c : list) {
                        items.add(c.getString("name"), c.getString("id"));
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                // return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillContainerAppNamespaceIdItems(@QueryParameter String f2cAccessKey,
                                                              @QueryParameter String f2cSecretKey,
                                                              @QueryParameter String f2cEndpoint,
                                                              @QueryParameter String workspaceId,
                                                              @QueryParameter String containerAppClusterId) {
            ListBoxModel items = new ListBoxModel();
            try {
                List<com.alibaba.fastjson.JSONObject> list = new ArrayList<>();
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (StringUtils.isNotBlank(workspaceId) && StringUtils.isNotBlank(containerAppClusterId)) {
                    list = fit2CloudClient.getContainerAppNamespaces(workspaceId, CommonConstants.CONTAINER, containerAppClusterId);
                }
                if (list != null && list.size() > 0) {
                    for (com.alibaba.fastjson.JSONObject c : list) {
                        items.add(StringUtils.defaultString(c.getString("displayName"), c.getString("name")), c.getString("id"));
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                // return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillContainerAppPvItems(@QueryParameter String f2cAccessKey,
                                                               @QueryParameter String f2cSecretKey,
                                                               @QueryParameter String f2cEndpoint,
                                                               @QueryParameter String workspaceId,
                                                               @QueryParameter String containerAppClusterId) {
            ListBoxModel items = new ListBoxModel();
            try {
                List<com.alibaba.fastjson.JSONObject> list = new ArrayList<>();
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (StringUtils.isNotBlank(workspaceId) && StringUtils.isNotBlank(containerAppClusterId)) {
                    list = fit2CloudClient.getContainerAppPVs(workspaceId, CommonConstants.CONTAINER, containerAppClusterId);
                }
                if (list != null && list.size() > 0) {
                    for (com.alibaba.fastjson.JSONObject c : list) {
                        items.add(c.getString("name"), c.getString("name"));
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                // return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillContainerAppStorageTypeItems() {
            ListBoxModel items = new ListBoxModel();
            items.add("Persistent Volume", "pv");
            items.add("Storage Class", "storageClass");
            return items;
        }

        public ListBoxModel doFillContainerAppStorageClassItems(@QueryParameter String f2cAccessKey,
                                                      @QueryParameter String f2cSecretKey,
                                                      @QueryParameter String f2cEndpoint,
                                                      @QueryParameter String workspaceId,
                                                      @QueryParameter String containerAppClusterId) {
            ListBoxModel items = new ListBoxModel();
            try {
                List<com.alibaba.fastjson.JSONObject> list = new ArrayList<>();
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                if (StringUtils.isNotBlank(workspaceId) && StringUtils.isNotBlank(containerAppClusterId)) {
                    list = fit2CloudClient.getContainerAppStorageClassAll(workspaceId, CommonConstants.CONTAINER, containerAppClusterId);
                }
                if (list != null && list.size() > 0) {
                    for (com.alibaba.fastjson.JSONObject c : list) {
                        items.add(c.getString("name"), c.getString("name"));
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                // return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillRepositorySettingIdItems(@QueryParameter String f2cAccessKey,
                                                           @QueryParameter String f2cSecretKey,
                                                           @QueryParameter String f2cEndpoint,
                                                           @QueryParameter String workspaceId,
                                                           @QueryParameter String applicationId) {
            ListBoxModel items = new ListBoxModel();
            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                items.add("请选择环境", "");
                List<ApplicationDTO> applicationDTOS = fit2CloudClient.getApplications(workspaceId, CommonConstants.OTHER);

                ApplicationDTO application = null;

                for (ApplicationDTO applicationDTO : applicationDTOS) {
                    if (applicationDTO.getId().equals(applicationId)) {
                        application = applicationDTO;
                    }
                }

                assert application != null;
                List<ApplicationRepositorySetting> list = application.getApplicationRepositorySettings();
                List<ApplicationRepository> applicationRepositories = fit2CloudClient.getApplicationRepositorys(workspaceId);
                List<TagValue> envs = fit2CloudClient.getEnvList();

                if (list != null && list.size() > 0) {
                    for (ApplicationRepositorySetting c : list) {
                        ApplicationRepository repository = null;
                        for (ApplicationRepository applicationRepository : applicationRepositories) {
                            if (applicationRepository.getId().equals(c.getRepositoryId())) {
                                repository = applicationRepository;
                            }
                        }
                        String envName = null;
                        for (TagValue env : envs) {
                            if (env.getId().equals(c.getEnvId())) {
                                envName = env.getTagValueAlias();
                            }
                            if (c.getEnvId().equalsIgnoreCase("ALL")) {
                                envName = "全部环境";
                            }
                        }


                        assert repository != null;
                        items.add(envName + "---" + repository.getType(), String.valueOf(c.getId()));
                    }
                }
            } catch (Exception e) {
//            		e.printStackTrace();
//                return FormValidation.error(e.getMessage());
            }
            return items;
        }


        public ListBoxModel doFillClusterIdItems(@QueryParameter String f2cAccessKey,
                                                 @QueryParameter String f2cSecretKey,
                                                 @QueryParameter String f2cEndpoint,
                                                 @QueryParameter String workspaceId,
                                                 @QueryParameter String applicationId,
                                                 @QueryParameter String repositorySettingId,
                                                 @QueryParameter String applicationRepositoryId) {
            ListBoxModel items = new ListBoxModel();
            items.add("请选择集群", "");

            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                List<ClusterDTO> list = fit2CloudClient.getClusters(workspaceId);

                if (list != null && list.size() > 0) {
                    for (ClusterDTO c : list) {
                        items.add(c.getName(), String.valueOf(c.getId()));
                    }
                }
            } catch (Exception e) {
//            		e.printStackTrace();
//                return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillClusterRoleIdItems(@QueryParameter String f2cAccessKey,
                                                     @QueryParameter String f2cSecretKey,
                                                     @QueryParameter String f2cEndpoint,
                                                     @QueryParameter String workspaceId,
                                                     @QueryParameter String clusterId) {
            ListBoxModel items = new ListBoxModel();
            items.add("请选择主机组", "");

            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                List<ClusterRole> list = fit2CloudClient.getClusterRoles(workspaceId, clusterId);
                if (list != null && list.size() > 0) {
//                    items.add("全部主机组", "ALL");
                    for (ClusterRole c : list) {
                        items.add(c.getName(), String.valueOf(c.getId()));
                    }
                }
            } catch (Exception e) {
//            		e.printStackTrace();
//                return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillCloudServerIdItems(@QueryParameter String f2cAccessKey,
                                                     @QueryParameter String f2cSecretKey,
                                                     @QueryParameter String f2cEndpoint,
                                                     @QueryParameter String workspaceId,
                                                     @QueryParameter String clusterId,
                                                     @QueryParameter String clusterRoleId) {
            ListBoxModel items = new ListBoxModel();
            items.add("请选择主机", "");
            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                List<CloudServer> list = fit2CloudClient.getCloudServers(workspaceId, clusterRoleId, clusterId);
                if (list != null && list.size() > 0) {
                    items.add("全部主机", "ALL");
                    for (CloudServer c : list) {
                        items.add(c.getInstanceName(), String.valueOf(c.getId()));
                    }
                }
            } catch (Exception e) {
//            		e.printStackTrace();
//                return FormValidation.error(e.getMessage());
            }
            return items;
        }

        public ListBoxModel doFillExecuteTypeItems() {
            ListBoxModel items = new ListBoxModel();
            items.add("自动", "automatic");
            items.add("手动", "manual");
            return items;
        }

        public ListBoxModel doFillFailStrategyItems() {
            ListBoxModel items = new ListBoxModel();
            items.add("挂起", "automatic");
            items.add("自动忽略", "ignore");
            return items;
        }

        public ListBoxModel doFillDeployPolicyItems() {
            ListBoxModel items = new ListBoxModel();
            items.add("全部同时部署", "all");
            items.add("半数分批部署", "harf");
            items.add("单台依次部署", "sigle");
            return items;
        }

        public ListBoxModel doFillDeployNamespaceIdItems(@QueryParameter String f2cAccessKey,
                                                         @QueryParameter String f2cSecretKey,
                                                         @QueryParameter String f2cEndpoint,
                                                         @QueryParameter String workspaceId) {
            ListBoxModel items = new ListBoxModel();
            items.add("请选择命名空间", "");
            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                List<ContainerResourceNamespace> list = fit2CloudClient.getDeployNamespaces(workspaceId);
                if (list != null && list.size() > 0) {
                    for (ContainerResourceNamespace c : list) {
                        items.add(String.format("%s(%s)", c.getName(), c.getAccountName()), c.getId());
                    }
                }
            } catch (Exception e) {
//            	e.printStackTrace();
            }
            return items;
        }

        public ListBoxModel doFillDeployClusterIdItems(@QueryParameter String f2cAccessKey,
                                                       @QueryParameter String f2cSecretKey,
                                                       @QueryParameter String f2cEndpoint,
                                                       @QueryParameter String workspaceId) {
            ListBoxModel items = new ListBoxModel();
            items.add("请选择容器集群", "");
            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                List<ContainerCluster> list = fit2CloudClient.getDeployContainerClusters(workspaceId);
                if (list != null && list.size() > 0) {
                    for (ContainerCluster c : list) {
                        items.add(c.getName(), c.getId());
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return items;
        }

        public ListBoxModel doFillDeployDeploymentsIdItems(@QueryParameter String f2cAccessKey,
                                                           @QueryParameter String f2cSecretKey,
                                                           @QueryParameter String f2cEndpoint,
                                                           @QueryParameter String deployClusterId) {
            ListBoxModel items = new ListBoxModel();
            items.add("请选择部署组", "");
            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                List<ContainerPods> list = fit2CloudClient.getDeployPodsByContainerClusterId(deployClusterId);
                if (list != null && list.size() > 0) {
                    for (ContainerPods c : list) {
                        items.add(c.getName(), c.getId());
                    }
                }
            } catch (Exception e) {
//            	e.printStackTrace();
            }
            return items;
        }

        public ListBoxModel doFillSecretItems(@QueryParameter String f2cAccessKey,
                                              @QueryParameter String f2cSecretKey,
                                              @QueryParameter String f2cEndpoint,
                                              @QueryParameter String deployNamespaceId) {
            ListBoxModel items = new ListBoxModel();
            items.add("请选择Secret", "");
            if (StringUtils.isEmpty(deployNamespaceId)) {
                return items;
            }
            try {
                Fit2cloudClient fit2CloudClient = new Fit2cloudClient(f2cAccessKey, f2cSecretKey, f2cEndpoint);
                List<ContainerResourceSecret> list = fit2CloudClient.getDockerSecret(deployNamespaceId);
                if (list != null && list.size() > 0) {
                    for (ContainerResourceSecret c : list) {
                        items.add(c.getName(), c.getId());
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return items;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            req.bindParameters(this);
            save();
            return super.configure(req, formData);
        }


        /**
         * In order to load the persisted global configuration, you have to
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            super(F2CCodeDeployPublisher.class);
            load();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "FIT2CLOUD 代码部署";
        }


    }

    private ApplicationSetting findApplicationSetting(String applicationId) {
        ApplicationSetting applicationSetting = null;
        final Fit2cloudClient fit2cloudClient = new Fit2cloudClient(this.f2cAccessKey, this.f2cSecretKey, this.f2cEndpoint);
        List<ApplicationSetting> applicationSettings = fit2cloudClient.getApplicationSettings(applicationId);
        for (ApplicationSetting appst : applicationSettings) {
            if (appst.getId().equalsIgnoreCase(this.applicationSettingId)) {
                applicationSetting = appst;
            }
        }
        return applicationSetting;
    }


    public String getF2cEndpoint() {
        return f2cEndpoint;
    }

    public String getF2cAccessKey() {
        return f2cAccessKey;
    }

    public String getF2cSecretKey() {
        return f2cSecretKey;
    }

    public String getApplicationRepositoryId() {
        return applicationRepositoryId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public boolean isNexusChecked() {
        return nexusChecked;
    }

    public boolean isOssChecked() {
        return ossChecked;
    }

    public boolean isS3Checked() {
        return s3Checked;
    }

    public boolean isAutoDeploy() {
        return autoDeploy;
    }

    public boolean isArtifactoryChecked() {
        return artifactoryChecked;
    }

    public String getApplicationSettingId() {
        return applicationSettingId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public String getClusterRoleId() {
        return clusterRoleId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public String getCloudServerId() {
        return cloudServerId;
    }

    public String getDeployPolicy() {
        return deployPolicy;
    }

    public String getApplicationVersionName() {
        return applicationVersionName;
    }

    public String getIncludes() {
        return includes;
    }

    public String getExcludes() {
        return excludes;
    }

    public String getAppspecFilePath() {
        return appspecFilePath;
    }

    public String getDescription() {
        return description;
    }

    public boolean isWaitForCompletion() {
        return waitForCompletion;
    }

    public Long getPollingTimeoutSec() {
        return pollingTimeoutSec;
    }

    public Long getPollingFreqSec() {
        return pollingFreqSec;
    }

    private void log(String msg) {
        logger.println(LOG_PREFIX + msg);
    }

    public String getRepositorySettingId() {
        return repositorySettingId;
    }

    public String getArtifactType() {
        return artifactType;
    }

    public String getObjectPrefixAliyun() {
        return objectPrefixAliyun;
    }

    public String getObjectPrefixAWS() {
        return objectPrefixAWS;
    }

    public String getPath() {
        return path;
    }

    public String getNexusGroupId() {
        return nexusGroupId;
    }

    public String getNexusArtifactId() {
        return nexusArtifactId;
    }

    public String getNexusArtifactVersion() {
        return nexusArtifactVersion;
    }

    public boolean isCustomZip() {
        return customZip;
    }

    public String getZipFilePath() {
        return zipFilePath;
    }

    public String getExecuteType() {
        return executeType;
    }

    public String getDeployType() {
        return deployType;
    }

    public boolean isContainerChecked() {
        return containerChecked;
    }

    public boolean isOtherChecked() {
        return otherChecked;
    }

    public boolean isContainerAppChecked() {
        return containerAppChecked;
    }

    public String getContainerApplicationId() {
        return containerApplicationId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getContainerPort() {
        return containerPort;
    }

    public String getDeployNamespaceId() {
        return deployNamespaceId;
    }

    public String getDeployClusterId() {
        return deployClusterId;
    }

    public String getDeployDeploymentsId() {
        return deployDeploymentsId;
    }

    public String getPortType() {
        return portType;
    }

    public boolean isClusterIpChecked() {
        return clusterIpChecked;
    }

    public boolean isNodePortChecked() {
        return nodePortChecked;
    }

    public Integer getNodePort() {
        return nodePort;
    }

    public boolean isHeadless() {
        return headless;
    }

    public boolean isPrivateImage() {
        return privateImage;
    }

    public String getSecret() {
        return secret;
    }

    public boolean isNoCustomZip() {
        return noCustomZip;
    }

    public String getContainerAppId() {
        return containerAppId;
    }

    public String getContainerAppVersion() {
        return containerAppVersion;
    }

    public String getContainerAppVersionDesc() {
        return containerAppVersionDesc;
    }

    public String getApplicationImageTag() {
        return applicationImageTag;
    }

    public String getApplicationImage() {
        return applicationImage;
    }

    public boolean isContainerAppAutoDeploy() {
        return containerAppAutoDeploy;
    }

    public String getContainerAppRuntimeEnvId() {
        return containerAppRuntimeEnvId;
    }

    public String getContainerAppClusterId() {
        return containerAppClusterId;
    }

    public String getContainerAppNamespaceId() {
        return containerAppNamespaceId;
    }

    @DataBoundSetter
    public void setContainerAppClusterId(String containerAppClusterId) {
        this.containerAppClusterId = containerAppClusterId;
    }

    @DataBoundSetter
    public void setContainerAppNamespaceId(String containerAppNamespaceId) {
        this.containerAppNamespaceId = containerAppNamespaceId;
    }

    public String getContainerAppPv() {
        return containerAppPv;
    }

    @DataBoundSetter
    public void setContainerAppPv(String containerAppPv) {
        this.containerAppPv = containerAppPv;
    }

    public String getContainerAppStorageClass() {
        return containerAppStorageClass;
    }

    @DataBoundSetter
    public void setContainerAppStorageClass(String containerAppStorageClass) {
        this.containerAppStorageClass = containerAppStorageClass;
    }

    public boolean isCheckContainerAppStoragePV() {
        return checkContainerAppStoragePV;
    }

    public boolean isCheckContainerAppStorageClass() {
        return checkContainerAppStorageClass;
    }

    public String getContainerAppStorageType() {
        return containerAppStorageType;
    }

    @DataBoundSetter
    public void setContainerAppStorageType(String containerAppStorageType) {
        this.containerAppStorageType = containerAppStorageType;
    }

    @DataBoundSetter
    public void setCheckContainerAppStoragePV(boolean checkContainerAppStoragePV) {
        this.checkContainerAppStoragePV = checkContainerAppStoragePV;
    }

    @DataBoundSetter
    public void setCheckContainerAppStorageClass(boolean checkContainerAppStorageClass) {
        this.checkContainerAppStorageClass = checkContainerAppStorageClass;
    }
}
