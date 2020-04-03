package com.yat.utils.image;

import com.yat.utils.convert.HexConverter;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;


/**
 * 图片工具
 *
 * @author YatXuan
 * @date 2019-03-01 18:00:17
 */
public class ImageUtil {

    /**
     * .分隔符
     */
    private static final String FILE_CONNECTOR_POINT = "\\.";

    /**
     * jpg格式图片文件头hex信息
     */
    private static final String JPG_HEX = "FFD8FF";
    /**
     * png格式图片文件头hex信息
     */
    private static final String PNG_HEX = "89504E47";
    /**
     * gif格式图片文件头hex信息
     */
    private static final String GIF_HEX = "47494638";
    /**
     * bmp格式图片文件头hex信息
     */
    private static final String BMP_HEX = "424D";

    /**
     * jpg格式
     */
    public static final String JPG = "jpg";
    /**
     * jpeg格式
     */
    public static final String JPEG = "jpeg";
    /**
     * png格式
     */
    public static final String PNG = "png";
    /**
     * gif格式
     */
    public static final String GIF = "gif";
    /**
     * bmp格式
     */
    public static final String BMP = "bmp";

    /**
     * 绘制图片
     *
     * @param bufferedImage 缓冲图片
     * @param type          图片类型
     * @param file          文件
     * @throws IOException
     */
    public static boolean drawSimpleImage(BufferedImage bufferedImage, String type, File file) throws IOException {
        Graphics g = bufferedImage.getGraphics();//获取图片画笔
        try {
            int backgroundX = 10;//背景x坐标
            int backgroundY = 40;//背景y坐标
            int backgroundWith = 180;//背景宽
            int backgroundHeight = 120;//背景高
            g.fillRect(backgroundX, backgroundY, backgroundWith, backgroundHeight);//填充背景，默认白色

            g.setColor(new Color(120, 120, 120));//设置画笔颜色

            int fontSize = 28;//字体大小
            g.setFont(new Font("宋体", Font.BOLD, fontSize));//设置字体

            int stringX = 10;//文字x坐标
            int stringY = 100;//文字y坐标
            g.drawString("绘制简单图片", stringX, stringY);
            return ImageIO.write(bufferedImage, type, file);
        } finally {
            g.dispose();//释放画笔
        }
    }

