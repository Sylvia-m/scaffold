package com.cms.scaffold.route.operate.config;

import com.cms.scaffold.route.operate.shiro.MyShiroRealm;
import com.cms.scaffold.sys.sys.domain.SysMenu;
import com.cms.scaffold.sys.sys.service.SysMenuService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zjh on 2018/2/8. Apache Shiro 核心通过 Filter 来实现，就好像SpringMvc 通过DispachServlet 来主控制一样。
 * 既然是使用 Filter 一般也就能猜到，是通过URL规则来进行过滤和权限校验，所以我们需要定义一系列关于URL的规则和访问权限。
 */
@Configuration
@ConditionalOnMissingBean(name = "sysMenuService")
// 这个注解是为了保证flywaydb执行在ShiroConfiguration查询数据库之前
@DependsOn("flywayInitializer")
@Component
public class ShiroConfiguration {

  @Autowired SysMenuService sysMenuService;

  @Value("${server.session.timeout:30000}")
  Long sessionTimeOut;

  /** 默认premission字符串 */
  public static final String PREMISSION_STRING = "perms[\"{0}\"]";

  /** 系统环境 */
  @Value("${spring.profiles.active:prod}")
  private String profile;

  @Autowired
  SessionDAO sessionDAO;

  /**
   * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
   * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
   * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
   */
  @Bean
  public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public DefaultWebSessionManager sessionManager() {
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    sessionManager.setSessionDAO(sessionDAO);
    sessionManager.setGlobalSessionTimeout(sessionTimeOut * 1000L);
    Cookie cookie = sessionManager.getSessionIdCookie();
    cookie.setName(profile + "SessionId");
    return sessionManager;
  }

  /** HashedCredentialsMatcher，这个类是为了对密码进行编码的， 防止密码在数据库里明码保存，当然在登陆认证的时候， 这个类也负责对form里输入的密码进行编码。 */
  public Md5CredentialsMatcher md5CredentialsMatcher() {
    return new Md5CredentialsMatcher();
  }

  @Bean
  public MyShiroRealm myShiroRealm() {
    MyShiroRealm myShiroRealm = new MyShiroRealm();
    myShiroRealm.setCredentialsMatcher(md5CredentialsMatcher());
    return myShiroRealm;
  }

  /** SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。 */
  @Bean
  public DefaultWebSecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(myShiroRealm());
    securityManager.setSessionManager(sessionManager());
    return securityManager;
  }

  /**
   * 安全认证过滤器 主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
   *
   * @return
   */
  @Bean
  public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager());

    Map<String, Filter> filters = new LinkedHashMap<>();
    LogoutFilter logoutFilter = new LogoutFilter();
    logoutFilter.setRedirectUrl("/home");
    shiroFilterFactoryBean.setFilters(filters);

    Map<String, String> filterChainDefinitionManager = new LinkedHashMap<String, String>();
    List<SysMenu> menuList = sysMenuService.findAll();

    if (menuList != null && !menuList.isEmpty()) {
      for (SysMenu menu : menuList) {
        if (!StringUtils.isEmpty(menu.getUrl())) {
          if (menu.getUrl().indexOf("/") == 0) {
            filterChainDefinitionManager.put(
                menu.getUrl(),
                MessageFormat.format(
                    PREMISSION_STRING, menu.getUrl().replaceFirst("/", "").replaceAll("/", ":")));
          } else {
            filterChainDefinitionManager.put(
                menu.getUrl(),
                MessageFormat.format(PREMISSION_STRING, menu.getUrl().replaceAll("/", ":")));
          }
        }
      }
    }
    filterChainDefinitionManager.put("/login/check", "anon");
    filterChainDefinitionManager.put("/logout", "logout");
    filterChainDefinitionManager.put("/login", "anon");
    filterChainDefinitionManager.put("/dingLogin/check", "anon");
    filterChainDefinitionManager.put("/static/**", "anon");
    filterChainDefinitionManager.put("/notify/**", "anon");
    filterChainDefinitionManager.put("/lang/**", "anon");

    filterChainDefinitionManager.put("/*/login/check", "anon");
    filterChainDefinitionManager.put("/*/logout", "logout");
    filterChainDefinitionManager.put("/*/login", "anon");
    filterChainDefinitionManager.put("/*/dingLogin/check", "anon");
    filterChainDefinitionManager.put("/*/static/**", "anon");
    filterChainDefinitionManager.put("/*/notify/**", "anon");
    filterChainDefinitionManager.put("/*/lang/**", "anon");

    filterChainDefinitionManager.put("/**", "user");

    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);

    shiroFilterFactoryBean.setLoginUrl("/login");
    shiroFilterFactoryBean.setSuccessUrl("/index");
    shiroFilterFactoryBean.setUnauthorizedUrl("/403");
    return shiroFilterFactoryBean;
  }

  /**
   * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
   *
   * @param securityManager
   * @return
   */
  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
      SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
        new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }
}
