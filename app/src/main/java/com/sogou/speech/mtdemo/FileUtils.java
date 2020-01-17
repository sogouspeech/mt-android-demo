package com.sogou.speech.mtdemo;

import android.text.TextUtils;

import com.sogou.sogocommon.utils.LogUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Date:2020/1/16
 * Author:zhangxiaobei
 * Describe:
 */
public class FileUtils {
    public static String ReadTxtFile(String filePath) {
        StringBuffer stringBuffer = new StringBuffer();
        File file = new File(filePath);
        if (file.isDirectory()) {
            LogUtil.e(filePath + " is directory");
        } else {
            try {
                InputStream inputStream = new FileInputStream(file);
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    //分行读取
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line).append("\n");
                    }
                    inputStream.close();
                }
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }
        }
        return stringBuffer.toString().trim();
    }


    public static void writeTxtFile(String translateResult, String filePath) {
        if (!TextUtils.isEmpty(translateResult) && !TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            try {
                OutputStream outputStream = new FileOutputStream(file);
                if (outputStream != null) {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(translateResult);
                    bufferedWriter.flush();
                    outputStream.close();
                    outputStreamWriter.close();
                    bufferedWriter.close();
                }
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }

        }
    }
}
