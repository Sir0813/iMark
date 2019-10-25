package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * @author cui
 * @date 2019-09-26
 */
public class App {

    private Integer appId;

    private String appName;

    private String appContent;

    private String appVersion;

    private String appSize;

    private String appReserve;

    private String appPlatform;

    private Integer appVersionCode;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date appUpdateTime;

    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getAppContent() {
        return appContent;
    }

    public void setAppContent(String appContent) {
        this.appContent = appContent == null ? null : appContent.trim();
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize == null ? null : appSize.trim();
    }

    public String getAppReserve() {
        return appReserve;
    }

    public void setAppReserve(String appReserve) {
        this.appReserve = appReserve == null ? null : appReserve.trim();
    }

    public String getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(String appPlatform) {
        this.appPlatform = appPlatform == null ? null : appPlatform.trim();
    }

    public Date getAppUpdateTime() {
        return appUpdateTime;
    }

    public void setAppUpdateTime(Date appUpdateTime) {
        this.appUpdateTime = appUpdateTime;
    }
}