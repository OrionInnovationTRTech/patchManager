package org.patchmanager;


import org.apache.commons.cli.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.naming.NamingException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    static Stream<Arguments> stringArrayProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{"-l", "123"}),
                Arguments.of((Object) new String[]{"-p", "123"}),
                Arguments.of((Object) new String[]{"-v", "123"}),
                Arguments.of((Object) new String[]{"-l", "123", "-p", "123"})
        );
    }

    @ParameterizedTest
    @MethodSource( "stringArrayProvider")
    public void shouldGiveMissingOptionIllegalArgumentException(String[] args) throws ParseException {
        Option LABEL = new Option("l", "label", true, "Specify the label e.g. KL_4.8.1_P_4");
        Option VERSION = new Option("v", "version", true, "Specify the version in the format of number.number.number.charcharnumber e.g. 9.8.1.dl35" );
        Option PATCH = new Option("p", "patch", true, "Specify the patch as a number e.g. 14");
        CommandLineParser commandLineParser = new DefaultParser();
        Options options = new Options();
        options.addOption(LABEL);
        options.addOption(VERSION);
        options.addOption(PATCH);
        CommandLine commandLine;
        //String[] args = new String[] { "-v", "asd"};
        commandLine = commandLineParser.parse(options, args);
        assertThrows(IllegalArgumentException.class, () -> Main.missingOptionChecker(options, commandLine));
//        String expectedMessage = "There is a missing option";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));

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
        assertEquals(expected ,Main.writeIntro("4","4.8.1","9.8.1","9.8.1.dl35", "20220719" ));
    }


}