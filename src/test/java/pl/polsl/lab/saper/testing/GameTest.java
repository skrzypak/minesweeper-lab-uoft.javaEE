package pl.polsl.lab.saper.testing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Game;
import pl.polsl.lab.saper.model.Index;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class contain test for game model and randomMines method
 *
 * @author Konrad Skrzypczyk
 * @version 2.0
 */
class GameTest {

    /**
     * Test data which contain wrong row index
     * @throws FieldException field error
     * @return test data
     */
    private static Stream<Arguments> FieldExceptionRowGenerator() throws FieldException {
        return Stream.of(
                Arguments.of(new Game("0", 10, 10), new Index(0, 0)),
                Arguments.of(new Game("0",10, 10), new Index(0, 10)),
                Arguments.of(new Game("0",10, 10), new Index(11, 0)),
                Arguments.of(new Game("0",10, 10), new Index(12, 0)),
                Arguments.of(new Game("0",10, 10), new Index(11, 11)),
                Arguments.of(new Game("0",10, 10), new Index(12, 12)),
                Arguments.of(new Game("0",5, 5), new Index(-5, 5)),
                Arguments.of(new Game("0",5, 5), new Index(-5, -5))
        );
    }

    /**
     * Test data which contain wrong column index
     * @throws FieldException field error
     * @return test data
     */
    private static Stream<Arguments> FieldExceptionColGenerator() throws FieldException {
        return Stream.of(
                Arguments.of(new Game("0",10, 10), new Index(1, 0)),
                Arguments.of(new Game("0",10, 10), new Index(1, 11)),
                Arguments.of(new Game("0",10, 10), new Index(1, 12)),
                Arguments.of(new Game("0",10, 10), new Index(10, 0)),
                Arguments.of(new Game("0",10, 10), new Index(10, 11)),
                Arguments.of(new Game("0",10, 10), new Index(10, 12)),
                Arguments.of(new Game("0",5, 5), new Index(5, -5))
        );
    }

    /**
     * Test data which contain correct field index
     * @throws FieldException field error
     * @return test data
     */
    private static Stream<Arguments> FieldGenerator() throws FieldException {
        return Stream.of(
                Arguments.of(new Game("0",1, 1), new Index(1, 1)),
                Arguments.of(new Game("0",10, 10), new Index(1, 1)),
                Arguments.of(new Game("0",10, 10), new Index(10, 10)),
                Arguments.of(new Game("0",10, 10), new Index(5, 5)),
                Arguments.of(new Game("0",10, 10), new Index(3, 8)),
                Arguments.of(new Game("0",2, 2), new Index(1, 1)),
                Arguments.of(new Game("0",2, 2), new Index(1, 2)),
                Arguments.of(new Game("0",2, 2), new Index(2, 1)),
                Arguments.of(new Game("0",2, 2), new Index(1, 2))
        );
    }

    /**
     * Is correct field row exceptions.
     *
     * @param game the game
     * @param inx  the inx
     */
    @ParameterizedTest
    @MethodSource("FieldExceptionRowGenerator")
    void isCorrectFieldRowExceptions(Game game, Index inx) {
        FieldException exception = assertThrows(
                FieldException.class,
                () -> game.isCorrectField(inx)
        );
        assertEquals("Invalid row index", exception.getMessage());
    }

    /**
     * Is correct field col exceptions.
     *
     * @param game the game
     * @param inx  the inx
     */
    @ParameterizedTest
    @MethodSource("FieldExceptionColGenerator")
    void isCorrectFieldColExceptions(Game game, Index inx) {
        FieldException exception = assertThrows(
                FieldException.class,
                () -> game.isCorrectField(inx)
        );
        assertEquals("Invalid column index", exception.getMessage());
    }

    /**
     * Is correct field test.
     *
     * @param game the game
     * @param inx  the inx
     */
    @ParameterizedTest
    @MethodSource("FieldGenerator")
    void isCorrectFieldTest(Game game, Index inx) {
        assertAll(
                () -> assertDoesNotThrow(() -> game.isCorrectField(inx))
        );
    }

    /**
     * Simply test.
     */
    @Test
    @Disabled
    @Deprecated
    void SimplyTest() {
    }

}