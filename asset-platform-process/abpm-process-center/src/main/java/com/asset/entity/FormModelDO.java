package com.asset.entity;

import com.alibaba.fastjson.JSONObject;
import com.asset.javabean.IDGenerator;
import com.asset.javabean.UuidIdGenerator;
import com.asset.dto.FormModelEditDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FormModelDO implements Serializable {
    private String id;
    private String sceneId;
    private String formName;
    private Date createdTime;
    private String createdBy;
    private Date lastUpdatedTime;
    private String lastUpdatedBy;
    private Integer version;
    private Integer groupId;
    private String iconCls;
    private Integer status;
    private String procModelId;
    private String modelSheetStr;

    public FormModelDO() {
    }

    public FormModelDO(String id, String formName,  String createdBy, Integer version, Integer groupId, Integer status, String procModelId, String modelSheetStr) {
        this.id = id;
        this.formName = formName;
        this.createdBy = createdBy;
        this.version = version;
        this.groupId = groupId;
        this.status = status;
        this.procModelId = procModelId;
        this.modelSheetStr = modelSheetStr;
    }

    public FormModelDO(String formName,
                       String createdBy,
                       String iconCls,
                       Integer status,
                       String modelSheetStr) {
        IDGenerator generator = new UuidIdGenerator();
        id = generator.generateID();
        this.formName = formName;
        this.createdBy = createdBy;
        this.iconCls = iconCls;
        this.status = status;
        this.modelSheetStr = modelSheetStr;
    }


    public FormModelDO(FormModelEditDTO rec) {
        this.id = rec.getForm_model_id();
        this.formName = rec.getForm_name();
        this.iconCls = rec.getIcon_cls();
        String jsonString = JSONObject.toJSONString(rec.getForm_sheet());
        this.modelSheetStr = jsonString;
        this.groupId = rec.getGroup_id();
    }

    public FormModelDO(String id, String procModelId) {
        this.id = id;
        this.procModelId = procModelId;
    }

    public FormModelDO(String id, Integer groupId) {
        this.id = id;
        this.groupId = groupId;
    }

    private FormModelDO(Builder builder) {
        setId(builder.id);
        setSceneId(builder.sceneId);
        setFormName(builder.formName);
        setCreatedTime(builder.createdTime);
        setCreatedBy(builder.createdBy);
        setLastUpdatedTime(builder.lastUpdatedTime);
        setLastUpdatedBy(builder.lastUpdatedBy);
        setVersion(builder.version);
        setGroupId(builder.groupId);
        setIconCls(builder.iconCls);
        setStatus(builder.status);
        setProcModelId(builder.procModelId);
        setModelSheetStr(builder.modelSheetStr);
    }


    public static final class Builder {
        private String id;
        private String sceneId;
        private String formName;
        private Date createdTime;
        private String createdBy;
        private Date lastUpdatedTime;
        private String lastUpdatedBy;
        private Integer version;
        private Integer groupId;
        private String iconCls;
        private Integer status;
        private String procModelId;
        private String modelSheetStr;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder sceneId(String val) {
            sceneId = val;
            return this;
        }

        public Builder formName(String val) {
            formName = val;
            return this;
        }

        public Builder createdTime(Date val) {
            createdTime = val;
            return this;
        }

        public Builder createdBy(String val) {
            createdBy = val;
            return this;
        }

        public Builder lastUpdatedTime(Date val) {
            lastUpdatedTime = val;
            return this;
        }

        public Builder lastUpdatedBy(String val) {
            lastUpdatedBy = val;
            return this;
        }

        public Builder version(Integer val) {
            version = val;
            return this;
        }

        public Builder groupId(Integer val) {
            groupId = val;
            return this;
        }

        public Builder iconCls(String val) {
            iconCls = val;
            return this;
        }

        public Builder status(Integer val) {
            status = val;
            return this;
        }

        public Builder procModelId(String val) {
            procModelId = val;
            return this;
        }

        public Builder modelSheetStr(String val) {
            modelSheetStr = val;
            return this;
        }

        public FormModelDO build() {
            return new FormModelDO(this);
        }
    }
}
