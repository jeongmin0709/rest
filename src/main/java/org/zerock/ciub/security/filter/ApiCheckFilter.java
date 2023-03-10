package org.zerock.ciub.security.filter;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.ciub.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    // 특정 path에만 동작하도록 하기위한 필드
    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("RQUESTIRI: " + request.getRequestURI());

        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        if(antPathMatcher.match(pattern, request.getRequestURI())){
            log.info("ApiCheckFiler..................................................");
            log.info("ApiCheckFiler..................................................");
            log.info("ApiCheckFiler..................................................");
            boolean checkHeader = checkAuthHeader(request);
            if(checkHeader){
                filterChain.doFilter(request, response);
                return;
            }else{
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴 및 한글꺠짐 수정
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code","403");
                json.put("message",message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }
        filterChain.doFilter(request, response); // 다음 필터 단계로 넘어가는 역할
    }
    private boolean checkAuthHeader(HttpServletRequest request){
        boolean checkResult = false;
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            try {
                log.info(authHeader.substring(7));
                String emil = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result: " + emil);
                checkResult = emil.length() > 0;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return checkResult;
    }
}
