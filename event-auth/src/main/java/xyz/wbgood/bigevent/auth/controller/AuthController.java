package xyz.wbgood.bigevent.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.wbgood.bigevent.auth.dto.RegDto;
import xyz.wbgood.bigevent.common.utils.Result;
import xyz.wbgood.bigevent.common.utils.ResultCode;

@Slf4j
@RestController
@RequestMapping("auth")
@Api(tags = "用户认证api")

public class AuthController {

    @PostMapping("reg/userName")
    @ApiOperation("用户名注册接口")
    @ApiImplicitParam(name = "regDto", value = "用户名注册信息", required = true)
    public Result regUserName(@RequestBody RegDto regDto) {
        return Result.ok(ResultCode.OK, "用户名注册成功!", regDto);
    }
}
