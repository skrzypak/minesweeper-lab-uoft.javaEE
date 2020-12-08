class App {
    init() {
        let baord = document.getElementById("board")
        for(let i = 1; i < 10; i++) {
            let row = document.createElement("div")
            row.classList.add("row")
            row.setAttribute("id", i)
            for(let j = 1; j < 10; j++) {
                let field = document.createElement("div")
                field.classList.add("field")
                field.classList.add("no-click")
                field.classList.add("no-select")
                field.onclick = function () {
                    console.log(`${i} | ${j} selected`)
                }
                field.oncontextmenu = function () {
                    console.log(`${i} | ${j} mark`)
                }
                row.appendChild(field)
            }
            baord.appendChild(row)
        }

        // let xhr = new XMLHttpRequest();
        // xhr.onreadystatechange = function() {
        //     if (xhr.readyState === 4) {
        //         let data = xhr.responseText;
        //         console.log(data)
        //     }
        // }
        // xhr.open('GET', '${pageContext.request.contextPath}/InitGame?height=5&width=5', true);
        // xhr.send(null);
    }
}