package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/auth/login";
        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);

        boolean isGetRequest = httpRequest.getMethod().equalsIgnoreCase("GET");
        if (loggedIn || loginRequest || !isGetRequest) {
            // 이미 로그인되어 있거나 로그인 요청일 경우 또는 GET 요청이 아닌 경우 필터 체인 진행
            chain.doFilter(request, response);
        } else {
            // 로그인되지 않은 사용자가 GET 요청을 보낼 경우 로그인 페이지로 리다이렉트
            httpResponse.sendRedirect(loginURI);
        }

    }

    @Override
    public void destroy() {
    }
}
