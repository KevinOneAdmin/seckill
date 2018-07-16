# 秒杀模拟

## 项目背景
&nbsp;&nbsp;&nbsp;&nbsp;本想项目是一个模拟秒杀系用于模拟到大并发情况下的开发,讨论在并发情况下一些问题的处理方案。
## 使用技术
  - Spring-Boot 1.5.8.RELEASE
  - Mybatis-Spring-Boot 1.3.1
  - Druid  1.0.29
  - MySql  5.6.39
  - Redis  3.2.100
  - Fastjson 1.2.47
  - Spring-Boot-Web 1.5.8.RELEASE
  - Spring-Boot-Thymeleaf 1.5.8.RELEASE
  - Spring-Boot-Validation 1.5.8.RELEASE
  - RabbitMq 3.7.5
  
## 具体实现


### 功能结构

**登录**
![登录](https://i.imgur.com/A3FchFz.png)
**秒杀商品列表**
![秒杀列表](https://i.imgur.com/RvqVvds.png)
**秒杀详情**
![秒杀详情](https://i.imgur.com/k6ukgyz.png)
**订单详情**
![订单详情](https://i.imgur.com/dZFW1uM.png)    

### 总流程
![总流程](https://i.imgur.com/4s0iEfw.png)

### 秒杀流程
![秒杀流程](https://i.imgur.com/TDvJD4g.png)


### 代码实现详解
#### 页面
&nbsp;&nbsp;&nbsp;&nbsp;页面纯静态化便于静态服务器缓存,页面都已纯HTML的形式存在。图片、CSS、JS都使用CDN存储,并且CS和js使用压缩格式,图片在用多个小图片是吧他们合并成一个大图用定位的方式引用。
![页面优化](https://i.imgur.com/FaWxaTB.png)

#### 基础校验
&nbsp;&nbsp;&nbsp;&nbsp;在业务逻辑中有很多地方需要进行参数校验，理论上每个接口的传入参数都是需要校验的。我们不可能在每个接口里都写上校验语句，这样没有效率且没有太多的功用。这方面其实jave有相应的技术，即jsr-303技术。这个技术是参考hibernate-validator制定的标准，我们这里用的Spring-Boot-validator其实是hibernate-validator与Spring-Boot的整合版。它的用法和hibernate-validator完全一样
使用注解在Bean标明校验的要求即可。hibernate-validator虽然好用，但有时它的注解是不能满足我们的要求是我们还是要自己去实现一些。那我们应该怎么实现呢？

	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Constraint(validatedBy = {IsMobileValidator.class}) //制定校验器这个是关键
	public @interface IsMobile {
	    boolean required() default true;
	    String message() default "手机号码格式有误";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
	}

	
	public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
        //实现ConstraintValidator接口后重写两个方法即可
	    private boolean required = false;
	    @Override
	    public void initialize(IsMobile isMobile) {
	        required = isMobile.required();
	    }
	    @Override
	    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
	        if(required){
	            return ValidatorUtil.isMobile(value);
	        }else {
	            return StringUtils.isEmpty(value)?true: ValidatorUtil.isMobile(value)?true:false;
	        }
	    }
	}

#### 秒杀设计
&nbsp;&nbsp;&nbsp;&nbsp;我们通过观察和经验可以得出在高并发的情况下,最终系统的压力都会集中到数据库。如下图所示你的应用程序可以做分布式扩展，但是数据哪怕做了分表分库在写的时候压力还是很大，大量的写操作会影响读取性能。
![数据库部署图](https://i.imgur.com/E75797F.png)

&nbsp;&nbsp;&nbsp;&nbsp;数据库是重要资源，也是我们的瓶颈所在所以我们要考虑用更高效的东西，那么这种情况下reidis等内存数据库合适来做这些事。秒杀的结果最终还是以订单为准，所以还是要用将数据生成订单。但是如果同步去做且数据量大，如果出现阻塞将会影响后面的订单生成。
所以这样我们就要引入MQ做异步解耦。

&nbsp;&nbsp;&nbsp;&nbsp;当秒杀详情页加载后如果秒杀已经开始就获取验证码，在服务端生成验码的时候会将验证码的值存储到reids中。
用户点击秒杀按钮。先将验证发送到服务端验证，验证通过后获取获取到秒杀地址。秒杀地址在生成时存入Redis,然后返回。拿到真实秒杀路径后拼装参数发起秒杀。秒杀请求到了服务端后。先验证请求路径是否正确，如果不正确返回非法请求。然后检查内存中售完标志是否存在，如果不存在继续。否则返回售完。然后通过Redis预计库存，如果预减失败数量已经小于0，将内存中售罄标志标记，然后返回售罄。然后判断是否重复秒杀,是返回不能重复秒杀。否就将用户信息和商品ID通过MQ发送个消费者用于生成订单。然后返回秒杀中的状态。浏览器轮训订单结果


&nbsp;&nbsp;&nbsp;&nbsp;MQ消费者拿到用户和商品ID后先判断库存。然后再次判断是否重复买秒杀，然后做减库存下订单的操作。在减库存是要注意**虽然在之前我们已经多次检查库存了，但是但并发情况下还是有可能不准确。所以我们要用乐观锁更新**
    
> update seckill_goods set stock_count = stock_count-1 where goods_id=#{id} and stockCount=stock_count and stock_count>0


&nbsp;&nbsp;&nbsp;&nbsp;SQL如上商品数量做为查询条件也是修改条件。上述设计中通过内存标识和Redis来做控制让请求可能少的访问数据库，达到数据库减压的目的。


#### 接口限流
&nbsp;&nbsp;&nbsp;&nbsp;在秒杀过程中为了防止有脚本恶意刷接口,所以我们要有限流措施。限流如何做？我们这里通过reids计数器加上Spring-Boot的的拦截器来实现。

    //通过注解标记方法
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface AccessLimit {
	    /**
	     * 时间单位,默认是秒
	     *
	     * @return
	     */
	    int seconds();
	    /**
	     * 单位时间内的最大访问次数
	     *
	     * @return
	     */
	    int maxCount();
	    /**
	     * 是否需登录
	     *
	     * @return
	     */
	    boolean needLogin() default true;
	}

	@Service
	public class AccessInterceptor extends HandlerInterceptorAdapter {
	
	    @Autowired
	    private SeckillUserService seckillUserService;
	
	    @Autowired
	    private RedisService redisService;
	
	    @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
	        if (handler instanceof HandlerMethod) {
	            SeckillUser user = getUser(request, response);
	            UserConText.setUser(user);
	
	            HandlerMethod hm = (HandlerMethod) handler;
	            AccessLimit limit = hm.getMethodAnnotation(AccessLimit.class);
	            if (null == limit) {
	                return true;
	            }
	            int seconds = limit.seconds();
	            int maxCount = limit.maxCount();
	            boolean needLogin = limit.needLogin();
	            String key = request.getRequestURI();
	
	            if (needLogin) {
	                if (null == user) {
	                    render(response, CodeMsg.SESSION_ERROR);
	                    return false;
	                }
	                key = key + "_" + user.getId();
	            }
	
	            //查询访问次数
	            AccessKey accessKey = AccessKey.whihExpire(seconds);
	            Integer count = redisService.get(accessKey, key, Integer.class);
	            if (null == count) {
	                redisService.set(accessKey, key, 1);
	            } else if (count < maxCount) {
	                redisService.incr(accessKey, key);
	            } else {
	                render(response, CodeMsg.ACCESS_LIMIT_REACHED);
	                return false;
	            }
	        }
	        return true;
	    }
	}

	//添加到拦截器链中
	@Configuration
	public class WebConfig extends WebMvcConfigurerAdapter {	
	    @Autowired
	    private AccessInterceptor accessInterceptor;
	    @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(accessInterceptor);
	    }
	}

&nbsp;&nbsp;&nbsp;&nbsp;限流除了这个以外，上面提到的公式验证码也是限流措施。复杂的验证码可以降低单秒内的访问请求值。

#### 数据库优化

&nbsp;&nbsp;&nbsp;&nbsp;在数据量大时，表的设计也很重要。在必要的字段要加上索引，对于常用的sql要查看执行计划保证sql有索引。在连表查询是最好是小表驱动大表查询。不要随意用动态sql，动态sql无法确定是否有索引。消耗大量得数据库资源。








