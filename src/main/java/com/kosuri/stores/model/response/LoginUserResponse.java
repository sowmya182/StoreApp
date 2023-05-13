package com.kosuri.stores.model.response;

import java.util.List;

public class LoginUserResponse extends GenericResponse {
    private String roleName;
    private String roleId;

    private List<String> storeId;

    public List<String> getStoreId() {
        return storeId;
    }

    public void setStoreId(List<String> storeId) {
        this.storeId = storeId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
