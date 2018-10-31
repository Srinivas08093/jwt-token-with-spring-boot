package jwt.pratice.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import jwt.pratice.common.JwtPropertyCOnfigReader;
import jwt.pratice.common.SpringBeanLocator;
import jwt.pratice.constants.MyConstants;
import jwt.pratice.utility.JwtTokenUtility;

@Configuration
@WebFilter(urlPatterns = {"/*"}, filterName = "ApplicationFilter")
public class ApplicationFilter implements Filter {
  private static final Logger logger = Logger.getLogger(ApplicationFilter.class);

  private static List<String> unsecuredUrls;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
	  
	  System.out.println("****************************************************8");
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    boolean continueChain = false;
    try {
      String contextPath = httpRequest.getContextPath();
      String authTokenId = httpRequest.getHeader(MyConstants.JWT_TOKEN_HEADER_PARAM);
      String headerUserId = httpRequest.getHeader(MyConstants.JWT_USER_HEADER_PARAM);
      String requestURI = httpRequest.getRequestURI();
      String reqResource = StringUtils.replace(requestURI, contextPath, "", 1);
      logger.debug("Requested URI = " + reqResource + " auth token id  = " + authTokenId + " contextPath = "
          + contextPath + " userPassed = " + headerUserId);

      System.out.println("***************************************************");
      System.out.println(getUnsecuredUrls());
      System.out.println("***************************************************");
      if (!getUnsecuredUrls().contains(reqResource)) {
        String userId = JwtTokenUtility.getUserFromToken(authTokenId);
        if (StringUtils.isNotBlank(userId) && StringUtils.equalsIgnoreCase(headerUserId, userId)) {
          continueChain = true;
        }
      } else {
        continueChain = true;
      }

      if (continueChain) {
        chain.doFilter(request, response);
      } else {
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      }
    } catch (Exception e) {
      logger.error("error in filter", e);
      e.printStackTrace();
      httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

  }

  @Override
  public void destroy() {
    // TODO Auto-generated method stub

  }


  public static List<String> getUnsecuredUrls() {
    if (null == unsecuredUrls) {
      JwtPropertyCOnfigReader propMgr = (JwtPropertyCOnfigReader) SpringBeanLocator.getInstance().getBean(JwtPropertyCOnfigReader.class);
      String allUnsecuredUrls = propMgr.getFilePropertyValue(MyConstants.UNSECURED_URLS);
      unsecuredUrls = Arrays.asList(allUnsecuredUrls.split(","));
    }
    return unsecuredUrls;
  }

}
