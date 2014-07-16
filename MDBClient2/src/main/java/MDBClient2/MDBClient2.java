package MDBClient2;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created by okalman on 7/16/14.
 */
public class MDBClient2 {


    public static void main(String [] args){
        System.out.println("Starting...");
        QueueConnection queueConnection=null;
        try {
            System.out.println("Reading...");
            Context context= getcontext();
            Queue queue= (Queue) context.lookup("/jms/myApp/MyOutQueue");
            QueueConnectionFactory factory= (QueueConnectionFactory)context.lookup("jms/RemoteConnectionFactory");
            queueConnection= factory.createQueueConnection();
            queueConnection.start();
            QueueSession session= queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            QueueReceiver queueReceiver=session.createReceiver(queue);
            TextMessage message= null;

            while(true){
                message= (TextMessage)queueReceiver.receive();
                System.out.println("Received: " + message.getText());

            }



        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                queueConnection.close();
            }catch(Exception e){
                e.printStackTrace();
            }
         System.out.println("End");
        }


    }

    public static Context getcontext() throws NamingException {
        Properties properties= new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
        properties.setProperty(Context.PROVIDER_URL, String.format("remote://127.0.0.1:4447"));
        return new InitialContext(properties);


    }

}
