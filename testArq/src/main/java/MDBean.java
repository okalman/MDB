import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;

/**
 * Created by okalman on 7/15/14.
 */
@MessageDriven(name = "MessageBeanEJB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/jms/MyInQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })


public class MDBean implements MessageListener {
    @Resource(mappedName = "java:/JmsXA")
    private QueueConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/queue/jms/MyOutQueue")
    private Queue queue;

    @Override
    public void onMessage(Message message) {
        try {
            QueueConnection queueConnection = connectionFactory.createQueueConnection();
            QueueSession session = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            QueueSender queueSender = session.createSender(queue);
            queueSender.send(message);
            System.out.println(((TextMessage) message).getText());
        }catch (Exception e){
            e.printStackTrace();
        }




    }
}
