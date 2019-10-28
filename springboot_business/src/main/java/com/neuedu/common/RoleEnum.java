package com.neuedu.common;

/**
 * 用户角色枚举类
 * @author jyw
 * @date 2019/10/24-9:03
 */
public enum RoleEnum {
    ROLE_USER(1,"普通用户"),
    ROLE_ADMIN(0,"管理员");
    ;

    private int role;
    private String desc;
    RoleEnum(int role,String desc){
        this.role = role;
        this.desc = desc;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
