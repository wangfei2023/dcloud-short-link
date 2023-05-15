package net.xdclass.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.xdclass.model.LoginUser;

import java.util.Date;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/15 0015 22:39
 * @Version: 1.0
 * @Description:
 */

public class JWTUtil {
  //主题
    private static  final String SUBJECT="xdclass";
    //加密秘钥
    private static  final String SECRET="xdclass.net";
    //令牌前缀
    private static  final String TOKEN_PREFIX ="dcloud_link";
    //token过期时间;
    private static  final long EXPIRE = 1000*60*60*24*7;

    //项目中封装生产token方法
    public static  String genJsonWebToken(LoginUser loginUser){
        if (loginUser==null){
            throw new NullPointerException("对象为空");
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("head_img", loginUser.getHeadImg())
                .claim("id", loginUser)
                .claim("name", loginUser.getUsername())
                .claim("phone", loginUser.getPhone())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
       token = TOKEN_PREFIX + token;
       return token;

    }
    //封装校验token方法
    public static Claims checkJWT(String token) {

        try {

            final Claims claims = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();

            return claims;

        } catch (Exception e) {
            return null;
        }
    }
}