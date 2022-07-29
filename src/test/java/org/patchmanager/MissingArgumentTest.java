package org.patchmanager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.patchmanager.command_line.MissingOptionChecker;
import org.patchmanager.command_line.OptionsRelated;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MissingArgumentTest {
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
}
