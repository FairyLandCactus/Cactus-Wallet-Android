package com.qianlihu.solanawallet.mvp.create_room.bean;

/**
 * author : Duan
 * date   : 2022/12/2315:24
 * desc   :
 * version: 1.0.0
 */
public class CreateRoomBean {

    private RecordBean record;

    public RecordBean getRecord() {
        return record;
    }

    public void setRecord(RecordBean record) {
        this.record = record;
    }

    public static class RecordBean {
        /**
         * address : 11
         * room_id : 111
         * room_name : 1
         * room_man_num : 1
         * user_name : 1
         * user_img : 1
         * user_type : 1
         * updated_at : 2022-12-23 15:22:35
         * created_at : 2022-12-23 15:22:35
         * id : 4
         */

        private String address;
        private String room_id;
        private String room_name;
        private int room_man_num;
        private String user_name;
        private String user_img;
        private int user_type;
        private String updated_at;
        private String created_at;
        private int id;

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

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
