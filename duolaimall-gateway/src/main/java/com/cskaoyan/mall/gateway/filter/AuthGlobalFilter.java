package com.cskaoyan.mall.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.cskaoyan.mall.common.constant.ResultCodeEnum;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.common.util.IpUtil;
import com.cskaoyan.mall.user.dto.UserLoginInfoDTO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import com.cskaoyan.mall.user.consts.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    RedissonClient redissonClient;
    @Value("${authUrls.url}")
    String authUrl;

    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        //获取url
        String path = request.getURI().getPath();
        //如果是内部接口，则拦截不允许访问
        if (matcher.match("/**/inner/**",path)){
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.PERMISSION);
        }

        //从请求中获取cookie中的token字符串，并根据token字符串的内容获取redis中的登录信息
        String userId = getUserId(request);
    }

    public String getUserId(ServerHttpRequest request){
        String tokenStr = getInfoByName(request,UserConstants.USER_LOGIN_TOKEN_HEADER);
        if (tokenStr.isEmpty()){
            //没有获取到token字符串
            return "";
        }
        //此时获取到了token字符串
        RBucket<UserLoginInfoDTO> bucket = redissonClient.getBucket(UserConstants.USER_LOGIN_KEY_PREFIX + tokenStr);
        if (bucket==null){
            //没有获取到用户信息
            return "";
        }
        UserLoginInfoDTO userLoginInfoDTO = bucket.get();
        //判断用户登录ip和当时请求的ip是否相等
        String gatwayIpAddress = IpUtil.getGatwayIpAddress(request);
        if (!gatwayIpAddress.equals(userLoginInfoDTO.getIp())){
            return "-1";
        }
        //返回用户id
        return userLoginInfoDTO.getUserId();
    }

    private String getInfoByName(ServerHttpRequest request, String name) {
        //从请求头中获取
        HttpHeaders headers = request.getHeaders();
        List<String> headersValues = headers.get(name);
        if (headersValues!=null&&headersValues.size()>0){
            return headersValues.get(0);
        }

        //从cookie中获取
        //获取所有的cookie
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        //获取指定的cookie
        List<HttpCookie> httpCookies = cookies.get(name);
        if (httpCookies==null||httpCookies.size()==0){
            //cookie中没有数据
            return "";
        }
        return httpCookies.get(0).getValue();

    }

    //接口鉴权失败返回数据
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        //返回用户没有权限登录
        Result<Object> result = Result.build(null, resultCodeEnum);
        //将result对象转换为json字符串，并将字符串转化为字节数据
        byte[] bytes = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        //封装一个字节数据为一个DataBuffer消息体数据
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        //输出到页面
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
