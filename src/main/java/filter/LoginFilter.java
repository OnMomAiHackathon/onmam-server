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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 모든 요청을 필터 없이 통과
        chain.doFilter(request, response);

        // 원래의 로그인 검증 코드가 있던 부분을 주석
        /*
        HttpSession session = httpRequest.getSession(false);
        String loginURI = httpRequest.getContextPath() + "/auth/login";
        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);

        boolean isGetRequest = httpRequest.getMethod().equalsIgnoreCase("GET");
        if (loggedIn || loginRequest || !isGetRequest) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(loginURI);
        }
        */
    }

    @Override
    public void destroy() {
    }
}
