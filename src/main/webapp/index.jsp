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
            <div style="text-align: center">
                <span>Type F5 to start new game</span>
            </div>
        </div>
    </div>

    <script src="App.js"></script>
    <script>
        let app;
        window.onload = () => {
            app = new App()
            app.init();
        }
    </script>
</body>
</html>
