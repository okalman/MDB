import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.*;
import java.util.Properties;

/**
 * Created by okalman on 7/15/14.
 */
public class MDBClient {
    private static byte exitStatus=0;
    public static void main(String [] args){
        System.out.println("Starting...");
        QueueConnection queueConnection=null;
        String sMessage= new String();
        try {
            System.out.println("Sending...");
            sMessage = (args.length>0) ? args[0]:"";
            Context context= getcontext();
            Queue queue= (Queue) context.lookup("/jms/myApp/MyInQueue");
            QueueConnectionFactory factory= (QueueConnectionFactory)context.lookup("jms/RemoteConnectionFactory");
            queueConnection= factory.createQueueConnection();
            QueueSession session= queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            TextMessage textMessage = session.createTextMessage(sMessage);
            QueueSender queueSender=  session.createSender(queue);
            queueSender.send(textMessage);
            System.out.println("Sent...");
            queueConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
            exitStatus=1;
        }finally {
             try {
                    queueConnection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                    exitStatus=2;
                }


        }
    System.exit(exitStatus      );

    }

    public static Context getcontext() throws NamingException {
        Properties properties= new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
        properties.setProperty(Context.PROVIDER_URL, String.format("remote://127.0.0.1:4447"));
        return new InitialContext(properties);


    }

}
