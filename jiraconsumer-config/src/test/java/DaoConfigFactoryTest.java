import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

/**
 * Created by fotarik on 03/03/2017.
 */
public class DaoConfigFactoryTest {

    String JUPITER1_JNDI_READ = "jdbc/Jupiter1DS";


    @Test
    public void testLookUpJndiDataSource() throws Exception {
        // rcarver - setup the jndi context and the datasource
        try {
            // Create initial context
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES,
                    "org.apache.naming");
            InitialContext context = new InitialContext();

            assertNotNull(context.lookup("java:comp/env/" + JUPITER1_JNDI_READ));
            DataSource ds = ((DataSource) context.lookup("java:comp/env/" + JUPITER1_JNDI_READ));
            assertNotNull(ds);

        } catch (NamingException ex) {

        }

    }
}
