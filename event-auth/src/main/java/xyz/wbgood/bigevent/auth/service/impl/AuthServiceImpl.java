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
import xyz.wbgood.bigevent.auth.entity.Auth;
import xyz.wbgood.bigevent.auth.mapper.AuthMapper;
import xyz.wbgood.bigevent.auth.service.AuthService;
import xyz.wbgood.bigevent.common.utils.JwtUtil;
import xyz.wbgood.bigevent.common.utils.MacUtil;
import xyz.wbgood.bigevent.common.utils.Result;
import xyz.wbgood.bigevent.common.utils.ResultCode;

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
        return Result.ok(ResultCode.OK, "登陆成功",map);
    }


}
