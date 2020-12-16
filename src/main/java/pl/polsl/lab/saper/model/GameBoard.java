package pl.polsl.lab.saper.model;

import pl.polsl.lab.saper.exception.FieldException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**
 * Class contain game board data
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class GameBoard {

    private final Integer numOfRows;         // Game board height, user input height + 2
    private final Integer numOfCols;         // Game board width, user input width + 2
    /**
     * The Fields.
     */
    ArrayList<Field> fields;                 // Game fields

    /**
     * Class constructor.
     *
     * @param height new board height;
     * @param width  new board width;
     */
    public GameBoard(Integer height, Integer width) {
        this.fields = new ArrayList<>();
        this.numOfRows = height + 2;
        this.numOfCols = width + 2;
    }

    /**
     * Get num of board rows
     *
     * @return num of rows
     */
    public Integer getNumOfRows() {
        return this.numOfRows;
    }

    /**
     * Get num of board columns
     *
     * @return num of columns
     */
    public Integer getNumOfCols() {
        return this.numOfCols;
    }

    /**
     * Get field data from index
     *
     * @param inx field index object
     * @return field data if found, otherwise null
     */
    public Field get(Index inx) {
        Optional<Field> fieldOptional = this.fields.stream()
                .filter(f -> f.getRowIndex().equals(inx.getRowIndex()))
                .filter(f -> f.getColIndex().equals(inx.getColIndex()))
                .findFirst();
        return fieldOptional.orElse(null);
    }

    /**
     * Get all fields data
     * @return fields arr reference
     */
    public ArrayList<Field> getFields() {return this.fields;}

    /**
     * Generate new empty field with index and putEmptyField to board
     *
     * @param inx field index object
     * @throws FieldException if field value is game board border or out of range
     */
    public void putEmptyField(Index inx) throws FieldException {
        if (inx.getRowIndex() < 0 || inx.getRowIndex() >= this.numOfRows)
            throw new FieldException("Row index out of range of board height");
        if (inx.getColIndex() < 0 || inx.getColIndex() >= this.numOfCols)
            throw new FieldException("Column index out of range of board  width");
        Optional<Field> optional = this.fields.stream()
                .filter(f -> f.getRowIndex().equals(inx.getRowIndex()))
                .filter(f -> f.getColIndex().equals(inx.getColIndex()))
                .findFirst();
        if (optional.isPresent()) {
            throw new FieldException("Field with this index exists");
        }
        this.fields.add(new Field(inx));
    }

    /** Simple id
     * @return id
     * */
    public Integer getId() {
        return 0;
    }
}
