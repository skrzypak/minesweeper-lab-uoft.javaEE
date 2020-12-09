/***
 * Main javascript class
 */
class App {

    /***
     * Execute left click mouse button
     * @param rowInx field row index
     * @param colInx field col index
     */
    static onFieldLeftClick = (rowInx, colInx) => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                let json = xhr.responseText;
                let data = JSON.parse(json);
                //console.log(data)
                if(data.error === undefined) {
                    let f = document.getElementById(`R${rowInx}C${colInx}`)
                    if(data.gameStatus === "NONE" || data.gameStatus === "WIN") {
                        f.classList.remove("no-click");
                        f.classList.remove("no-select");
                        f.classList.add("click-empty");
                        f.innerHTML = "<span>"+data.numOfMines+"</span>";
                        if(data.zero !== undefined) {
                            let z = JSON.parse(data.zero);
                            for (const [key, value] of Object.entries(z)) {
                                let k = JSON.parse(key)
                                let fi = document.getElementById(`R${k.row}C${k.col}`)
                                fi.classList.remove("no-click");
                                fi.classList.remove("no-select");
                                fi.classList.add("click-empty");
                                fi.innerHTML = "<span>"+value+"</span>";
                            }
                        }
                        if(data.gameStatus === "WIN")
                            alert(data.gameStatus)
                    } else if(data.gameStatus === "LOSE") {
                        let mines = JSON.parse(data.mines);
                        mines.map(el => {
                            let fi = document.getElementById(`R${el.row}C${el.col}`)
                            fi.classList.remove("no-click");
                            fi.classList.remove("no-select");
                            fi.classList.add("click-mine");
                        })
                        alert(data.gameStatus)
                    } else {
                        alert(data.gameStatus)
                    }
                } else{
                    alert(data.error)
                }

            }
        }
        xhr.open('GET', `${document.location.href}fieldClick?row=${rowInx}&col=${colInx}&btn=left`, true);
        xhr.send(null);
    }

    /***
     * Execute right click mouse button
     * @param rowInx field row index
     * @param colInx field col index
     */
    static onFieldRightClick = (rowInx, colInx) => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                let json = xhr.responseText;
                let data = JSON.parse(json);
                //console.log(data)
                let f = document.getElementById(`R${rowInx}C${colInx}`)
                if(data.error === undefined) {
                    if(data.mark === "true") {
                        f.classList.remove("no-click");
                        f.classList.add("click-right");
                    } else {
                        f.classList.remove("click-right");
                        f.classList.add("no-click");
                    }
                }
            }
        }
        xhr.open('GET', `${document.location.href}fieldClick?row=${rowInx}&col=${colInx}&btn=right`, true);
        xhr.send(null);
    }

    /***
     * Render board html
     * @param rows number of rows
     * @param cols number of cols
     */
    static renderBoard = (rows, cols) => {
        let baord = document.getElementById("board")
        while (baord.lastElementChild) {
            baord.removeChild(baord.lastElementChild);
        }
        for(let i = 1; i < rows - 1; i++) {
            let row = document.createElement("div")
            row.classList.add("row")
            row.setAttribute("id", i)
            for(let j = 1; j < cols - 1; j++) {
                let field = document.createElement("div")
                field.classList.add("field")
                field.classList.add("no-click")
                field.classList.add("no-select")
                field.style.lineHeight = `50px`
                field.style.height = `50px`
                field.style.width = `50px`
                field.setAttribute("id", `R${i}C${j}`)
                field.onclick = () => {
                    App.onFieldLeftClick(i, j)
                }
                field.oncontextmenu = () => {
                    App.onFieldRightClick(i, j)
                }
                row.appendChild(field)
            }
            baord.appendChild(row)
        }
    }

    /***
     * Start new game.
     * @warning method get last correct input data from cookies
     */
    init() {

        let cXhr = new XMLHttpRequest();
        cXhr.onreadystatechange = function() {
            if (cXhr.readyState === 4) {
                let oldData = JSON.parse(cXhr.responseText);
                let height = prompt("Enter height", oldData.lastHeight)
                let width = prompt("Enter width", oldData.lastWidth)
                let xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function() {
                    if (xhr.readyState === 4) {
                        let json = xhr.responseText;
                        let data = JSON.parse(json);
                        if(data.error === undefined) {
                            let size = JSON.parse(data.size)
                            App.renderBoard(size.height, size.width);
                        }
                        else alert(data.error)
                    }
                }

                xhr.open('POST', `${document.location.href}InitGame?`);
                xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                xhr.send(`height=${height}&width=${width}`);

            }
        }
        cXhr.open('GET', `${document.location.href}cookies?`);
        cXhr.send(null);
    }
}