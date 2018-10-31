package jwt.pratice.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanLocator implements BeanFactoryAware {
  private static SpringBeanLocator instance;

  private BeanFactory beanFactory;

  private SpringBeanLocator() {
    super();
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
    SpringBeanLocator.getInstance().beanFactory = beanFactory;
  }

  public static SpringBeanLocator getInstance() {
    if (null == instance) {
      instance = new SpringBeanLocator();
    }
    return instance;
  }

  public Object getBean(String beanName) {
    return beanFactory.getBean(beanName);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Object getBean(Class beanName) {
    return beanFactory.getBean(beanName);
  }

}
