package org.patchmanager;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.apiutils.ParseJiraIssues;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class IssueParsingTest {
  @Test
  public void shouldGiveTheseIssuesWith481P3ResponseBodyWithMock() {
    DotEnvUser dotEnvUserObj = new DotEnvUser();
    HttpResponse<String> response = Mockito.mock(HttpResponse.class);

    when(response.statusCode()).thenReturn(200);
    when(response.body()).thenReturn("{\"expand\":\"schema,names\",\"startAt\":0,\"maxResults\":50,\"total\":33,\"issues\":[{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"31563\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/31563\",\"key\":\"ABE-25213\",\"fields\":{\"summary\":\"Kandy app2 not listening on port 18581 and no error in \\\"service wae diag\\\"\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"31587\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/31587\",\"key\":\"ABE-25237\",\"fields\":{\"summary\":\"RCA for Kandy Link Outage - restart services hots3, host4 required to recover service\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"123258\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/123258\",\"key\":\"ABE-25410\",\"fields\":{\"summary\":\"[KL 4.8.1] Kandy Link TURN unable to start due to wrong system.conf permissions\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"123955\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/123955\",\"key\":\"ABE-25433\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 3] Kandy Link PM report CSV file issues\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"129580\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/129580\",\"key\":\"ABE-25593\",\"fields\":{\"summary\":\"[CLONE KL 4.8.1 Patch 2]- waesetup on Standalone TURN fails to complete if NTP server is not reachable\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"129961\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/129961\",\"key\":\"ABE-25601\",\"fields\":{\"summary\":\"[CLONE -  KL 4.8.1 Patch 2] Kandy Link sends two callEnd messages but both have the same eventId\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"129965\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/129965\",\"key\":\"ABE-25603\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 2] Standalone TURN server \\\"turn-bandwidth-controller log directory doesn't exist\\\" error\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"129966\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/129966\",\"key\":\"ABE-25604\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 2] Standalone TURN server \\\"Read timed out\\\" error\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"130272\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/130272\",\"key\":\"ABE-25612\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 2] KL Log File size increase after 4.8 Upgrade.\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"131926\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/131926\",\"key\":\"ABE-25652\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 2] externalNotification issue for localDB users\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"132713\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/132713\",\"key\":\"ABE-25655\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 2] When sdp parameter includes * instead of payload number, KL does not pass related parameter\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"134802\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/134802\",\"key\":\"ABE-25713\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 2] There is no video path on both sides when consult transfering the video call.\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"135903\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/135903\",\"key\":\"ABE-25750\",\"fields\":{\"summary\":\"Kandy Link DEVICE_ACTV Performance Metric is negative\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"135920\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/135920\",\"key\":\"ABE-25756\",\"fields\":{\"summary\":\"[KandyLink 4.8.1] KandyLink does not behave correctly for redundancy when statusTimeout=0\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"135925\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/135925\",\"key\":\"ABE-25757\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 2] An Exception occurred during registration aliasId\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"137538\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/137538\",\"key\":\"ABE-25800\",\"fields\":{\"summary\":\"[KandyLink 4.8.1 Patch 3] Protect Against Kandy Link Outage When Root Password Expires\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"138783\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/138783\",\"key\":\"ABE-25847\",\"fields\":{\"summary\":\"KL 4.8.1 P3 - Nessus Scan Failure - CVE-2021-4034: PwnKit: Local Privilege Escalation Security Vulnerability (HIGH Severity) - nss & polkit\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"139002\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/139002\",\"key\":\"ABE-25857\",\"fields\":{\"summary\":\"KL_4.8.1_Patch_3 update dblogcleanup to return error code of MariaDB when \\\"Purge\\\" query fail\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"139174\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/139174\",\"key\":\"ABE-25859\",\"fields\":{\"summary\":\"KL 4.8.1 - Nessus Scan Failure - Severity: Critical - Plugin ID: 156860 - Name: Apache Log4j 1.x Multiple Vulnerabilities\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"139205\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/139205\",\"key\":\"ABE-25861\",\"fields\":{\"summary\":\"[KandyLink 4.8.1] - Kandy Link KPI Usage reports sftp error and missing fields\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"139393\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/139393\",\"key\":\"ABE-25867\",\"fields\":{\"summary\":\"[KandyLink 4.8.1 Patch 3] - Kandy Link is not rotating /var/log/mysql/error.log\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"139509\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/139509\",\"key\":\"ABE-25869\",\"fields\":{\"summary\":\"[CLONE - Kandy Link 4.8.1 Patch 3] - 488 Unexpectedly Triggers “down” Status\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"139590\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/139590\",\"key\":\"ABE-25872\",\"fields\":{\"summary\":\"[KandyLink 4.8.1 Patch 3]  Patch Management Tool Error\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"139620\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/139620\",\"key\":\"ABE-25876\",\"fields\":{\"summary\":\"CLONE - R4.8.1 -KL Security Vulnerability - Library: log4j-1.2.17.jar Vulnerability ID: CVE-2021-4104 (Remote code execution in Log4j 1.x when application is configured to use JMSAppender)\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"139846\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/139846\",\"key\":\"ABE-25883\",\"fields\":{\"summary\":\"KL 4.8.1 - Nessus Scan Failure - Severity: Critical - Plugin ID: 156860 - Name: Apache Log4j 1.x Multiple Vulnerabilities - Script Preparation\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"140125\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/140125\",\"key\":\"ABE-25898\",\"fields\":{\"summary\":\"Kandy Link 4.8.1 Patch 3 - Increase Severity Alarms when Platform User Passwords are about to Expire\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"140128\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/140128\",\"key\":\"ABE-25899\",\"fields\":{\"summary\":\"Stuck registrations over Kandy Link restart/data centre outage\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"140373\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/140373\",\"key\":\"ABE-25905\",\"fields\":{\"summary\":\"CLONE - [KandyLink 4.8.1 Patch 3]  Review of Registration Audit\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"141510\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/141510\",\"key\":\"ABE-25938\",\"fields\":{\"summary\":\"KandyLink 4.8.1 Patch 3 - PASSWORD_EXPIRED Alarm Needs to be Update the R4.8.0 Fault and Performance Doc\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"142015\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/142015\",\"key\":\"ABE-25948\",\"fields\":{\"summary\":\"[CLONE - KL 4.8.1 Patch 3] Revert Fix for ABE-25602 and ABE-25824\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"142231\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/142231\",\"key\":\"ABE-25952\",\"fields\":{\"summary\":\"Kandy Link 4.8.1- RESTful API Spec Doc Should be Updated \"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"143635\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/143635\",\"key\":\"ABE-25992\",\"fields\":{\"summary\":\"Upgrade doc update - KL 4.8.1 P3 - Nessus Scan Failure - CVE-2021-4034: PwnKit: Local Privilege Escalation Security Vulnerability (HIGH Severity) - nss & polkit\"}},{\"expand\":\"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\"id\":\"145413\",\"self\":\"https://kandyio.atlassian.net/rest/api/2/issue/145413\",\"key\":\"ABE-26053\",\"fields\":{\"summary\":\"KL 4.8.1 - Patch3 - Nessus Scan Failure - Severity: High - Plugin ID: 159908 - Name: RHEL 6 : kernel (RHSA-2022:1417)\"}}]}");
    assertEquals("ABE-25213\n" +
        "Kandy app2 not listening on port 18581 and no error in \"service wae diag\"\n" +
        "\n" +
        "ABE-25237\n" +
        "RCA for Kandy Link Outage - restart services hots3, host4 required to recover service\n" +
        "\n" +
        "ABE-25410\n" +
        "Kandy Link TURN unable to start due to wrong system.conf permissions\n" +
        "\n" +
        "ABE-25433\n" +
        "Kandy Link PM report CSV file issues\n" +
        "\n" +
        "ABE-25593\n" +
        "waesetup on Standalone TURN fails to complete if NTP server is not reachable\n" +
        "\n" +
        "ABE-25601\n" +
        "Kandy Link sends two callEnd messages but both have the same eventId\n" +
        "\n" +
        "ABE-25603\n" +
        "Standalone TURN server \"turn-bandwidth-controller log directory doesn't exist\" error\n" +
        "\n" +
        "ABE-25604\n" +
        "Standalone TURN server \"Read timed out\" error\n" +
        "\n" +
        "ABE-25612\n" +
        "KL Log File size increase after 4.8 Upgrade.\n" +
        "\n" +
        "ABE-25652\n" +
        "externalNotification issue for localDB users\n" +
        "\n" +
        "ABE-25655\n" +
        "When sdp parameter includes * instead of payload number, KL does not pass related parameter\n" +
        "\n" +
        "ABE-25713\n" +
        "There is no video path on both sides when consult transfering the video call.\n" +
        "\n" +
        "ABE-25750\n" +
        "Kandy Link DEVICE_ACTV Performance Metric is negative\n" +
        "\n" +
        "ABE-25756\n" +
        "KandyLink does not behave correctly for redundancy when statusTimeout=0\n" +
        "\n" +
        "ABE-25757\n" +
        "An Exception occurred during registration aliasId\n" +
        "\n" +
        "ABE-25800\n" +
        "Protect Against Kandy Link Outage When Root Password Expires\n" +
        "\n" +
        "ABE-25847\n" +
        "Nessus Scan Failure - CVE-2021-4034: PwnKit: Local Privilege Escalation Security Vulnerability (HIGH Severity) - nss & polkit\n" +
        "\n" +
        "ABE-25857\n" +
        "update dblogcleanup to return error code of MariaDB when \"Purge\" query fail\n" +
        "\n" +
        "ABE-25859\n" +
        "Nessus Scan Failure - Severity: Critical - Plugin ID: 156860 - Name: Apache Log4j 1.x Multiple Vulnerabilities\n" +
        "\n" +
        "ABE-25861\n" +
        "Kandy Link KPI Usage reports sftp error and missing fields\n" +
        "\n" +
        "ABE-25867\n" +
        "Kandy Link is not rotating /var/log/mysql/error.log\n" +
        "\n" +
        "ABE-25869\n" +
        "488 Unexpectedly Triggers “down” Status\n" +
        "\n" +
        "ABE-25872\n" +
        "Patch Management Tool Error\n" +
        "\n" +
        "ABE-25876\n" +
        "KL Security Vulnerability - Library: log4j-1.2.17.jar Vulnerability ID: CVE-2021-4104 (Remote code execution in Log4j 1.x when application is configured to use JMSAppender)\n" +
        "\n" +
        "ABE-25883\n" +
        "Nessus Scan Failure - Severity: Critical - Plugin ID: 156860 - Name: Apache Log4j 1.x Multiple Vulnerabilities - Script Preparation\n" +
        "\n" +
        "ABE-25898\n" +
        "Increase Severity Alarms when Platform User Passwords are about to Expire\n" +
        "\n" +
        "ABE-25899\n" +
        "Stuck registrations over Kandy Link restart/data centre outage\n" +
        "\n" +
        "ABE-25905\n" +
        "Review of Registration Audit\n" +
        "\n" +
        "ABE-25938\n" +
        "PASSWORD_EXPIRED Alarm Needs to be Update the R4.8.0 Fault and Performance Doc\n" +
        "\n" +
        "ABE-25948\n" +
        "Revert Fix for ABE-25602 and ABE-25824\n" +
        "\n" +
        "ABE-25952\n" +
        "RESTful API Spec Doc Should be Updated \n" +
        "\n" +
        "ABE-25992\n" +
        "Nessus Scan Failure - CVE-2021-4034: PwnKit: Local Privilege Escalation Security Vulnerability (HIGH Severity) - nss & polkit\n" +
        "\n" +
        "ABE-26053\n" +
        "Nessus Scan Failure - Severity: High - Plugin ID: 159908 - Name: RHEL 6 : kernel (RHSA-2022:1417)\n" +
        "\n", ParseJiraIssues.parseJiraIssues(response.body(), "4.8.1", "3"));

  }
}
