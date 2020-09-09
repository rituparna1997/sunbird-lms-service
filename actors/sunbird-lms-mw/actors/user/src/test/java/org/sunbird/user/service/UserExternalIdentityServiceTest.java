package org.sunbird.user.service;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.helper.ServiceFactory;
import org.sunbird.user.service.impl.UserExternalIdentityServiceImpl;
import org.sunbird.user.util.UserUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
  UserUtil.class,
  ServiceFactory.class,
  CassandraOperationImpl.class,
})
@PowerMockIgnore("javax.management.*")
public class UserExternalIdentityServiceTest {

  private static CassandraOperation cassandraOperationImpl;

  @BeforeClass
  public static void beforeEachTest() {
    PowerMockito.mockStatic(ServiceFactory.class);
    cassandraOperationImpl = mock(CassandraOperation.class);
    when(ServiceFactory.getInstance()).thenReturn(cassandraOperationImpl);
  }

  @Test
  public void getUserV1Test() {
    Map<String, Object> propertyMap = new HashMap<>();
    Response response = new Response();
    List<Map<String, Object>> resp = new ArrayList<>();
    Map<String, Object> userList = new HashMap<>();
    userList.put(JsonKey.USER_ID, "1234");
    resp.add(userList);
    response.put(JsonKey.RESPONSE, resp);
    when(cassandraOperationImpl.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(response);
    Map<String, String> orgProviderMap = new HashMap<>();
    orgProviderMap.put("channel1004", "01234567687");
    PowerMockito.mockStatic(UserUtil.class);
    when(UserUtil.fetchOrgIdByProvider(Mockito.anyList(), Mockito.any()))
        .thenReturn(orgProviderMap);
    UserExternalIdentityService userExternalIdentityService = new UserExternalIdentityServiceImpl();

    String userId =
        userExternalIdentityService.getUserV1("1234", "channel1004", "channel1004", null);
    Assert.assertTrue(true);
  }

  @Test
  public void getUserV2Test() {
    Map<String, Object> propertyMap = new HashMap<>();
    Response response = new Response();
    List<Map<String, Object>> resp = new ArrayList<>();
    Map<String, Object> userList = new HashMap<>();
    userList.put(JsonKey.USER_ID, "1234");
    resp.add(userList);
    response.put(JsonKey.RESPONSE, resp);
    when(cassandraOperationImpl.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(response);
    UserExternalIdentityService userExternalIdentityService = new UserExternalIdentityServiceImpl();

    String userId =
        userExternalIdentityService.getUserV2("1234", "channel1004", "channel1004", null);
    Assert.assertTrue(true);
  }

  @Test
  public void getSelfDeclaredDetailsTest() {
    Response response = new Response();
    List<Map<String, Object>> resp = new ArrayList<>();
    Map<String, Object> userList = new HashMap<>();
    userList.put(JsonKey.USER_ID, "1234");
    userList.put(JsonKey.PERSONA, "Teacher");
    userList.put(JsonKey.STATUS, "PENDING");
    Map userInfo = new HashMap();
    userInfo.put(JsonKey.DECLARED_EMAIL, "demo@gmail.com");
    userList.put(JsonKey.USER_INFO, userInfo);
    resp.add(userList);
    response.put(JsonKey.RESPONSE, resp);
    Map user = new HashMap();
    user.put(JsonKey.USER_ID, "1234");
    when(cassandraOperationImpl.getRecordById(
            JsonKey.SUNBIRD, JsonKey.USR_DECLARATION_TABLE, user, null))
        .thenReturn(response);
    UserExternalIdentityService userExternalIdentityService = new UserExternalIdentityServiceImpl();
    List selfDeclareExternalId = userExternalIdentityService.getSelfDeclaredDetails("1234", null);
    System.out.println(selfDeclareExternalId);
    Assert.assertTrue(selfDeclareExternalId.size() > 0);
  }
}
