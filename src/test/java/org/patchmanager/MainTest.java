package org.patchmanager;


import org.apache.commons.cli.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.patchmanager.api_related.DotEnvUser;
import org.patchmanager.command_line.MissingOptionChecker;
import org.patchmanager.command_line.OptionsRelated;
import org.patchmanager.command_line.PatchInputChecker;
import org.patchmanager.command_line.VersionInputChecker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.patchmanager.api_related.AuthChecker.checkAuth;
import static org.patchmanager.writing_to_file.WriteIntro.writeIntro;
import static org.patchmanager.writing_to_file.WriteOutro.writeOutro;

class MainTest {
    OptionsRelated optionsRelatedObj = new OptionsRelated();

    static Stream<Arguments> stringArrayProviderForMissingArgument() {
        return Stream.of(
                //Arguments.of((Object) new String[]{null}),
                Arguments.of((Object) new String[]{""}),
                Arguments.of((Object) new String[]{"-l", ""}),
                //Arguments.of((Object) new String[]{"-l", null}),
                Arguments.of((Object) new String[]{"-l", "123"}),
                Arguments.of((Object) new String[]{"-p", "123"}),
                Arguments.of((Object) new String[]{"-v", "123"}),
                Arguments.of((Object) new String[]{"-l", "123", "-v", "123"}),
                Arguments.of((Object) new String[]{"-l", "123", "-p", "123"}),
                Arguments.of((Object) new String[]{"-v", "123", "-p", "123"}),
                Arguments.of((Object) new String[]{"-v", "123", "-l", "123"}),
                Arguments.of((Object) new String[]{"-p", "123", "-v", "123"}),
                Arguments.of((Object) new String[]{"-p", "123", "-l", "123"}),
                Arguments.of((Object) new String[]{"-v", "123", "-v", "123"}),
                Arguments.of((Object) new String[]{"-l", "123", "-l", "123"}),
                Arguments.of((Object) new String[]{"-p", "123", "-p", "123"})
        );
    }
    static Stream<Arguments> stringArrayProviderForIllegalPatchArgument() {
        return Stream.of(
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl35", "-p", "a"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl35", "-p", "1a"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl35", "-p", "1.5"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl35", "-p", ""})
        );
    }
    static Stream<Arguments> stringArrayProviderForIllegalVersionArgument() {
        return Stream.of(
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl", "-p", "1"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.dl35", "-p", "1"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "..", "-p", "1"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "", "-p", "1"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", null, "-p", "1"})
        );
    }
    @ParameterizedTest
    @MethodSource( "stringArrayProviderForMissingArgument")
    public void shouldGiveMissingOptionMissingArgumentException(String[] args) throws ParseException {
        //String[] args = new String[] { "-v", "asd"};
        CommandLine commandLine = optionsRelatedObj.commandLineParser.parse(optionsRelatedObj.optionsList, args);
        Exception exception = assertThrows(MissingArgumentException.class, () -> MissingOptionChecker.missingOptionChecker(commandLine));
        String expectedMessage = "There is a missing option";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @MethodSource( "stringArrayProviderForIllegalPatchArgument")
    public void patchInputCheckerShouldGiveIllegalArgumentException(String[] args) throws ParseException {
        CommandLine commandLine = optionsRelatedObj.commandLineParser.parse(optionsRelatedObj.optionsList, args);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> PatchInputChecker.patchInputChecker(commandLine));
        String expectedMessage = "Patch does not fit the criteria";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @ParameterizedTest
    @MethodSource( "stringArrayProviderForIllegalVersionArgument")
    public void versionInputCheckerShouldGiveIllegalArgumentException(String[] args) throws ParseException {
        CommandLine commandLine = optionsRelatedObj.commandLineParser.parse(optionsRelatedObj.optionsList, args);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VersionInputChecker.versionInputChecker(commandLine));
        String expectedMessage = "Version does not fit the criteria";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void checkAuthTest() throws ParseException, URISyntaxException, IOException, InterruptedException {
        DotEnvUser dotEnvUserObj = new DotEnvUser();
        assertTrue(checkAuth(dotEnvUserObj.email, dotEnvUserObj.api));
    }
    @Test
    public void writeIntroTest(){
        String expected = "PRODUCT_LINE: KANDYLINK\n" +
                "============================================\n" +
                "\n" +
                "CATEGORY: GEN\n" +
                "PREREQUISITES: \n" +
                "END\n" +
                "PATCH ID: KANDYLINK_9.8.1.dl35_P_4\n" +
                "LOADS: KANDYLINK_9.8.1\n" +
                "END\n" +
                "STATUS: V\n" +
                "WEB_POST: Y\n" +
                "STATUS DATE: 20220719\n" +
                "TITLE: Patch 4\n" +
                "DETAILED_DESCRIPTION:\n" +
                "This patch includes all fix that were previously released in earlier patches and therefore only the latest patches needs to be applied. \n" +
                "For a complete list of fixes in this patch please refer to the individual patch admin files from the previously released patches.\n" +
                "Please check to KANDYLINK 4.8.1 Patch 4 Release Notes for details.\n" +
                "\n" +
                "Includes fixes for following issues:\n";
        assertEquals(expected ,writeIntro("4","4.8.1","9.8.1","9.8.1.dl35", "20220719" ));
    }
    @Test
    public void writeOutroTest(){
        String expected = "\n" +
                "END DETAILED_DESCRIPTION\n" +
                "KANDYLINK_" + "9.8.1" + "_P_" + "4" + ".tar.gz\n\n";
        assertEquals(expected ,writeOutro("4","9.8.1"));
    }

}