## Minio 图片永久访问的问题处理,需要到 Minio的管理页面设置桶的属性
## 1-1、创建桶：登录后台管理界面，在右下角有个红色的加号图标，点击它，然后会看到下面的效果
## 1-2、点击create bucket ,输入要创建的bucket,如：pic
## 1-3、点击那三个点，选择Edit policy ，会弹出下面的弹窗。
## 1-4、在Prefix中输入* ，即允许所有访问。右边的选项选择 Read and write。
## 1-5、然后点击Add 按钮就可以了。
## 图片参考在image文件夹中