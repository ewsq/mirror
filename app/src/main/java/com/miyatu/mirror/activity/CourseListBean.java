package com.miyatu.mirror.activity;

import java.util.List;

public class CourseListBean {

    /**
     * status : 1
     * msg : 查询成功
     * data : [{"id":1,"title":"帮拍教程","cover":"/tmp/uploads/20191218/ddff723a0ba6f5752ef9bf3fa8ccb3ad.png","simple":"第一次使用拍照量体的用户，请务必先观看此教程短片，再进行拍照","video":"/public/tmp/uploads/20191223/703128017b380f22829529147ecee5b5.mp4"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 帮拍教程
         * cover : /tmp/uploads/20191218/ddff723a0ba6f5752ef9bf3fa8ccb3ad.png
         * simple : 第一次使用拍照量体的用户，请务必先观看此教程短片，再进行拍照
         * video : /public/tmp/uploads/20191223/703128017b380f22829529147ecee5b5.mp4
         */

        private int id;
        private String title;
        private String cover;
        private String simple;
        private String video;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getSimple() {
            return simple;
        }

        public void setSimple(String simple) {
            this.simple = simple;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }
}
