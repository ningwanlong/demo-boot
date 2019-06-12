<%--
  Created by IntelliJ IDEA.
  User: nnn
  Date: 2019/5/31
  Time: 9:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>管理员登陆</title>
    <meta charset="UTF-8">
    <title>管理员登录-WeAdmin Frame型后台管理系统-WeAdmin 1.0</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="shortcut icon" href="${MYFILE_PREFIX_MAP.COMM_JS}/WeAdmin/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="${MYFILE_PREFIX_MAP.COMM_JS}/WeAdmin/static/css/font.css">
    <link rel="stylesheet" href="${MYFILE_PREFIX_MAP.COMM_JS}/WeAdmin/static/css/weadmin.css">
    <%--<script src="${MYFILE_PREFIX_MAP.COMM_JS}/WeAdmin/static/js/jquery.min.js" type="text/javascript"></script>--%>
    <script src="${MYFILE_PREFIX_MAP.COMM_JS}/WeAdmin/lib/layui/layui.js" charset="utf-8"></script>
</head>
<body class="login-bg">

<div class="login">
    <div class="message">WeAdmin 1.0-管理后台登录</div>
    <div id="darkbannerwrap"></div>

    <form method="POST" class="layui-form" action="loginAuth">
        <c:choose>
            <c:when test="${empty loginName}">
                <input name="username" placeholder="用户名"  type="text" lay-verify="required" class="layui-input" >
            </c:when>
            <c:otherwise>
                <input name="username" placeholder="用户名"  value="${loginName}"  type="text" lay-verify="required" class="layui-input" >
            </c:otherwise>
        </c:choose>
        <hr class="hr15">
        <c:choose>
            <c:when test="${empty password}">
                <input name="password" lay-verify="required" placeholder="密码"  type="password" class="layui-input">
            </c:when>
            <c:otherwise>
                <input name="password" lay-verify="required" placeholder="密码" value="${password}"  type="password" class="layui-input">
            </c:otherwise>
        </c:choose>
        <hr class="hr15">
        <input class="loginin" value="登录" lay-submit lay-filter="login" style="width:100%;" type="submit">
        <hr class="hr20" >
        <div>
            <c:choose>
                <c:when test="${!empty errorMsg}">
                    <p style="color:red">${ errorMsg}</p>
                </c:when>
                <c:otherwise>
                    请输入您的用户名和密码登陆！
                </c:otherwise>
            </c:choose>
        </div>
    </form>
</div>
<script type="text/javascript">

    layui.extend({
        admin: '${MYFILE_PREFIX_MAP.COMM_JS}/WeAdmin/static/js/admin'
    });
    layui.use(['form','admin'], function(){
        var form = layui.form
            ,admin = layui.admin;
        // layer.msg('玩命卖萌中', function(){
        //   //关闭后的操作
        //   });
        //监听提交
       /* form.on('submit(login)', function(data){
            alert(888)
            layer.msg(JSON.stringify(data.field),function(){
                location.href='./index.html'
            });
            return false;
        });*/
    });
</script>
<!-- 底部结束 -->
</body>
</html>
