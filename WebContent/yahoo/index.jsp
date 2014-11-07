<%@ page import="com.harambesa.yahoo.YahooSession" %>
<%@ page import="net.oauth.OAuthProblemException" %>
<%@ page import="org.json.JSONObject" %><%
    YahooSession state = (YahooSession) request.getAttribute("yahooSession");
    if ("POST".equals(request.getMethod())) {
        String format = request.getParameter("format");
        try {
            if ("yql".equals(request.getParameter("form"))) {
                String method = request.getParameter("method");
                String url = request.getParameter("url");
                String callback = request.getParameter("callback");
                String query = request.getParameter("query");

                String[] params = null;
                if ("xml".equals(format)) {
                    response.setContentType("text/xml");
                    params = new String[] {"q", query, "format", format};
                } else {
                    response.setContentType("text/plain");
                    params = new String[] {"q", query, "format", format, "callback", callback};
                }
                String output = state.invokeString(url, method, params);
                out.println(output);
            } else if ("yap".equals(request.getParameter("form"))) {
                String method = request.getParameter("method");
                String url = request.getParameter("url");
                String content = request.getParameter("content");
                String[] params = {"format", format, "content", content};
                if ("xml".equals(format)) {
                    response.setContentType("text/xml");
                    out.println(state.invokeStringWithBody(url, method, content, params));
                } else {
                    response.setContentType("text/plain");
                    JSONObject object = new JSONObject(state.invokeStringWithBody(url, method, content, params));
                    out.println("JSON Object:");
                    out.println(object.toString(10));
                }
            } else {
                out.println("<html><body>unknown form</body></html>");
            }
        } catch (Exception e) {
            response.setContentType("text/plain");
            e.printStackTrace();
        }
    } else if ("GET".equals(request.getMethod()) && "true".equals(request.getParameter("clear"))) {
        state.clearSession(request, response);
    } else {
%>
<html>
<body>
<h1>YAP:</h1>

<a href="./test.jsp?clear=true">logout</a>

<br/>

<form name="yap" method="post" action="test.jsp">
    <input type="hidden" name="form" value="yap"/>
    Method:
    <select name="method">
        <option>GET</option>
        <option selected="true">PUT</option>
        <option>POST</option>
    </select>
    <br/>
    URL: <input name="url" type="text" size="75"
                value="http://social.yahooapis.com/v1/user/<%= state.getGUID() %>/presence/presence"/>
    <br/>
    Content: <textarea name="content" rows="10" cols="50">
    {
    "status": "Reading paper"
    }
</textarea>
    <br/>
    Format:
    <select name="format">
        <option>xml</option>
        <option>json</option>
    </select>
    <br/>
    <input type="submit"/>
</form>

<h1>YQL:</h1>

<form name="yql" method="post" action="test.jsp">
    <input type="hidden" name="form" value="yql"/>
    Method:
    <select name="method">
        <option>GET</option>
        <option>PUT</option>
        <option>POST</option>
    </select>
    <br/>
    URL: <input name="url" type="text" size="75" value="http://query.yahooapis.com/v1/yql"/>
    <br/>
    Query: <input name="query" type="text" size="75" value="select * from social.profile where guid=me"/>
    <br/>
    Format:
    <select name="format">
        <option>xml</option>
        <option>json</option>
    </select>
    <br/>
    Callback: <input name="callback" type="text" size="10" value="foo"/>
    <br/>
    <input type="submit"/>
</form>
</body>
</html>
<%
    }
%>