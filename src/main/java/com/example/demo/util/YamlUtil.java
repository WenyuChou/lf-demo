package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import static com.example.demo.util.SingleGrantTest.singleGrant;

/**
 * @author zhouwenyu
 * date 2021-11-09
 */
public class YamlUtil {

    public static void main1(String[] args) throws FileNotFoundException {
        //System.out.println(getRootUrlByChild("/dataReport/queryConsumeList/makeUp/22/45"));
        Set<String> set = null;
        Set<String> set1 = Optional.ofNullable(set).orElse(new HashSet<>());
        System.out.println(set1.size());
    }
    public static void testString(String... strings){
        System.out.println(JSON.toJSONString(strings));
    }
    public static List<String> getRootUrlByChild(String child) {
        List<String> rootList = new ArrayList<>();
        try (InputStream input = new FileInputStream("src/main/resources/static/permission.yaml")) {
            Yaml yaml = new Yaml();
            Map<String, List<String>> object = yaml.load(input);
            for (Map.Entry<String, List<String>> entry : object.entrySet()) {
                if (contains(entry.getValue(),child)) {
                    rootList.add(entry.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return rootList;
    }

    public static boolean contains(List<String> list, String str) {
        if (list.isEmpty()) {
            return false;
        }
        if (str == null) {
            return list.contains(null);
        }
        for (String s : list) {
            if (s != null && ((s.contains("*") && str.contains(s.replace("*", ""))) || s.equals(str))) {
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {
    }
}