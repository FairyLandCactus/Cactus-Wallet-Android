package com.qianlihu.solanawallet.mvp.join_room;

/**
 * author : Duan
 * date   : 2022/12/3014:00
 * desc   :
 * version: 1.0.0
 */
public class JoinRoomBean {

    private RecordBean record;

    public RecordBean getRecord() {
        return record;
    }

    public void setRecord(RecordBean record) {
        this.record = record;
    }

    public static class RecordBean {
        /**
         * address : 2
         * user_name : 2
         * user_img : 2
         * room_p_id : 123
         * taboo_status : 1
         * live_status : 1
         * user_type : 3
         */

        private String address;
        private String user_name;
        private String user_img;
        private String room_p_id;
        private String taboo_status;
        private String live_status;
        private int user_type;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getTaboo_status() {
            return taboo_status;
        }

        public void setTaboo_status(String taboo_status) {
            this.taboo_status = taboo_status;
        }

        public String getLive_status() {
            return live_status;
        }

        public void setLive_status(String live_status) {
            this.live_status = live_status;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }
    }
}
