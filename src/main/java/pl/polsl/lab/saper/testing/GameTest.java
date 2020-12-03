//package pl.polsl.lab.saper.testing;
//
//import jdk.jfr.Description;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import pl.polsl.lab.saper.controller.GameController;
//import pl.polsl.lab.saper.exception.FieldException;
//import pl.polsl.lab.saper.model.Game;
//import pl.polsl.lab.saper.model.Index;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Class contain test for game model and randomMines method
// *
// * @author Konrad Skrzypczyk
// * @version 1.0
// */
//class GameTest {
//
//    /**
//     * Test data which contain wrong row index
//     *
//     * @return test data
//     */
//    private static Stream<Arguments> FieldExceptionRowGenerator() {
//        return Stream.of(
//                Arguments.of(new Game(10, 10), new Index(0, 0)),
//                Arguments.of(new Game(10, 10), new Index(0, 10)),
//                Arguments.of(new Game(10, 10), new Index(11, 0)),
//                Arguments.of(new Game(10, 10), new Index(12, 0)),
//                Arguments.of(new Game(10, 10), new Index(11, 11)),
//                Arguments.of(new Game(10, 10), new Index(12, 12)),
//                Arguments.of(new Game(5, 5), new Index(-5, 5)),
//                Arguments.of(new Game(5, 5), new Index(-5, -5))
//        );
//    }
//
//    /**
//     * Test data which contain wrong column index
//     *
//     * @return test data
//     */
//    private static Stream<Arguments> FieldExceptionColGenerator() {
//        return Stream.of(
//                Arguments.of(new Game(10, 10), new Index(1, 0)),
//                Arguments.of(new Game(10, 10), new Index(1, 11)),
//                Arguments.of(new Game(10, 10), new Index(1, 12)),
//                Arguments.of(new Game(10, 10), new Index(10, 0)),
//                Arguments.of(new Game(10, 10), new Index(10, 11)),
//                Arguments.of(new Game(10, 10), new Index(10, 12)),
//                Arguments.of(new Game(5, 5), new Index(5, -5))
//        );
//    }
//
//    /**
//     * Test data which contain correct field index
//     *
//     * @return test data
//     */
//    private static Stream<Arguments> FieldGenerator() {
//        return Stream.of(
//                Arguments.of(new Game(1, 1), new Index(1, 1)),
//                Arguments.of(new Game(10, 10), new Index(1, 1)),
//                Arguments.of(new Game(10, 10), new Index(10, 10)),
//                Arguments.of(new Game(10, 10), new Index(5, 5)),
//                Arguments.of(new Game(10, 10), new Index(3, 8)),
//                Arguments.of(new Game(2, 2), new Index(1, 1)),
//                Arguments.of(new Game(2, 2), new Index(1, 2)),
//                Arguments.of(new Game(2, 2), new Index(2, 1)),
//                Arguments.of(new Game(2, 2), new Index(1, 2))
//        );
//    }
//
//    /**
//     * Is correct field row exceptions.
//     *
//     * @param game the game
//     * @param inx  the inx
//     */
//    @ParameterizedTest
//    @Description("Testing wrong row index behaviour")
//    @MethodSource("FieldExceptionRowGenerator")
//    void isCorrectFieldRowExceptions(Game game, Index inx) {
//        FieldException exception = assertThrows(
//                FieldException.class,
//                () -> game.isCorrectField(inx)
//        );
//        assertEquals("Invalid row index", exception.getMessage());
//    }
//
//    /**
//     * Is correct field col exceptions.
//     *
//     * @param game the game
//     * @param inx  the inx
//     */
//    @ParameterizedTest
//    @Description("Testing wrong column index behaviour")
//    @MethodSource("FieldExceptionColGenerator")
//    void isCorrectFieldColExceptions(Game game, Index inx) {
//        FieldException exception = assertThrows(
//                FieldException.class,
//                () -> game.isCorrectField(inx)
//        );
//        assertEquals("Invalid column index", exception.getMessage());
//    }
//
//    /**
//     * Is correct field test.
//     *
//     * @param game the game
//     * @param inx  the inx
//     */
//    @ParameterizedTest
//    @Description("Testing correct field index")
//    @MethodSource("FieldGenerator")
//    void isCorrectFieldTest(Game game, Index inx) {
//        assertAll(
//                () -> assertDoesNotThrow(() -> game.isCorrectField(inx))
//        );
//    }
//
//    /**
//     * Random mines invalid value test.
//     */
//    @RepeatedTest(5)
//    @Description("Test randomMines method with negative arguments. It is used in GameController in constructor")
//    void randomMinesInvalidValueTest() {
//        try {
//            GameController gameController = new GameController(null,0, 0);
//            fail("Can random mines for board with wrong width or height");
//        } catch (IllegalArgumentException ignore) {
//        }
//        try {
//            GameController gameController = new GameController(null,0, 1);
//            fail("Can random mines for board with wrong width or height");
//        } catch (IllegalArgumentException ignore) {
//        }
//        try {
//            GameController gameController = new GameController(null,1, 0);
//            fail("Can random mines for board with wrong width or height");
//        } catch (IllegalArgumentException ignore) {
//        }
//        try {
//            GameController gameController = new GameController(null,-1, 1);
//            fail("Can random mines for board with wrong width or height");
//        } catch (IllegalArgumentException ignore) {
//        }
//    }
//
//    /**
//     * Random mines board null exception.
//     */
//    @Test
//    @Description("Test that use @Disabled label")
//    void randomMinesBoardNullException() {
//        try {
//            GameController gameController = new GameController(null,null, -1);
//            fail("Can random mines for board with wrong width or height");
//        } catch (NullPointerException ignore) {
//        }
//        try {
//            GameController gameController = new GameController(null,-1, null);
//            fail("Can random mines for board with wrong width or height");
//        } catch (NullPointerException ignore) {
//        }
//    }
//
//    /**
//     * Simply test.
//     */
//    @Test
//    @Disabled
//    @Deprecated
//    @Description("Simply test contain create board and random mines")
//    void SimplyTest() {
//    }
//
//}