<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="UTF-8">
    <title>PPT预览</title>
    <#--<link rel="stylesheet" href="css/base.css">
    <link rel="stylesheet" href="css/MPreview.css">-->
    <link rel="stylesheet" href="css/viewer.min.css">
</head>
<body>
<div class="wrapper">
    <div class="ppt" id="ppt" style="width: 640px;height: 480px;"></div>
</div>
<script type="text/javascript" src="js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="js/viewer.min.js"></script>

<#--<script type="text/javascript" src="js/MPreviewPPT.js"></script>
<script type="text/javascript">
    var data = ['C:/tmp/conventer/f330fd64c1eb52a3b06cf904bcc3307655693b1274d09be13a32bd8199058ae6/resource/img0.jpg','./upload/sorry.jpg','upload/MPreview_PPT_1.jpg'];
    $('#ppt').MPreviewPPT({
        data: data,                             //包含所有图片地址的数组，如填写则不发送Ajax。用于直接传入数据，方便调用。
        loadSize: 5,                            //每次加载几张图片
        scrollFix: 5,                           //当前默认滚动条距离外容器的边距
        minScrollHeight: 20                     //当前滚动条按钮最小高度
    });
</script>-->

<ul id="jq22">
    <li><img src="C:\tmp\conventer\f330fd64c1eb52a3b06cf904bcc3307655693b1274d09be13a32bd8199058ae6\resource" alt="图片1"></li>
    <#--<li><img src="img/tibet-2.jpg" alt="图片2"></li>
    <li><img src="img/tibet-3.jpg" alt="图片3"></li>
    <li><img src="img/tibet-4.jpg" alt="图片4"></li>
    <li><img src="img/tibet-5.jpg" alt="图片5"></li>
    <li><img src="img/tibet-6.jpg" alt="图片6"></li>-->
</ul>

</body>

<script>
    var viewer = new Viewer(document.getElementById('jq22'), {
        url: 'data-original'
    });
</script>
</html>
