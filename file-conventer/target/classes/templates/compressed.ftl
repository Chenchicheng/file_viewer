<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>layer-更懂你的web弹窗解决方案</title>

    <style>
        html{background-color:#E3E3E3; font-size:14px; color:#000; font-family:'微软雅黑'}

    </style>

    <script src="http://cdn.bootcss.com/jquery/1.12.3/jquery.min.js"></script>
    <script src="layer/layer.js"></script>
</head>
<body>

<script>
    ;!function(){


//页面一打开就执行，放入ready是为了layer所需配件（css、扩展模块）加载完毕
        layer.ready(function(){
            layer.open({
                type: 2,
                scrollbar: true,
                maxmin: true,
                area: ['800px', '500px'],
                content: ['viewer/document/${pathId}']
            });
        });


    }();
</script>
</body>
</html>