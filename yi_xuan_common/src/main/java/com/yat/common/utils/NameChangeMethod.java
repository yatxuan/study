package com.yat.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.*;

/**
 * <p>Description: 中文昵称转拼音 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/2 15:08
 */
public class NameChangeMethod {

    private static List<String> list;

    static {
        list = Arrays.asList("赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈",
                "楮", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏",
                "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章",
                "云", "苏", "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦",
                "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳",
                "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺",
                "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
                "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余",
                "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹",
                "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝",
                "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞",
                "熊", "纪", "舒", "屈", "项", "祝", "董", "梁", "杜", "阮",
                "蓝", "闽", "席", "季", "麻", "强", "贾", "路", "娄", "危",
                "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "锺", "徐",
                "丘", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍",
                "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房",
                "裘", "缪", "干", "解", "应", "宗", "丁", "宣", "贲", "邓",
                "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉",
                "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁",
                "荀", "羊", "於", "惠", "甄", "麹", "家", "封", "芮", "羿",
                "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫",
                "乌", "焦", "巴", "弓", "牧", "隗", "山", "谷", "车", "侯",
                "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫",
                "宁", "仇", "栾", "暴", "甘", "斜", "厉", "戎", "祖", "武",
                "符", "刘", "景", "詹", "束", "龙", "叶", "幸", "司", "韶",
                "郜", "黎", "蓟", "薄", "印", "宿", "白", "怀", "蒲", "邰",
                "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙",
                "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘",
                "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵",
                "冉", "宰", "郦", "雍", "郤", "璩", "桑", "桂", "濮", "牛",
                "寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农",
                "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连",
                "茹", "习", "宦", "艾", "鱼", "容", "向", "古", "易", "慎",
                "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿",
                "满", "弘", "匡", "国", "文", "寇", "广", "禄", "阙", "东",
                "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩",
                "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚",
                "那", "简", "饶", "空", "曾", "毋", "沙", "乜", "养", "鞠",
                "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红",
                "游", "竺", "权", "逑", "盖", "益", "桓", "公", "仉", "督",
                "晋", "楚", "阎", "法", "汝", "鄢", "涂", "钦", "归", "海",
                "岳", "帅", "缑", "亢", "况", "后", "有", "琴", "商", "牟",
                "佘", "佴", "伯", "赏", "墨", "哈", "谯", "笪", "年", "爱",
                "阳", "佟",
                "万俟", "司马", "上官", "欧阳", "夏侯",
                "诸葛", "闻人", "东方", "赫连", "皇甫",
                "尉迟", "公羊", "澹台", "公冶", "宗政",
                "濮阳", "淳于", "单于", "太叔", "申屠",
                "公孙", "仲孙", "轩辕", "令狐", "锺离",
                "宇文", "长孙", "慕容", "鲜于", "闾丘",
                "司徒", "司空", "丌官", "司寇", "南宫",
                "子车", "颛孙", "端木", "巫马", "公西",
                "漆雕", "乐正", "壤驷", "公良", "拓拔",
                "夹谷", "宰父", "谷梁", "段干", "百里",
                "东郭", "南门", "呼延", "羊舌", "微生",
                "梁丘", "左丘", "东门", "西门");

    }

    /**
     * 根据名字转换拼音 例如：王大锤转换为wangdachui
     *
     * @param src 名字
     * @return 名字的拼音
     */
    public static String getPingYin(String src) {

        char[] t1;
        t1 = src.toCharArray();
        String[] t2;
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder sb = new StringBuilder();
        try {
            for (char c : t1) {
                // 判断是否为汉字字符
                if (Character.toString(c).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(c, t3);
                    sb.append(t2[0]);
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 根据名字转换名字拼音的首字母 例如：王大锤转换为wdc
     *
     * @param str 名字
     * @return 名字拼音的首字母
     */
    public static String getPinYinHeadChar(String str) {

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                sb.append(pinyinArray[0].charAt(0));
            } else {
                sb.append(word);
            }
        }
        return sb.toString();
    }

    /** 将字符串转移为ASCII码 */
    public static String getCnAscii(String cnStr) {
        StringBuilder strBuf = new StringBuilder();
        byte[] bGbk = cnStr.getBytes();
        for (byte b : bGbk) {
            strBuf.append(Integer.toHexString(b & 0xff));
        }
        return strBuf.toString();
    }

    /**
     * 王大锤转换为wangdc
     *
     * @param oldName 名字
     * @return 名字的姓的全拼和名的首字母
     */
    public static String getNameChange1(String oldName) {
        return getPingYin(oldName.substring(0, 1)) +
                getPinYinHeadChar(oldName.substring(1));
    }

    /**
     * 王大锤转换为dc.wang
     *
     * @param oldName 名字
     * @return 名字的名的首字母和姓的全拼，中间一个.
     */
    public static String getNameChange2(String oldName) {
        return getPinYinHeadChar(oldName.substring(1)) +
                "." +
                getPingYin(oldName.substring(0, 1));
    }

    /**
     * 王大锤转换为dachui.wang
     *
     * @param oldName 名字
     * @return 名字的名的全拼和姓的全拼，中间一个.
     */
    public static String getNameChange3(String oldName) {
        StringBuilder sb = new StringBuilder();
        int num = 2;
        String substring = oldName.substring(0, num);
        if (!list.contains(substring)) {
            num = 1;
            substring = oldName.substring(0, num);
        }
        sb.append(getPingYin(oldName.substring(num)).toUpperCase());

        sb.append(".");
        sb.append(getPingYin(substring).toUpperCase());

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getPingYin("王大锤"));
        System.out.println(getPinYinHeadChar("王大锤"));
        System.out.println(getNameChange1("王大锤"));
        System.out.println(getNameChange2("王大锤"));
        System.out.println(getNameChange3("上官大锤"));
    }
}
