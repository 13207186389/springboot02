package com.pengyou.config;

import com.pengyou.rabbitListener.UserOrderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * rabbitmq配置
 * Created by steadyjack on 2018/9/28.
 */
@Configuration
public class RabbitmqConfig {

    private static final Logger log= LoggerFactory.getLogger(RabbitmqConfig.class);

    @Autowired
    private Environment env;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;


    /**
     * 单一消费者
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setTxSize(1);
        return factory;
    }

    /**
     * 多个消费者
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory,connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.concurrency",int.class));
        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.max-concurrency",int.class));
        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.simple.prefetch",int.class));
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);

        //TODO：面向生产端
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
            }
        });
        return rabbitTemplate;
    }


    //TODO：用户注册消息模型----------------------------------------------------------------------------------------------

    /**
     * 创建队列,指定队列名,并且持久化队列
     * @return
     */
    @Bean
    public Queue userRegisterQueue(){
        return new Queue(env.getProperty("rabbitmq.user.register.queue.name"),true);
    }

    /**
     * 创建直连交换机 指定交换机名称,持久化,不自动删除
     * @return
     */
    @Bean
    public Exchange userRegisterExchange(){
        return new DirectExchange(env.getProperty("rabbitmq.user.register.exchange.name"),true,false);
    }

    /**
     * 绑定队列到交换机,采用路由routingkey,并且传入map设置绑定超时时间等,没有的话传入null
     * @return
     */
    @Bean
    public Binding subContractInvalidBinding(){
        return BindingBuilder.bind(userRegisterQueue()).to(userRegisterExchange()).with(env.getProperty("rabbitmq.user.register.routing.key.name")).and(null);
    }

    //TODO：并发配置-消息确认机制-用户商城下单模型--------------------------------------------------------------------------

    /**
     * 创建队列,指定队列名字,并持久化队列
     * @return
     */
    @Bean(name = "userOrderQueue")
    public Queue userOrderQueue(){
        return new Queue(env.getProperty("rabbitmq.user.order.queue.name"),true);
    }

    /**
     * 创建主题交换机,指定交换机名称,持久化,不自动删除
     * @return
     */
    @Bean
    public TopicExchange userOrderExchange(){
        return new TopicExchange(env.getProperty("rabbitmq.user.order.exchange.name"),true,false);
    }

    /**
     * 绑定队列到交换机,采用路由routkingkey
     * @return
     */
    @Bean
    public Binding userOrderBinding(){
        return BindingBuilder.bind(userOrderQueue()).to(userOrderExchange()).with(env.getProperty("rabbitmq.user.order.routing.key.name"));
    }

    //TODO:面向消费端,消息确认机制针对监听者做一个消息确认的设置-------------------------------------------------------------

    @Autowired
    private UserOrderListener userOrderListener;

    //TODO:消息监听器容器
    @Bean(name = "userOrderSimpleContainer")
    public SimpleMessageListenerContainer simpleContainer(@Qualifier("userOrderQueue") Queue userOrderQueue){
        SimpleMessageListenerContainer container=new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageConverter(new Jackson2JsonMessageConverter());

        //TODO：并发配置
        container.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.concurrency",Integer.class));
        container.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.max-concurrency",Integer.class));
        container.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.simple.prefetch",Integer.class));

        //TODO：消息确认-确认机制种类(手动)
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setQueues(userOrderQueue);
        container.setMessageListener(userOrderListener);

        return container;
    }










}






































