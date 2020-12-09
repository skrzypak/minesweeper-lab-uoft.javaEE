class App {

    static onFieldLeftClick = (rowInx, colInx) => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                let json = xhr.responseText;
                let data = JSON.parse(json);
                if(data.error === undefined) {
                    let f = document.getElementById(`R${rowInx}C${colInx}`)
                    if(data.gameStatus === "NONE" || data.gameStatus === "WIN") {
                        f.classList.remove("no-click");
                        f.classList.remove("no-select");
                        f.classList.add("click-empty");
                        f.innerHTML = "<span>"+data.numOfMines+"</span>";
                    } else if(data.gameStatus === "LOSE") {
                        f.classList.remove("no-click");
                        f.classList.remove("no-select");
                        f.classList.add("click-mine");
                    }
                    if (data.gameStatus !== "NONE") {
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

    static onFieldRightClick = (rowInx, colInx) => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                let json = xhr.responseText;
                let data = JSON.parse(json);
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

    static renderBoard = (rows, cols) => {
        let baord = document.getElementById("board")
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

    init() {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                let json = xhr.responseText;
                let data = JSON.parse(json);
                App.renderBoard(data.height, data.width);
            }
        }
        xhr.open('GET', `${document.location.href}/InitGame?height=5&width=5`, true);
        xhr.send(null);
    }
}