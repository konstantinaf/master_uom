package services;

import com.uom.jirareport.consumers.dao.JiraConsumerRepository;
import com.uom.jirareport.consumers.dto.JiraConsumer;
import com.uom.jirareport.consumers.dto.JiraConsumerKey;
import com.uom.jirareport.consumers.dto.ServiceResponse;
import com.uom.jirareport.consumers.oauth.Command;
import com.uom.jirareport.consumers.oauth.JiraOAuthClient;
import com.uom.jirareport.consumers.oauth.OAuthClient;
import com.uom.jirareport.consumers.services.JiraConsumerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by fotarik on 04/03/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({JiraConsumerServiceImpl.class, ServiceResponse.ServiceResponseBuilder.class})
public class JiraConsumerServiceImplTest {

    private static String AUTHORIZATION_URL = "http://localhost:8100/";
    private static Integer ERROR_CODE = 12;
    private static String ERROR_MESSAGE = "Error";


    @InjectMocks
    JiraConsumerServiceImpl jiraConsumerService;

    @Mock
    JiraConsumerRepository mockedRepository;

    @Mock
    ServiceResponse.ServiceResponseBuilder mockedServiceResponseBuilder;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAuthorizationUrl() throws Exception {
        ServiceResponse serviceResponse = buildServiceResponse(AUTHORIZATION_URL, null, null);

        JiraConsumer jiraConsumer = mock(JiraConsumer.class);
        JiraConsumerKey jiraConsumerKey = mock(JiraConsumerKey.class);

        when(jiraConsumer.getJiraConsumerKey()).thenReturn(jiraConsumerKey);
        when(jiraConsumerKey.getJiraUrl()).thenReturn(AUTHORIZATION_URL);

        when(mockedRepository.findJiraConsumerByHomeUrl(anyString())).thenReturn(jiraConsumer);

        OAuthClient oAuthClient = mock(OAuthClient.class);
        whenNew(OAuthClient.class).withArguments(any(JiraOAuthClient.class), any(JiraConsumer.class)).thenReturn(oAuthClient);

        doNothing().when(oAuthClient).execute(any(Command.class), anyListOf(String.class));

        when(oAuthClient.getAuthorizationUrl()).thenReturn(AUTHORIZATION_URL);

        assertEquals(serviceResponse.getData(), jiraConsumerService.getAuthorizationUrl(anyString()).getData());

    }

    @Test
    public void testGetAutorizationUrlWithInvalidArguments() throws Exception {
        ServiceResponse serviceResponse = buildServiceResponse(null, ERROR_CODE, ERROR_MESSAGE);

        when(mockedRepository.findJiraConsumerByHomeUrl(anyString())).thenReturn(null);

        assertEquals(serviceResponse.getError(), jiraConsumerService.getAuthorizationUrl(anyString()).getError());
    }

    @Test
    public void testGetAccessToken() throws Exception {

    }

    private ServiceResponse buildServiceResponse(String data, Integer error, String errorMessage) {
        ServiceResponse.ServiceResponseBuilder serviceResponseBuilder = new ServiceResponse.ServiceResponseBuilder(data, error, errorMessage);

        return serviceResponseBuilder.build();
    }

}
