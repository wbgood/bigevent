package xyz.wbgood.bigevent.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserInfoDto对象", description = "修改用户信息的dto")
public class UserInfoDto {
    @ApiModelProperty(value = "原密码",example = "xxxxxx")
    private String password;

    @ApiModelProperty(value = "新密码",example = "xxxxxx")
    private String newPassword;

    @ApiModelProperty(value = "确认密码",example = "xxxxxx")
    private String cnewPassword;

    // 用户昵称
    @ApiModelProperty(value = "用户昵称",example = "张三")
    private String nickname;
    // 用户头像
    @ApiModelProperty(value = "用户头像")
    private String userPic;
}
