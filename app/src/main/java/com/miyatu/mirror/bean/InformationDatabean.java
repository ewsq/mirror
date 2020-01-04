package com.miyatu.mirror.bean;

import java.util.List;

/**
 * Created by yhd on 2019/11/22.
 * Email 1443160259@qq.com
 */

//消息列表bean
public class InformationDatabean {

    /**
     * status : 1
     * msg : 获取成功
     * data : [{"id":1,"cover":"/tmp/uploads/20191218/fade821f1f32cb11f3e33141ea421ce1.png","title":"南京艺术学院2+2国际本科项目第二批录取名单公示","date":"2019-12-20"}]
     * extra : {"page":1,"pageSize":20,"total_page":1}
     */

    private int status;
    private String msg;
    private ExtraBean extra;
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

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class ExtraBean {
        /**
         * page : 1
         * pageSize : 20
         * total_page : 1
         */

        private int page;
        private int pageSize;
        private int total_page;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }
    }

    public static class DataBean {
        /**
         * id : 1
         * cover : /tmp/uploads/20191218/fade821f1f32cb11f3e33141ea421ce1.png
         * title : 南京艺术学院2+2国际本科项目第二批录取名单公示
         * date : 2019-12-20
         */

        private int id;
        private String cover;
        private String title;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
