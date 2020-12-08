<%--
  Created by IntelliJ IDEA.
  User: Xboot
  Date: 03.12.2020
  Time: 11:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>"Minesweeper" - Java lab 4 </title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div id="container">
        <div id="content">
            <div id="board" oncontextmenu="return false">
            </div>
            <div id="buttonPanel">
                <form action="InitGame" id="fInputMenu">
                    <input type="submit" value="Start new game" />
                </form>
                <form action="RawInputServlet" id="fInputRaw">
                    <label style="width: 100%">
                        <input type="text" id="inputRaw" />
                    </label>
                </form>
            </div>
        </div>
    </div>

    <script src="App.js"></script>
    <script>
        window.onload = () => {
            let app = new App()
            app.init();
        }
    </script>
</body>
</html>
