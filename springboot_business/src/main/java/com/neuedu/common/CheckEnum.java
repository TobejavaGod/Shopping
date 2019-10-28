package com.neuedu.common;

/**
 * 购物车商品是否选中枚举类
 * @author jyw
 * @date 2019/10/27-14:41
 */
public enum CheckEnum {
    PRODUCT_CHECKED(1,"已选中"),
    PRODUCT_UNCHECK(0,"未选中")
    ;
    private int status;
    private String desc;

    CheckEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
