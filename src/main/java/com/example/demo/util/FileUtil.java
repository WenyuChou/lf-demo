package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author zhouwenyu
 * date 2022-11-04
 */
public class FileUtil {
    public static JSONObject readJson() throws Exception {
        File filePath = new File("/Users/admin/Documents/java/idea_project/demo/src/main/resources/data.json");
        //读取文件
        String input = FileUtils.readFileToString(filePath, "UTF-8");
        //将读取的数据转换为JSONObject
        return JSONObject.parseObject(input);
    }

    public static void writeJson(JSONObject json) throws Exception {
        BufferedWriter writer = null;
        try {
            File filePath = new File("/Users/admin/Documents/java/idea_project/demo/src/main/resources/data.json");
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(json.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
