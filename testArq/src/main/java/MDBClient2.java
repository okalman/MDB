import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created by okalman on 7/17/14.
 */
public class MDBClient2 {
    public static String recieve(){
        //  System.out.println("Starting...");
        QueueConnection queueConnection=null;
        TextMessage message= null;
        try {
            //  System.out.println("Reading...");
            Context context= getcontext();
            Queue queue= (Queue) context.lookup("queue/jms/MyOutQueue");
            QueueConnectionFactory factory= (QueueConnectionFactory)context.lookup("jms/RemoteConnectionFactory");
            queueConnection= factory.createQueueConnection();
            queueConnection.start();
            QueueSession session= queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            QueueReceiver queueReceiver=session.createReceiver(queue);
            message= (TextMessage)queueReceiver.receive();
            System.out.println(message.getText());
            return message.getText();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if(queueConnection!=null) queueConnection.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            //System.out.println("End");

        }
       // System.exit(exitCode);
        return null;
    }

    public static Context getcontext() throws NamingException {
        Properties properties= new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
        properties.setProperty(Context.PROVIDER_URL, String.format("remote://127.0.0.1:4447"));
        return new InitialContext(properties);


    }
}
