
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;


/**
 * Created by okalman on 7/17/14.
 */

@RunWith(Arquillian.class)
public class TestClass {


    private static final String MDB="mdb";
    @Deployment(managed = false, testable = false, name = MDB)
    public static JavaArchive createDeployment(){
        JavaArchive jar= ShrinkWrap.create(JavaArchive.class, "testMDB.jar");
        jar.addClass(MDBean.class);
        jar.addAsManifestResource(EmptyAsset.INSTANCE,"MANIFEST.MF");
        jar.addAsManifestResource(EmptyAsset.INSTANCE,"bean.xml");
        System.out.println(jar.toString(true));
        return jar;
    }


    @ArquillianResource
    protected Deployer deployer;

    @RunAsClient
    @Test
    public void test1(){
        deployer.deploy(MDB);
        String message="zprava";
        MDBClient.send(message);
        String message2=MDBClient2.recieve();
        assertTrue(message.equals(message2));
        deployer.undeploy(MDB);



    }


}
