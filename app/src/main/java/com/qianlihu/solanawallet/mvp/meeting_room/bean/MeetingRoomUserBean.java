package com.qianlihu.solanawallet.mvp.meeting_room.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/12/2314:11
 * desc   :
 * version: 1.0.0
 */
public class MeetingRoomUserBean {


    private List<AnchorBean> anchor;
    private List<UserBean> user;

    public List<AnchorBean> getAnchor() {
        return anchor;
    }

    public void setAnchor(List<AnchorBean> anchor) {
        this.anchor = anchor;
    }

    public List<UserBean> getUser() {
        return user;
    }

    public void setUser(List<UserBean> user) {
        this.user = user;
    }

    public static class AnchorBean {

        private int id;
        private String address;
        private String room_id;
        private String room_name;
        private int room_man_num;
        private String user_name;
        private String user_img;
        private String room_p_id;
        private int user_type;
        private String ctime;
        private String end_time;
        private int status;
        private int taboo_status;
        private int live_status;
        private String updated_at;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public int getRoom_man_num() {
            return room_man_num;
        }

        public void setRoom_man_num(int room_man_num) {
            this.room_man_num = room_man_num;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getRoom_p_id() {
            return room_p_id;
        }

        public void setRoom_p_id(String room_p_id) {
            this.room_p_id = room_p_id;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTaboo_status() {
            return taboo_status;
        }

        public void setTaboo_status(int taboo_status) {
            this.taboo_status = taboo_status;
        }

        public int getLive_status() {
            return live_status;
        }

        public void setLive_status(int live_status) {
            this.live_status = live_status;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

    public static class UserBean {

        private int id;
        private String address;
        private String room_id;
        private String room_name;
        private int room_man_num;
        private String user_name;
        private String user_img;
        private String room_p_id;
        private int user_type;
        private String ctime;
        private String end_time;
        private int status;
        private int taboo_status;
        private int live_status;
        private String updated_at;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public int getRoom_man_num() {
            return room_man_num;
        }

        public void setRoom_man_num(int room_man_num) {
            this.room_man_num = room_man_num;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getRoom_p_id() {
            return room_p_id;
        }

        public void setRoom_p_id(String room_p_id) {
            this.room_p_id = room_p_id;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTaboo_status() {
            return taboo_status;
        }

        public void setTaboo_status(int taboo_status) {
            this.taboo_status = taboo_status;
        }

        public int getLive_status() {
            return live_status;
        }

        public void setLive_status(int live_status) {
            this.live_status = live_status;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
