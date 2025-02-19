package com.yt.message.mq.constant;

/**
 * @ClassName MQConstant
 * @Author Ts
 * @Version 1.0
 */

public class MQConstant {
     //消息发送主题的消费者配置
     public static final String MESSAGE_TOPIC_NAME = "message-send-topic";
     public static final String MESSAGE_TOPIC_CONSUMER_GROUP = "message-send-topic-group";
     public static final int MESSAGE_TOPIC_CONSUMER_THREAD_NUMBER = 3;
     //注意 开启重试可能会有日志重复打印的问题
     //TODO 修复上述问题
     public static final int MESSAGE_MAX_RECONSUME_TIMES = 0;



     //数据同步主题的消费者配置
     public static final String DATA_SYNC_TOPIC_NAME = "data-sync-topic";
     public static final String DATA_SYNC_TOPIC_CONSUMER_GROUP = "data-sync-topic-group";
     public static final int DATA_SYNC_TOPIC_CONSUMER_THREAD_NUMBER = 1;
     public static final int DATA_SYNC_MAX_RECONSUME_TIMES = 1;


     //将消息发送结果发送到一个topic供flink streaming消费生成统计数据
     public static final String MESSAGE_SEND_RESULT_TOPIC_NAME = "message-send-result-topic";
     public static final String MESSAGE_SEND_RESULT_TOPIC_CONSUMER_GROUP = "message-send-result-topic-group";
     public static final int MESSAGE_SEND_RESULT_TOPIC_CONSUMER_THREAD_NUMBER = 3;
     public static final int MESSAGE_SEND_RESULT_TOPIC_MAX_RECONSUME_TIMES = 0;




}