    /**
     * 裁剪原图，生成新的图片
     *
     * @param srcPath    原图地址
     * @param destPath   裁剪后的图片地址
     * @param x          裁剪开始x坐标
     * @param y          裁剪开始y坐标
     * @param width      裁剪宽度
     * @param height     裁剪高度
     * @param formatName 图片类型
     */
    public static boolean cutImage(String srcPath, String destPath, int x, int y, int width, int height, String formatName) {
        boolean cutSuccess = false;
        Iterator<ImageReader> ite = ImageIO.getImageReadersByFormatName(formatName);
        if (ite.hasNext()) {
            ImageReader reader = ite.next();
            InputStream is = null;
            try {
                is = new FileInputStream(srcPath);
                ImageInputStream iis = ImageIO.createImageInputStream(is);
                reader.setInput(iis);
                ImageReadParam defaultReadParam = reader.getDefaultReadParam();
                Rectangle rec = new Rectangle(x, y, width, height);
                defaultReadParam.setSourceRegion(rec);
                BufferedImage bi = reader.read(0, defaultReadParam);
                cutSuccess = ImageIO.write(bi, formatName, new File(destPath));
            } catch (IOException e) {
                System.out.println("cut image[" + srcPath + "] failed");
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new RuntimeException("不支持的格式[" + formatName + "]");
        }
        return cutSuccess;
    }

    /**
     * 判断文件后缀是否为图片文件格式,bmp|gif|jpg|jpeg|png 返回true
     *
     * @param imageFileSuffix 图片文件后缀名
     * @return
     */
    public static boolean isImageBySuffix(String imageFileSuffix) {
        if (StringUtils.isNotEmpty(imageFileSuffix)) {
            //[JPG, jpg, bmp, BMP, gif, GIF, WBMP, png, PNG, wbmp, jpeg, JPEG]
            String[] formatNames = ImageIO.getReaderFormatNames();
            if (ArrayUtils.isNotEmpty(formatNames)) {
                for (String formatName : formatNames) {
                    if (imageFileSuffix.toLowerCase().equals(formatName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断文件是否为图片文件格式, bmp|gif|jpg|jpeg|png 后缀的图片文件返回true
     *
     * @param imageName 图片文件名
     * @return
     */
    public static boolean isImageByFileName(String imageName) {
        return isImageBySuffix(getImageType(imageName));
    }

    /**
     * 判断文件是否为合法图片
     *
     * @param srcPath        图片文件绝对路径
     * @param checkImageName 是否校验图片文件名
     * @return
     */
    public static boolean isImage(String srcPath, boolean checkImageName) {
        if (checkImageName) {
            return isImageByFileName(srcPath);
        } else {
            if (StringUtils.isNotEmpty(srcPath)) {
                File imageFile = new File(srcPath);
                if (imageFile.isFile() && imageFile.length() > 0) {
                    return isImage(imageFile);
                }
            }
        }
        return false;
    }

    /**
     * 判断文件流是否为合法图片
     *
     * @param file 图片文件
     * @return
     */
    public static boolean isImage(File file) {
        return isImage((Object) file);
    }

    /**
     * 判断文件流是否为合法图片
     *
     * @param srcInputStream 图片文件的流
     * @return
     */
    public static boolean isImage(InputStream srcInputStream) {
        return isImage((Object) srcInputStream);
    }

    /**
     * 判断文件流是否为合法图片
     *
     * @param srcInputStream 图片文件的流
     * @return
     */
    public static boolean isImage(URL url) {
        return isImage((Object) url);
    }

    /**
     * 图片文件读取
     *
     * @param obj (URL|InputStream|File)
     * @return
     */
    private static boolean isImage(Object obj) {
        Image image = null;
        if (obj != null) {
            try {
                if (obj instanceof URL) {
                    image = ImageIO.read((URL) obj);
                } else if (obj instanceof InputStream) {
                    image = ImageIO.read((InputStream) obj);
                } else if (obj instanceof File) {
                    image = ImageIO.read((File) obj);
                } else {
                    throw new IllegalArgumentException("不支持这种类型[" + obj.getClass().getCanonicalName() + "]");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (image != null && image.getWidth(null) > 0 && image.getHeight(null) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取图片类型
     * JPG图片头信息:FFD8FF
     * PNG图片头信息:89504E47
     * GIF图片头信息:47494638
     * BMP图片头信息:424D
     *
     * @param is 图片文件流
     * @return 图片类型:jpg|png|gif|bmp
     */
    public static String getImageType(InputStream is) {
        String type = null;
        if (is != null) {
            byte[] b = new byte[4];
            try {
                is.read(b, 0, b.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String hexStr = HexConverter.byteArrayToHexString(b, true);//图片文件流前4个字节的头信息（子文字母）
            if (hexStr != null) {
                if (hexStr.startsWith(JPG_HEX)) {
                    type = JPG;
                } else if (hexStr.startsWith(PNG_HEX)) {
                    type = PNG;
                } else if (hexStr.startsWith(GIF_HEX)) {
                    type = GIF;
                } else if (hexStr.startsWith(BMP_HEX)) {
                    type = BMP;
                }
            }
        }
        return type;
    }

    /**
     * 获取图片类型
     * JPG图片头信息:FFD8FF
     * PNG图片头信息:89504E47
     * GIF图片头信息:47494638
     * BMP图片头信息:424D
     *
     * @param file 图片文件
     * @return 图片类型:jpg|png|gif|bmp
     * @throws FileNotFoundException 未找到文件
     */
    public static String getImageType(File file) throws FileNotFoundException {
        return getImageType(new FileInputStream(file));
    }

    /**
     * 根据图片名称或者图片所在绝对路径，获取图片类型
     *
     * @param imageNameOrPath 图片名称或者图片所在绝对路径
     * @return 图片类型（小写）， bmp|gif|jpg|jpeg|png
     */
    public static String getImageType(String imageNameOrPath) {
        String type = null;
        if (StringUtils.isNotEmpty(imageNameOrPath)) {
            String[] imageNames = imageNameOrPath.split(FILE_CONNECTOR_POINT);
            String suffix = imageNames[imageNames.length - 1];
            if (isImageBySuffix(suffix)) {
                type = suffix.toLowerCase();
            }
        }
        return type;
    }

    /**
     * 根据文件头，判断文件流是否为合法图片
     *
     * @param is 文件流
     * @return
     */
    public static boolean isImageByHeader(InputStream is) {
        return isImageBySuffix(getImageType(is));
    }

    /**
     * 根据文件头，判断文件流是否为合法图片
     *
     * @param file 图片文件
     * @return
     * @throws FileNotFoundException
     */
    public static boolean isImageByHeader(File file) throws FileNotFoundException {
        return isImageBySuffix(getImageType(file));
    }

    /**
     * 使用thumbnailator库，不修改图片尺寸，压缩图片占用内存
     * 支持所有格式图片，存在压缩后图片文件占用内存变大的情况
     *
     * @param srcPath  原文件路径
     * @param destPath 压缩后的文件路径
     * @param scale    压缩图的比例因子(0,1]
     * @param quality  图片质量，取值范围[0, 1]
     * @return
     * @throws IOException
     */
    public static boolean compressImageByThumbnails(String srcPath, String destPath, float scale, float quality) throws IOException {
        Thumbnails.of(srcPath).scale(scale).outputQuality(quality).toFile(destPath);
        return true;
    }

    /**
     * 使用JDK，不修改图片尺寸，压缩图片占用内存
     * 仅仅支持文件头是jpg格式图片(png格式图片压缩后背景颜色存在问题)
     * 存在压缩后图片文件占用内存变大的情况
     *
     * @param srcPath  原文件路径
     * @param destPath 压缩后的文件路径
     * @param quality  图片质量，取值范围[0, 1]
     * @return
     * @throws IOException
     */
    public static boolean compressImageByJdk(String srcPath, String destPath, float quality) {
        boolean isSuccess = false;
        File image = new File(srcPath);
        if (image.exists()) {
            String imageType = null;
            try {
                imageType = getImageType(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Iterator<ImageWriter> imageWriterIte = ImageIO.getImageWritersByFormatName(imageType);
            if (imageWriterIte != null && imageWriterIte.hasNext()) {

                ImageWriter writer = imageWriterIte.next();
                ImageWriteParam imgWriteParam;
                BufferedImage srcImage = null;
                FileOutputStream fos = null;
                try {
                    //写图片参数
                    imgWriteParam = writer.getDefaultWriteParam();
                    //必须指定压缩方式为MODE_EXPLICIT
                    imgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    //设置图片质量
                    imgWriteParam.setCompressionQuality(quality);
//					imgWriteParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
//					//图片颜色模式
//					ColorModel colorModel = ImageIO.read(image).getColorModel();
//					imgWriteParam.setDestinationType(new ImageTypeSpecifier(
//							colorModel, colorModel.createCompatibleSampleModel(16, 16)));

                    fos = new FileOutputStream(destPath);
                    writer.reset();
                    writer.setOutput(ImageIO.createImageOutputStream(fos));
                    srcImage = ImageIO.read(image);
                    writer.write(null, new IIOImage(srcImage, null, null), imgWriteParam);
                    isSuccess = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                            writer.dispose();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("文件不存在[ " + srcPath + "]");
        }
        return isSuccess;
    }

    /**
     * 重新生成图片宽、高
     *
     * @param srcPath   图片路径
     * @param destPath  新生成的图片路径
     * @param newWith   新的宽度
     * @param newHeight 新的高度
     * @param forceSize 是否强制使用指定宽、高,false:会保持原图片宽高比例约束
     * @return
     * @throws IOException
     */
    public static boolean resizeImage(String srcPath, String destPath, int newWith, int newHeight, boolean forceSize) throws IOException {
        if (forceSize) {
            Thumbnails.of(srcPath).forceSize(newWith, newHeight).toFile(destPath);
        } else {
            Thumbnails.of(srcPath).width(newWith).height(newHeight).toFile(destPath);
        }
        return true;
    }

    /**
     * 网络图片下载
     *
     * @param imageUrl        图片url
     * @param formatName 文件格式名称
     * @param localFile  下载到本地文件
     * @return 下载是否成功
     */
    public static boolean downloadImage(String imageUrl, String formatName, File localFile) {
        boolean isSuccess = false;
        URL url = null;
        try {
            url = new URL(imageUrl);
            isSuccess = ImageIO.write(ImageIO.read(url), formatName, localFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 携带头信息下载网络图片
     *
     * @param imageUrl        图片url
     * @param formatName 文件格式名称
     * @param localFile  下载到本地文件
     * @param headers    http协议交互中header信息，如Cookie
     * @return 下载是否成功
     */
    public static boolean downloadImageWithHeaders(String imageUrl, String formatName, File localFile, Map<String, String> headers) {
        boolean isSuccess = false;
        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            if (headers != null && !headers.isEmpty()) {
                //设置头信息
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.setDoInput(true);
            stream = conn.getInputStream();
            BufferedImage bufferedImg = ImageIO.read(stream);
            if (bufferedImg != null) {
                isSuccess = ImageIO.write(bufferedImg, formatName, localFile);
            } else {
                throw new RuntimeException("图片[" + imageUrl + "]下载失败");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 修改原图的文件格式
     *
     * @param srcPath    原图路径
     * @param destPath   新图路径
     * @param formatName 图片格式，支持bmp|gif|jpg|jpeg|png
     * @return
     */
    public static boolean modifyImageFormat(String srcPath, String destPath, String formatName) {
        boolean isSuccess = false;
        InputStream fis = null;
        try {
            fis = new FileInputStream(srcPath);
            BufferedImage bufferedImg = ImageIO.read(fis);
            isSuccess = ImageIO.write(bufferedImg, formatName, new File(destPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 合并两个图片
     *
     * @param img1Path   图片1地址
     * @param img2Path   图片2地址
     * @param x          图片2左上点在X轴起始坐标
     * @param y          图片2左上点在y轴起始坐标
     * @param destPath   合并两个图片生成新的图片地址
     * @param formatName 图片格式  bmp|gif|jpg|jpeg|png
     * @param maxBorder  ture:取两图最大边框;false:以图片1尺寸和坐标为准
     * @return
     * @throws IOException
     */
    public static boolean mergeImages(String img1Path, String img2Path,
                                      int x, int y, String destPath, String formatName, boolean maxBorder) throws IOException {
        boolean isSuccess = false;
        if (StringUtils.isNotEmpty(img1Path) && StringUtils.isNotEmpty(img2Path)) {
            BufferedImage img1 = ImageIO.read(new File(img1Path));//读取图片1
            BufferedImage img2 = ImageIO.read(new File(img2Path));//读取图片2
            if (!maxBorder) {//以图片1尺寸和坐标为准
                isSuccess = drawNewImageInImage1(img1, img2, x, y, destPath, formatName);
            } else {//取两图最大边框
                if (!needReptain(img1, img2, x, y)) {
                    isSuccess = drawNewImageInImage1(img1, img2, x, y, destPath, formatName);
                } else {
                    int w1 = img1.getWidth();
                    int w2 = img2.getWidth();
                    int wx1 = w1 - x;

                    int h1 = img1.getHeight();
                    int h2 = img2.getHeight();
                    int hy1 = h1 - y;
                    //创建背景图片
                    BufferedImage blankImage = new BufferedImage(wx1 >= w2 ? (x >= 0 ? w1 : w1 - x) : (x >= 0 ? w2 + x : w2),
                            hy1 > h2 ? (y >= 0 ? h1 : h1 - y) : (y >= 0 ? h2 + y : h2), BufferedImage.TYPE_INT_RGB);
                    //画图片1、图片2
                    Graphics blankImgGraphics = null;
                    try {
                        blankImgGraphics = blankImage.getGraphics();
                        blankImgGraphics.drawImage(img1, x < 0 ? -1 * x : 0, y < 0 ? -1 * y : 0, img1.getWidth(), img1.getHeight(), null);
                        blankImgGraphics.drawImage(img2, x < 0 ? 0 : x, y < 0 ? 0 : y, img2.getWidth(), img2.getHeight(), null);
                        isSuccess = ImageIO.write(blankImage, formatName, new File(destPath));
                    } finally {
                        if (blankImgGraphics != null) {
                            blankImgGraphics.dispose();
                        }
                    }
                }
            }
        }
        return isSuccess;
    }

    /**
     * 在图片1中画图片2
     *
     * @param x          X坐标位置
     * @param y          Y坐标位置
     * @param destPath   新图片存储位置
     * @param formatName 图片格式
     * @param img1       图片1
     * @param img2       图片2
     * @return
     * @throws IOException
     */
    private static boolean drawNewImageInImage1(BufferedImage img1, BufferedImage img2,
                                                int x, int y, String destPath, String formatName) throws IOException {
        boolean isSuccess;
        Graphics img1Graphics = null;
        try {
            img1Graphics = img1.getGraphics();
            img1Graphics.drawImage(img2, x, y, img2.getWidth(), img2.getHeight(), null);
            isSuccess = ImageIO.write(img1, formatName, new File(destPath));
        } finally {
            if (img1Graphics != null) {
                img1Graphics.dispose();
            }
        }
        return isSuccess;
    }

    /**
     * 判断是否需要重新画最大边框的图片背景
     *
     * @param img1 图片1
     * @param img2 图片2
     * @param x    图片2起始X坐标
     * @param y    图片2起始Y坐标
     * @return
     */
    private static boolean needReptain(BufferedImage img1, BufferedImage img2,
                                       int x, int y) {
        if (img1 == null || img2 == null || img1.getWidth() <= 0 || img1.getHeight() <= 0
                || img2.getWidth() <= 0 || img2.getHeight() <= 0) {
            throw new IllegalArgumentException("图片信息不正确");
        }
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();
        return x < 0 || y < 0 || w1 - x < w2 || h1 - y < h2;
    }

    /**
     * 在图片右下角添加白色文字水印
     *
     * @param is
     * @param os
     * @param text
     * @throws IOException
     */
    public static void makeWatermark(InputStream is, OutputStream os, String text, String format) throws IOException {
        BufferedImage image = ImageIO.read(is);
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();

            //计算字体大小
            int fontSize = (int) (width * height * 0.000008 + 13);
            Font font = new Font("宋体", Font.PLAIN, fontSize);

            Graphics2D g = image.createGraphics();
            g.setFont(font);
            g.setColor(Color.white); //水印颜色-白色

//			// 透明度
//			float alpha = 0.9f;
//			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            int x = width - getWatermarkLength(text, g) - 10;
            x = x < 0 ? 0 : x;
            int y = height - 10;
            y = y < 0 ? 0 : y;

            //对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawString(text, x, y);
            g.dispose();

            ImageIO.write(image, format, os);
        }
    }

    /**
     * 获取水印文字总长度
     *
     * @param text 水印的文字
     * @param g
     * @return 水印文字总长度
     */
    private static int getWatermarkLength(String text, Graphics g) {
        return g.getFontMetrics(g.getFont()).charsWidth(
                text.toCharArray(), 0, text.length());
    }
}

