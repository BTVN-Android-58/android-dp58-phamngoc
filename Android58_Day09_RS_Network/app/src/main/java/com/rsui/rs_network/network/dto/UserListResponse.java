package com.rsui.rs_network.network.dto;

import com.google.gson.annotations.SerializedName;
import com.rsui.rs_network.model.User;

import java.util.List;

public class UserListResponse extends BaseResponse<List<User>> {
    @SerializedName("pagination")
    private Pagination pagination;

    // Getters and setters
}
