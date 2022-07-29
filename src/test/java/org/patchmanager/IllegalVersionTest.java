package org.patchmanager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.patchmanager.command_line.OptionsRelated;
import org.patchmanager.command_line.VersionInputChecker;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IllegalVersionTest {
    OptionsRelated optionsRelatedObj = new OptionsRelated();


    static Stream<Arguments> stringArrayProviderForIllegalVersionArgument() {
        return Stream.of(
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl", "-p", "1"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.dl35", "-p", "1"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "..", "-p", "1"}),
                Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "", "-p", "1"})
        );
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
}
