package com.fit2cloud.codedeploy2.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit2cloud.codedeploy2.CommonConstants;
import com.fit2cloud.codedeploy2.client.model.*;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.*;

public class Fit2cloudClient {


    private static final String ACCEPT = "application/json;charset=UTF-8";
    private static final Integer CONNECT_TIME_OUT = 10000;
    private static final Integer CONNECT_REQUEST_TIME_OUT = 10000;
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private HttpClient httpClient;

    public Fit2cloudClient(String accessKey, String secretKey, String endpoint) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.endpoint = endpoint;
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT).build();
        httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig).build();
    }

    public void checkUser() {
        Result getUserResult = call(ApiUrlConstants.USER_INFO, RequestMethod.GET);
        if (!getUserResult.isSuccess()) {
            throw new Fit2CloudException(getUserResult.getMessage());
        }
    }

    public List<Workspace> getWorkspace() {
        Result result = call(ApiUrlConstants.GET_USER_WORKSPACE, RequestMethod.GET);
        if(result.isSuccess() && StringUtils.isNotEmpty(result.getData())){
            return JSONObject.parseArray(result.getData(),Workspace.class);
        }
        return Lists.newArrayList();
    }


    public List<ApplicationRepository> getApplicationRepositorys(String workspaceId) {
        long currentPage = 1L;
        long pageSize = 100L;
        long pageCount;
        List<ApplicationRepository> applicationRepositories = new ArrayList<ApplicationRepository>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        do {
            Result result = call(ApiUrlConstants.REPOSITORY_LIST + "/" + currentPage + "/" + pageSize, RequestMethod.POST, new HashMap(), headers);
            Page page = JSON.parseObject(result.getData(), Page.class);
            String listJson = JSON.toJSONString(page.getListObject());
            List<ApplicationRepository> appReps = JSON.parseArray(listJson, ApplicationRepository.class);
            applicationRepositories.addAll(appReps);
            pageCount = page.getPageCount();
            currentPage++;
        } while (pageCount > currentPage);
        return applicationRepositories;
    }

    public List<TagValue> getEnvList() {
        Result result = call(ApiUrlConstants.APPLICATION_ENV_LIST, RequestMethod.GET);
        return JSON.parseArray(result.getData(), TagValue.class);
    }


    public List<ApplicationDTO> getApplications(String workspaceId,String type) {
        if(StringUtils.isEmpty(type)){
            type = CommonConstants.OTHER;
        }
        long currentPage = 0L;
        long pageSize = 100L;
        long pageCount;
        List<ApplicationDTO> applications = new ArrayList<ApplicationDTO>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);

        HashMap<String, Object> params = new HashMap<>();
        if(StringUtils.equalsIgnoreCase(type,CommonConstants.OTHER)){
            params.put("applicationTypeList", Lists.newArrayList("other","win_iis"));
        }else{
            params.put("applicationTypeList", Lists.newArrayList("container","container_yaml"));
        }
        do {
            currentPage++;
            Result result = call(ApiUrlConstants.APPLICATION_LIST + "/" + currentPage + "/" + pageSize, RequestMethod.POST, params, headers);
            Page page = JSON.parseObject(result.getData(), Page.class);
            String listJson = JSON.toJSONString(page.getListObject());
            List<ApplicationDTO> apps = JSON.parseArray(listJson, ApplicationDTO.class);
            applications.addAll(apps);
            pageCount = page.getPageCount();
        } while (pageCount > currentPage);
        return applications;
    }


    public List<ClusterDTO> getClusters(String workspaceId) {
        long currentPage = 1L;
        long pageSize = 100L;
        long pageCount;
        List<ClusterDTO> clusters = new ArrayList<ClusterDTO>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        do {
            Result result = call(ApiUrlConstants.CLUSTER_LIST + "/" + currentPage + "/" + pageSize, RequestMethod.POST, new HashMap<String, Object>(), headers);
            Page page = JSON.parseObject(result.getData(), Page.class);
            String listJson = JSON.toJSONString(page.getListObject());
            List<ClusterDTO> clusts = JSON.parseArray(listJson, ClusterDTO.class);
            clusters.addAll(clusts);
            pageCount = page.getPageCount();
            currentPage++;
        } while (pageCount > currentPage);
        return clusters;
    }


    public List<ApplicationSetting> getApplicationSettings(String applicationId) {
        List<ApplicationSetting> applicationSettings = new ArrayList<>();
        Result result = call(ApiUrlConstants.APPLICATION_SETTING_LIST + "?appId=" + applicationId, RequestMethod.GET);
        if (result.isSuccess()) {
            applicationSettings = JSON.parseArray(result.getData(), ApplicationSetting.class);
        }
        return applicationSettings;
    }


    public List<ClusterRole> getClusterRoles(String workspaceId, String clusterId) {
        long currentPage = 1L;
        long pageSize = 100L;
        long pageCount;
        List<ClusterRole> clusterRoles = new ArrayList<ClusterRole>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clusterId", clusterId);
        do {
            Result result = call(ApiUrlConstants.CLUSTER_ROLE_LIST + "/" + currentPage + "/" + pageSize, RequestMethod.POST, params, headers);
            Page page = JSON.parseObject(result.getData(), Page.class);
            String listJson = JSON.toJSONString(page.getListObject());
            List<ClusterRole> clusrs = JSON.parseArray(listJson, ClusterRole.class);
            clusterRoles.addAll(clusrs);
            pageCount = page.getPageCount();
            currentPage++;
        } while (pageCount > currentPage);
        return clusterRoles;
    }

    public List<CloudServer> getCloudServers(String workspaceId, String clusterRoleId, String clusterId) {
        long currentPage = 1L;
        long pageSize = 100L;
        long pageCount;
        List<CloudServer> cloudServers = new ArrayList<CloudServer>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clusterRoleId", clusterRoleId);
        params.put("clusterId", clusterId);

        do {
            Result result = call(ApiUrlConstants.SERVER_LIST + "/" + currentPage + "/" + pageSize, RequestMethod.POST, params, headers);
            Page page = JSON.parseObject(result.getData(), Page.class);
            String listJson = JSON.toJSONString(page.getListObject());
            List<CloudServer> clds = JSON.parseArray(listJson, CloudServer.class);
            cloudServers.addAll(clds);
            pageCount = page.getPageCount();
            currentPage++;
        } while (pageCount > currentPage);
        return cloudServers;
    }

    public List<ContainerResourceNamespace> getDeployNamespaces(String workspaceId) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        Result result = call(ApiUrlConstants.NAMESPACE_LIST, RequestMethod.GET,null,headers);
        return JSON.parseArray(result.getData(), ContainerResourceNamespace.class);
    }
    public List<ContainerCluster> getDeployContainerClusters(String workspaceId) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        Result result = call(ApiUrlConstants.CONTAINER_CLUSTER_LIST, RequestMethod.GET,null,headers);
        return JSON.parseArray(result.getData(), ContainerCluster.class);
    }

    public List<ContainerPods> getDeployPodsByContainerClusterId(String clusterId) {
        Result result = call(ApiUrlConstants.CONTAINER_PODS_LIST, RequestMethod.POST,clusterId,null);
        return JSON.parseArray(result.getData(), ContainerPods.class);
    }

    public ApplicationDeployment createApplicationDeployment(ApplicationDeployment applicationDeployment, String workspaceId) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        Result result = call(ApiUrlConstants.APPLICATION_DEPLOY_SAVE, RequestMethod.POST, applicationDeployment, headers);
        return JSON.parseObject(result.getData(), ApplicationDeployment.class);
    }

    public String deployAppVersion(ApplicationDeployment applicationDeployment, String workspaceId) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        Result result = call(ApiUrlConstants.APPLICATION_VERSION_DEPLOY, RequestMethod.POST, applicationDeployment, headers);
        return result.getData();
    }

    public ApplicationDeployment getApplicationDeployment(String applicationDeploymentId) {
        Result result = call(ApiUrlConstants.APPLICATION_SETTING_GET + "?applicationDeploymentId=" + applicationDeploymentId, RequestMethod.GET);
        return JSON.parseObject(result.getData(), ApplicationDeployment.class);
    }

    public List<ContainerResourceSecret> getDockerSecret(String namespaceId) {
        Result result = call(ApiUrlConstants.DOCKER_SECRET_LIST, RequestMethod.POST,namespaceId,null);
        return JSON.parseArray(result.getData(), ContainerResourceSecret.class);
    }

    public ApplicationVersion createApplicationVersion(ApplicationVersionDTO applicationVersion, String workspaceId) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("sourceId", workspaceId);
        Result result = call(ApiUrlConstants.APPLICATION_VERSION_SAVE, RequestMethod.POST, applicationVersion, headers);
        try {
            JSON.parseObject(result.getData(), ApplicationVersion.class);
        }catch (Exception e){
            throw new Fit2CloudException(result.getData());
        }
        return JSON.parseObject(result.getData(), ApplicationVersion.class);
    }


    private Result call(String url, RequestMethod requestMethod) {
        return call(url, requestMethod, null, null);
    }

    private Result call(String url, RequestMethod requestMethod, Object params, Map<String, String> headers) {
        url = this.endpoint + "/" + url;
        String responseJson = null;
        try {
            if (requestMethod == RequestMethod.GET) {
                HttpGet httpGet = new HttpGet(url);
                if (headers != null && headers.size() > 0) {
                    for (String key : headers.keySet()) {
                        httpGet.addHeader(key, headers.get(key));
                    }
                }
                auth(httpGet);
                HttpResponse response = httpClient.execute(httpGet);

                HttpEntity httpEntity = response.getEntity();

                responseJson = EntityUtils.toString(httpEntity);
            } else {
                HttpPost httpPost = new HttpPost(url);
                if (headers != null && headers.size() > 0) {
                    for (String key : headers.keySet()) {
                        httpPost.addHeader(key, headers.get(key));
                    }
                }
                if (params != null) {
                    StringEntity stringEntity;
                    if(params instanceof String){
                        stringEntity = new StringEntity(params.toString(), "UTF-8");
                    }else{
                        stringEntity = new StringEntity(JSON.toJSONString(params), "UTF-8");
                    }
                    httpPost.setEntity(stringEntity);
                }
                auth(httpPost);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                responseJson = EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Result result = JSON.parseObject(responseJson, Result.class);
        if (!result.isSuccess()) {
            throw new Fit2CloudException(result.getMessage() + result.isSuccess() + result.getData() + url);
        }
        return JSON.parseObject(responseJson, Result.class);
    }

    private void auth(HttpRequestBase httpRequestBase) {
        httpRequestBase.addHeader("Accept", ACCEPT);
        httpRequestBase.addHeader("accessKey", accessKey);
        String signature;
        try {
            signature = aesEncrypt(accessKey + "|" + UUID.randomUUID().toString() + "|" + System.currentTimeMillis(), secretKey, accessKey);
        } catch (Exception e) {
            throw new Fit2CloudException("签名失败: " + e.getMessage());
        }
        httpRequestBase.addHeader("signature", signature);
    }


    private static String aesEncrypt(String src, String secretKey, String iv) throws Exception {
        byte[] raw = secretKey.getBytes("UTF-8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv1 = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv1);
        byte[] encrypted = cipher.doFinal(src.getBytes("UTF-8"));
        return Base64.encodeBase64String(encrypted);

    }

    public CheckVersionAndTagDTO checkVersionAndTasIsExist(String imageUrl, String containerApplicationId) {
        Result result = call(ApiUrlConstants.CHECK_VERSION_TAG_EXIST  + "/" + containerApplicationId,
                RequestMethod.POST,imageUrl ,null);
        if(result.isSuccess()){
            return JSON.parseObject(result.getData(), CheckVersionAndTagDTO.class);
        }
        throw new RuntimeException(result.getMessage());
    }

    public boolean syncHarborSingleTag(String tag, String containerApplicationId,String organizationId) {
        Result result = call(ApiUrlConstants.SYNC_SINGLE_TAG + "/" + tag + "/" + containerApplicationId + "/" + organizationId,
                RequestMethod.GET);
        if(result.isSuccess()){
            return true;
        }
        throw new RuntimeException(result.getMessage());
    }

    public String saveOrUpdateApplicationVersion(SaveOrUpdateApplicationVersionDto request) {
        Result result = call(ApiUrlConstants.SAVE_OR_UPDATE_CONTAINER_APPLICATION_VERSION,
                RequestMethod.POST,request,null);
        if(!result.isSuccess()){
            throw new RuntimeException(result.getMessage());
        }
        return result.getData();
    }

    public String createContainerTaskAndRun(String workspaceId, ContainerDeployDto params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("sourceId", workspaceId);
        Result result = call(ApiUrlConstants.DEPLOY_APPLICATION_VERSION, RequestMethod.POST, params , headers);
        if(!result.isSuccess()){
            throw new RuntimeException(result.getMessage());
        }
        return result.getData();
    }

    public String selectWorkJobStatus(String workFlowJobId) {
        Result result = call(ApiUrlConstants.GET_DEPLOY_TASK_STATUS, RequestMethod.POST, workFlowJobId , null);
        if(!result.isSuccess()){
            throw new RuntimeException(result.getMessage());
        }
        return result.getData();
    }
}

class ApiUrlConstants {
    public static final String USER_INFO = "dashboard/user/info";
    public static final String APPLICATION_REPOSITORY_LIST = "devops/application/repository/list";
    public static final String APPLICATION_SETTING_LIST = "devops/application/setting/list";
    public static final String APPLICATION_SETTING_GET = "devops/application/deploy/get";
    public static final String USER_PERMISSION_LIST = "dashboard/user/switch/source";
    public static final String REPOSITORY_LIST = "devops/repository/list";
    public static final String APPLICATION_LIST = "devops/application/list";
    public static final String CLUSTER_LIST = "devops/cluster/list";
    public static final String CLUSTER_ROLE_LIST = "devops/clusterRole/list";
    public static final String SERVER_LIST = "devops/server/list";
    public static final String APPLICATION_VERSION_SAVE = "devops/application/version/save-version";
    public static final String APPLICATION_DEPLOY_SAVE = "devops/application/deploy/save";
    public static final String APPLICATION_VERSION_DEPLOY = "devops/application/version/deploy";
    public static final String APPLICATION_ENV_LIST = "devops/application/setting/env/list";
    public static final String NAMESPACE_LIST = "devops/container/resource/k8s/Namespace/getResourceByWorkspaceId";
    public static final String CONTAINER_CLUSTER_LIST = "devops/container/cluster/list";
    public static final String CONTAINER_PODS_LIST = "devops/container/pods/getPodsByClusterId";
    public static final String DOCKER_SECRET_LIST = "devops/container/resource/secret/getDockerSecret";
    public static final String CHECK_VERSION_TAG_EXIST = "devops/application/version/checkVersionAndTasIsExist";
    public static final String SYNC_SINGLE_TAG = "devops/repository/sync/single/tag";
    public static final String SAVE_OR_UPDATE_CONTAINER_APPLICATION_VERSION = "devops/application/version/saveOrUpdateContainerApplicationVersion";
    public static final String DEPLOY_APPLICATION_VERSION = "devops/application/version/container/deploy";
    public static final String GET_DEPLOY_TASK_STATUS = "devops/workJob/selectStatusById";
    public static final String GET_USER_WORKSPACE = "management-center/user/selectWorkspaceByUserId";

}

enum RequestMethod {
    GET, POST
}
