package com.tyy.uml.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.tyy.uml.core.exception.ServiceException;

public class SystemUtils {

    public static boolean is(Boolean b) {
        return b != null && b.booleanValue();
    }

    public static boolean isEmpty(Object v) {
        return isEmpty(v, true);
    }

    /**
     * 判断对象是否为空, 其中String、Collection、Map、Array会判断是否为空内容, 其他对象都为判断是否为null
     * 
     * @param v
     *            : 判断对象
     * @param trim
     *            : 如果判断对象类型为String类型时, 设置是否先trim()再判断
     * @return
     */
    public static boolean isEmpty(Object v, boolean trim) {
        if (v == null)
            return true;
        if (v instanceof String) {
            String sv = (String) v;
            return trim ? sv.trim().length() == 0 : sv.length() == 0;
        } else if (v instanceof Collection) {
            Collection<?> c = (Collection<?>) v;
            return c.size() == 0;
        } else if (v instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) v;
            return m.size() == 0;
        } else if (isArray(v.getClass())) { return getArrayLength(v) == 0; }
        return false;
    }

    /**
     * 是否是数组或java.util.List
     * 
     * @param c
     */
    public static boolean isArray(Class<?> c) {
        return c.isArray() || List.class.isAssignableFrom(c);
    }

    /**
     * 获取数组或List长度
     * 
     * @param array
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static int getArrayLength(Object array) {
        return array == null ? 0 : (array instanceof List) ? ((List) array).size() : Array.getLength(array);
    }

    public static final String humpNameToUpperUnderline(String str) {
        String r = humpNameToUnderline(str);
        return r == null ? null : r.toUpperCase();
    }

    public static final String humpNameToLowerUnderline(String str) {
        String r = humpNameToUnderline(str);
        return r == null ? null : r.toLowerCase();
    }

    public static final String humpNameToUnderline(String str) {
        if (isEmpty(str)) { return str; }
        StringBuffer sBuf = new StringBuffer();
        sBuf.append(str.charAt(0));
        for (int i = 1; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                sBuf.append("_");
            }
            sBuf.append(str.charAt(i));
        }
        return sBuf.toString();
    }

    /**
     * 将source的属性拷贝到targetCls的实例中，targetCls必须有空参数构造方法
     *
     * @param source
     * @param targetCls
     * @return
     */
    public static <T> T converBean(Object source, Class<T> targetCls) {
        if (source == null) { return null; }
        T target = null;
        try {
            target = targetCls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("实体转换失败");
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <T> List<T> asListFilterNull(@SuppressWarnings("unchecked") T... r) {
        if (r == null) { return new ArrayList<T>(0); }
        List<T> tm = new ArrayList<T>();
        for (T t : r) {
            if (t != null) {
                tm.add(t);
            }
        }
        return tm;
    }

    public static <T> Set<T> asSetFilterNull(@SuppressWarnings("unchecked") T... r) {
        if (r == null) { return new HashSet<T>(0); }
        Set<T> tm = new HashSet<T>();
        for (T t : r) {
            if (t != null) {
                tm.add(t);
            }
        }
        return tm;
    }

    /**
     * 拷贝beans
     *
     * @param sources
     * @param targetCls
     * @return
     */
    public static <T> List<T> converBean(Collection<?> sources, Class<T> targetCls) {
        List<T> targets = new LinkedList<>();
        if (sources != null && sources.size() > 0) {
            for (Object source : sources) {
                T target = converBean(source, targetCls);
                targets.add(target);
            }
        }
        return targets;
    }

    // public static <T> Page<T> converPage(Page<?> page, List<T> data) {
    // return new Page<>(page.getPageNum(), page.getPageSize(), page.getTotalRows(), page.getTotalPages(), data);
    // }

    public static String md5(String... source) {
        if (source == null || source.length == 0) { return null; }
        StringBuilder sourceBuf = new StringBuilder();
        for (String s : source) {
            sourceBuf.append(s);
        }
        StringBuilder sbuf = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(sourceBuf.toString().getBytes());
            for (byte b : md5.digest()) {
                sbuf.append(String.format("%02X", b)); // 10进制转16进制，X 表示以十六进制形式输出，02 表示不足两位前面补0输出
            }
            return sbuf.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) { return cookie.getValue(); }
        }
        return null;
    }

    public static void checkEmpty(Object val, String msg) {
        if (isEmpty(val)) { throw new ServiceException(msg + "不可为空!"); }
    }

    public static <T> T readFile(File file, Class<? extends T> cls) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file.toPath());) {
            String string = IOUtils.toString(inputStream, "UTF-8");
            return JSON.parseObject(string, cls);
        }
    }

    public static void writeFile(File file, Object obj) throws IOException {
        String string = JSON.toJSONString(obj, JSONWriter.Feature.PrettyFormat);
        file.getParentFile().mkdirs();
        try (OutputStream newOutputStream = Files.newOutputStream(file.toPath())) {
            newOutputStream.write(string.getBytes("UTF-8"));
            newOutputStream.flush();
        }
    }

    public static String cnToUnicode(String cn) {
        char[] charArray = cn.toCharArray();
        StringBuffer r = new StringBuffer();
        for (int i = 0; i < charArray.length; i++) {
            r.append("\\u").append(Integer.toString(charArray[i], 16));
        }
        return r.toString();
    }

    public static String unicodeToCn(String u) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(u);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            u = u.replace(matcher.group(1), ch + "");
        }
        return u;
    }

}
