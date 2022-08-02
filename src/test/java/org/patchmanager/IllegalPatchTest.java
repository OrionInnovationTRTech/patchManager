package org.patchmanager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.patchmanager.cli.OptionsRelated;
import org.patchmanager.cli.PatchInputChecker;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IllegalPatchTest {
  OptionsRelated optionsRelatedObj = new OptionsRelated();

  static Stream<Arguments> stringArrayProviderForIllegalPatchArgument() {
    return Stream.of(
        Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl35", "-p", "a"}),
        Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl35", "-p", "1a"}),
        Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl35", "-p", "1.5"}),
        Arguments.of((Object) new String[]{"-l", "KL_4.8.1_P_3", "-v", "9.8.1.dl35", "-p", ""})
    );
  }

  @ParameterizedTest
  @MethodSource("stringArrayProviderForIllegalPatchArgument")
  public void patchInputCheckerShouldGiveIllegalArgumentException(String[] args) throws ParseException {
    CommandLine commandLine = optionsRelatedObj.commandLineParser.parse(optionsRelatedObj.optionsList, args);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> PatchInputChecker.patchInputChecker(commandLine));
    String expectedMessage = "Patch does not fit the criteria";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

}
