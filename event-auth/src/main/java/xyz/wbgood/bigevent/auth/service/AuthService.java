package xyz.wbgood.bigevent.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import xyz.wbgood.bigevent.auth.dto.RegDto;
import xyz.wbgood.bigevent.auth.entity.Auth;
import xyz.wbgood.bigevent.common.utils.Result;


public interface AuthService extends IService<Auth> {
    /**
     * 用户名注册
     * @param regDto
     * @return
     */
    Result regUserName(RegDto regDto);

    /**
     * 用户名dengl
     * @param regDto
     * @return
     */
    Result loginUserName(RegDto regDto);

    /**
     * 查找用户信息
     * @return
     */
    Result getUserinfo();

    /**
     * 更改用户信息
     * @param regDto
     * @return
     */
    Result updateUserinfo(RegDto regDto);
}
