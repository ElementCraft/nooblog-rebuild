package com.noobug.NooblogRebuild.security.jwt;

import com.noobug.NooblogRebuild.domain.User;
import com.noobug.NooblogRebuild.repository.UserRepository;
import com.noobug.NooblogRebuild.tools.utils.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuthFilter implements WebFilter {

    @Autowired
    private ConfigUtil configUtil;

    @Autowired
    private UserRepository userRepository;

    private ServerWebExchangeMatcher authMatcher = ServerWebExchangeMatchers.pathMatchers( "/api/user/info", "/api/user/col1", "/api/user/col2", "/api/user/column", "/api/article", "/api/user/article", "/api/user/article/**", "/api/admin/**");
    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        // 过滤不需要鉴权的地址 然后对需要鉴权的地址做jwt token转换， 然后验证。
        return authMatcher.matches(serverWebExchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(webFilterChain.filter(serverWebExchange).then(Mono.empty()))
                .flatMap(matchResult -> this.convert(serverWebExchange))
                .flatMap(authentication -> this.auth(serverWebExchange, webFilterChain, authentication));
    }

    private Mono<Void> auth(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain, Authentication authentication) {
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);

        String account = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();


        // 验证用户帐号密码
        Optional<User> user = userRepository.findByAccountAndPasswordAndBannedAndDeleted(account, password, Boolean.FALSE, Boolean.FALSE);
        if (!user.isPresent()) {
            return Mono.empty();
        }

        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        return this.securityContextRepository.save(serverWebExchange, securityContext)
                .then(webFilterChain.filter(serverWebExchange))
                .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));

    }

    private Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization == null || authorization.isEmpty()) {
            return Mono.empty();
        }

        String credentials = authorization.length() <= JwtConst.BEARER.length() ?
                "" : authorization.substring(JwtConst.BEARER.length(), authorization.length());
        SignedJWT signedJWT;
        String secret = configUtil.get(JwtConst.SECRET_KEY, JwtConst.SECRET);
        String subject;
        String password;
        String auths;
        // 权限集合
        Collection<? extends GrantedAuthority> authorities;

        try {
            signedJWT = SignedJWT.parse(credentials);
            signedJWT.verify(new MACVerifier(secret));

            subject = signedJWT.getJWTClaimsSet().getSubject();
            auths = (String) signedJWT.getJWTClaimsSet().getClaim(JwtConst.AUTHORITIES_CLAIM_KEY);
            password = (String) signedJWT.getJWTClaimsSet().getClaim(JwtConst.PASSWORD_CLAIM_KEY);

            // 权限字符串拆分
            authorities = Optional.ofNullable(auths)
                    .map(o -> Stream.of(auths.split(JwtConst.AUTHORITIES_CLAIM_DELIMITER))
                            .filter(str -> str != null && !str.isEmpty())
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
                    )
                    .orElse(new ArrayList<>());

        } catch (ParseException e) {
            return Mono.empty();
        } catch (JOSEException e) {
            return Mono.empty();
        }

        return Mono.just(new UsernamePasswordAuthenticationToken(subject, password, authorities));
    }
}


