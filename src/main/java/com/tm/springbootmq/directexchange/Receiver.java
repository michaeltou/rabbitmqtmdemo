package com.tm.springbootmq.directexchange;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by lenovo on 2017/4/20.
 */
public class Receiver {

    private static final String EXCHANGE_NAME = "directexchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("106.14.98.121");
        factory.setUsername("michael");
        factory.setPassword("tm123456");


        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");


        String queueNameInfo = channel.queueDeclare().getQueue();
        channel.queueBind(queueNameInfo, EXCHANGE_NAME, "info");

        String queueNameWarning = channel.queueDeclare().getQueue();
        channel.queueBind(queueNameWarning, EXCHANGE_NAME, "warning");

        String queueNameError = channel.queueDeclare().getQueue();
        channel.queueBind(queueNameError, EXCHANGE_NAME, "error");


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueNameInfo, true, consumer);
        channel.basicConsume(queueNameWarning, true, consumer);
        channel.basicConsume(queueNameError, true, consumer);
    }
}