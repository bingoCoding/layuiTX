package com.bingo.websocket.domain;

import java.io.Serializable;

public class Domain implements Serializable {

    /**
     * @description 添加群信息
     */
    public class Group implements Serializable{
        private Integer groupId;
        private String remark;

        public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    /**
     * @description 同意添加好友
     */
    public class AgreeAddGroup implements Serializable{
        private Integer toUid;
        private Integer groupId;
        private Integer messageBoxId;

        public Integer getToUid() {
            return toUid;
        }

        public void setToUid(Integer toUid) {
            this.toUid = toUid;
        }

        public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        public Integer getMessageBoxId() {
            return messageBoxId;
        }

        public void setMessageBoxId(Integer messageBoxId) {
            this.messageBoxId = messageBoxId;
        }
    }
}
