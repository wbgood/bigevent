package xyz.wbgood.bigevent.common.utils;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtil {
    /**
     * 签发JWT
     *
     * @param authId 认证信息Id
     * @param secret
     * @return
     * @throws
     */
    public static String createJWT(String authId, String secret) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        SecretKey secretKey = generalKey(secret);

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = LocalDateTime.now().atZone(zoneId);
        //获取第三天的凌晨3点
        ZonedDateTime zdtEnd = zdt.truncatedTo(ChronoUnit.DAYS).plusDays(3).plusHours(3);

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setIssuedAt(Date.from(zdt.toInstant())) //签发时间
                .setHeaderParam("alg", "HS256")  //加密算法
                .claim("authId", authId) //认证信息ID
                .setExpiration(Date.from(zdtEnd.toInstant()))  //设置过期时间
                .signWith(signatureAlgorithm, secretKey);  //用密钥签名

        //生成JWT
        return builder.compact();
    }

    /**
     * 验证jwt
     *
     * @param token  校验的token
     * @param secret
     * @return
     */
    public static VerifyResult verifyJwt(String token, String secret) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generalKey(secret);
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token) //注意不要使用方法错误
                    .getBody();

            //校验成功，获取认证信息
            String authId = (String) claims.get("authId");
            return new VerifyResult(true, "校验成功", authId);

        } catch (ExpiredJwtException e) {
            return new VerifyResult(false, "token过期", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new VerifyResult(false, "token无效", null);
        }
    }

    /**
     * 生成key
     *
     * @param secret
     * @return
     */
    private static SecretKey generalKey(String secret) {
        byte[] encodedKey = Base64.decodeBase64(secret);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        return key;
    }

    @Getter
    @AllArgsConstructor
    public static class VerifyResult {
        //是否校验成功
        private boolean isValidate;

        //校验结果信息
        private String msg;

        //认证信息主键
        private String authId;

    }
}