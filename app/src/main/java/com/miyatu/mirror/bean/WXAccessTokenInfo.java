package com.miyatu.mirror.bean;

/**
 * create by: wangchao
 * 邮箱: 1064717519@qq.com 
 */
public class WXAccessTokenInfo {

    /**
     * access_token : 27_GR7zjvyAFvAaHimriWr-jZB6xF3Ho1NU3WNGN_ooVvJQAhhaEFmquGNcZVrAmQ7qhE6Zzvf3z2tsvdMffWaFqOlxixslrjCWWBTHWxdJv30
     * expires_in : 7200
     * refresh_token : 27_J8B6DYdLfBZomRL87zepAqmbazqK4OBe-blcq9Do4pyISSmjk0QpbzFEqKL8IZQJRBC28mkwqeWDH32Cik59wMrvi9_i7kt---MgbH8xR3E
     * openid : ov9NovzocsKrnTaXWENVbMA6vkfA
     * scope : snsapi_userinfo
     * unionid : oYyl7uDqiTF_CZhFiOw8FKMRmRKw
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
