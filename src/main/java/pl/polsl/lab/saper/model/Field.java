package pl.polsl.lab.saper.model;

/**
 * Class define board field
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class Field extends Index {

    private Boolean mine;           // If true, field is mine
    private Boolean marked;         // If true, user mark field as mine
    private Boolean selected;       // If true, field was selected by user
    private Integer aroundMines;    // Container number of mines around field, update when user select field

    /**
     * Class constructor
     *
     * @param inx field index object
     */
    public Field(Index inx) {
        super(inx.getRowIndex(), inx.getColIndex());
        this.mine = false;
        this.marked = false;
        this.selected = false;
        this.aroundMines = 0;
    }

    /**
     * Method set field as mine
     */
    public void setAsMine() {
        this.mine = true;
    }


    /**
     * Method mark set field as marked by player
     */
    public void setFieldAsMark() {
        this.marked = true;
    }

    /**
     * Method set field as selected
     *
     * @param num number of mine around field
     * @return false when filed was selected earlier, otherwise true
     */
    public boolean setFieldAsSelected(Integer num) {
        if (!this.selected) {
            this.aroundMines = num;
            this.selected = true;
            return true;
        }
        return false;
    }

    /**
     * Method return info about mine
     *
     * @return true if filed is mine, otherwise false
     */
    public boolean isMine() {
        return this.mine;
    }

    /**
     * Method return info about selected proper value field
     *
     * @return true if selected earlier, otherwise false
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Method return info about marked proper value field
     *
     * @return true if marked by player, otherwise false
     */
    public boolean isMarked() {
        return this.marked;
    }

    /**
     * Method return info about number of mines around field
     * Remember that number of mines is update during call setFieldAsSelected(Integer num)
     *
     * @return number of mines around field if field was selected, otherwise 0
     */
    public Integer getNumOfMinesAroundField() {
        return this.aroundMines;
    }

    /**
     * Method remove mark from filed
     */
    public void removeFieldMark() {
        this.marked = false;
    }
}
