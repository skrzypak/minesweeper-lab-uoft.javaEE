class App {

    static onFieldClickAsSelect = (rowInx, colInx) => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                let json = xhr.responseText;
                let data = JSON.parse(json);
                console.log(data)
                //field.setAttribute("id", `R${i}C${j}`)
            }
        }
        xhr.open('GET', `${document.location.href}fieldClick?row=${rowInx}&col=${colInx}&btn=left`, true);
        xhr.send(null);
    }

    static onFieldClickAsMark = (rowInx, colInx) => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                let json = xhr.responseText;
                let data = JSON.parse(json);
                console.log(data)
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
                field.setAttribute("id", `R${i}C${j}`)
                field.onclick = () => {
                    App.onFieldClickAsSelect(i, j)
                }
                field.oncontextmenu = () => {
                    App.onFieldClickAsMark(i, j)
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