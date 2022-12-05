package xyz.wbgood.bigevent.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 认证信息实体类
 * </p>
 */
@Data
@Accessors(chain = true)
@TableName("tb_auth")
@AllArgsConstructor
@Builder
public class Auth implements Serializable {
    private static final long serialVersionUID = 1L;

    public Auth() {

    }
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String userName;

    @JsonIgnore
    private String password;

    private int status;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mobileBindDate;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date emailBindDate;

    private String qq;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date qqBindDate;

    private String weixin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date weixinBindDate;

    private String weibo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date weiboBindDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastDate;
}