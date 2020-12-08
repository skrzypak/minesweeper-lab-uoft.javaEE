//package pl.polsl.lab.saper.testing;
//
//import jdk.jfr.Description;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import pl.polsl.lab.saper.exception.FieldException;
//import pl.polsl.lab.saper.model.GameBoard;
//import pl.polsl.lab.saper.model.Index;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Class contain test for game board model
// *
// * @author Konrad Skrzypczyk
// * @version 1.0
// */
//class GameBoardTest {
//
//    /**
//     * Put empty field test.
//     */
//    @Test
//    @DisplayName("Testing putEmptyField method")
//    @Description("Testing throws behaviour in putEmptyField method from GameBoard class")
//    void putEmptyFieldTest() {
//        GameBoard gameBoard = new GameBoard(2, 2);
//        assertAll(
//                () -> assertDoesNotThrow(() -> gameBoard.putEmptyField(new Index(0, 0))),
//                () -> assertDoesNotThrow(() -> gameBoard.putEmptyField(new Index(1, 1))),
//                () -> assertDoesNotThrow(() -> gameBoard.putEmptyField(new Index(2, 2))),
//                () -> assertDoesNotThrow(() -> gameBoard.putEmptyField(new Index(3, 3))),
//                () -> assertThrows(FieldException.class, () -> gameBoard.putEmptyField(new Index(0, 0))),
//                () -> assertThrows(FieldException.class, () -> gameBoard.putEmptyField(new Index(1, 1))),
//                () -> assertThrows(FieldException.class, () -> gameBoard.putEmptyField(new Index(0, 0))),
//                () -> assertThrows(FieldException.class, () -> gameBoard.putEmptyField(new Index(4, 4))),
//                () -> assertThrows(FieldException.class, () -> gameBoard.putEmptyField(new Index(-1, 0))),
//                () -> assertThrows(FieldException.class, () -> gameBoard.putEmptyField(new Index(0, -1))),
//                () -> assertThrows(NullPointerException.class, () ->
//                        gameBoard.putEmptyField(new Index(null, null)))
//        );
//    }
//}