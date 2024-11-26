<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>북마크 그룹 추가</title>
</head>
<body>
<form action="bookmark_group_add_action.jsp" method="post">
    <label for="name">북마크 이름:</label>
    <input type="text" name="name" id="name" required>
    <label for="orderNo">순서:</label>
    <input type="number" name="orderNo" id="orderNo" required>
    <button type="submit">추가</button>
</form>
<a href="bookmark_group.jsp">뒤로가기</a>
</body>
</html>
