package xyz.wbgood.bigevent.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.wbgood.bigevent.auth.controller.BaseExceptionHandler;
import xyz.wbgood.bigevent.auth.dto.RegDto;
import xyz.wbgood.bigevent.auth.dto.UserInfoDto;
import xyz.wbgood.bigevent.auth.entity.Auth;
import xyz.wbgood.bigevent.auth.mapper.AuthMapper;
import xyz.wbgood.bigevent.auth.service.AuthService;
import xyz.wbgood.bigevent.common.utils.JwtUtil;
import xyz.wbgood.bigevent.common.utils.MacUtil;
import xyz.wbgood.bigevent.common.utils.Result;
import xyz.wbgood.bigevent.common.utils.ResultCode;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    // jwt校验密钥
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * 用户名注册
     *
     * @param regDto
     * @return
     */
    @Override
    public Result regUserName(RegDto regDto) {
        // 如果用户名存在则无法注册
        LambdaQueryWrapper<Auth> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Auth::getUserName, regDto.getUserName());
        Integer count = authMapper.selectCount(wrapper);
        if (count > 0) {
            return Result.error("用户名已存在");
        }
        String password = MacUtil.makeHashPassword(regDto.getPassword());
        Auth auth = Auth.builder()
                .status(1)
                .userName(regDto.getUserName())
                .password(password)
                .createDate(new Date())
                .lastDate(new Date())
                .build();
        int insertCount = authMapper.insert(auth);
        // 插入失败
        if (insertCount <= 0) {
            return Result.error("注册失败");
        }
        return Result.ok(ResultCode.OK, "注册成功", null);
    }

    /**
     * 用户名登录
     *
     * @param regDto
     * @return
     */
    @Override
    public Result loginUserName(RegDto regDto) {
        //获取用户信息
        LambdaQueryWrapper<Auth> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Auth::getUserName, regDto.getUserName());
        Auth auth = getOne(wrapper);
        if (auth == null) {
            return Result.error("用户不存在");
        }

        // 用户存在比较密码
        String password = MacUtil.makeHashPassword(regDto.getPassword());

        if (!auth.getPassword().equals(password)) {
            return Result.error("密码错误");
        }

        String token = JwtUtil.createJWT(auth.getId(), jwtSecret);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        // 用户存在 密码比对成功
        return Result.ok(ResultCode.OK, "登陆成功", map);
    }

    @Autowired
    private HttpServletRequest request;

    /**
     * 查找用户信息
     *
     * @return
     */
    @Override
    public Result getUserinfo() {
        String authId = request.getHeader("authId");
        Auth auth = getById(authId);
        // 用户不存在
        if (auth == null) return Result.error(ResultCode.NOT_FOUND, "查找失败");

        return Result.ok(ResultCode.OK, "查询成功", auth);
    }

    @Override
    public Result updateUserinfo(UserInfoDto userInfoDto) {
        if (userInfoDto == null) return Result.error("修改数据不能为空");

        String authId = request.getHeader("authId");
        String newPassword = null;

        //更新密码
        if (StringUtils.isNotBlank(userInfoDto.getPassword())) {
            //判断两次密码是否一致
            if (!userInfoDto.getNewPassword().equals(userInfoDto.getCnewPassword())) {
                return Result.error("两次密码不一致");
            }
            // 获取用户信息
            Auth selectAuth = getById(authId);
            // 加密原密码
             newPassword = MacUtil.makeHashPassword(userInfoDto.getPassword());

            // 判断原密码是否正确
            if (!newPassword.equals(selectAuth.getPassword())) {
                return Result.error("原密码错误");
            }
            // 加密原密码
            newPassword = MacUtil.makeHashPassword(userInfoDto.getNewPassword());

        }
        Auth auth = new Auth();
        BeanUtils.copyProperties(userInfoDto,auth);
        auth.setPassword(newPassword);
        auth.setId(authId);

        if (!updateById(auth)) {
            return Result.error("更新失败");
        }
        return Result.ok("更新成功", null);
    }


}
