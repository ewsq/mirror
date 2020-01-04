package com.miyatu.mirror.bean;

public class InformationDetailBean {

    /**
     * status : 1
     * msg : 查询成功
     * data : {"id":1,"cover":"/tmp/uploads/20191218/fade821f1f32cb11f3e33141ea421ce1.png","title":"南京艺术学院2+2国际本科项目第二批录取名单公示","content":"<p>阿达帕金佛山地哈佛爱睡觉的法律框架和<br/><\/p>","date":"2019-12-20"}
     */

    private int status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * cover : /tmp/uploads/20191218/fade821f1f32cb11f3e33141ea421ce1.png
         * title : 南京艺术学院2+2国际本科项目第二批录取名单公示
         * content : <p>阿达帕金佛山地哈佛爱睡觉的法律框架和<br/></p>
         * date : 2019-12-20
         */

        private int id;
        private String cover;
        private String title;
        private String content;
        private String date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
