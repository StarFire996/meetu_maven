package com.meetu.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.ListLogStoresRequest;
import com.aliyun.openservices.log.request.PutLogsRequest;

public class LoggerUtils {

    public static String endpoint = "cn-beijing.log.aliyuncs.com"; // 选择与上面步骤创建Project所属区域匹配的//
                                                                   // Endpoint

    public static String accessKeyId = "XWp6VLND94vZ8WNJ"; // 使用你的阿里云访问秘钥AccessKeyId

    public static String accessKeySecret = "DSi9RRCv4bCmJQZOOlnEqCefW4l1eP"; // 使用你的阿里云访问秘钥AccessKeySecret

    public static String project = "app-iu"; // 上面步骤创建的项目名称

    public static String logstore = "biubiu-log"; // 上面步骤创建的日志库名称

    public static void setLogger(String loggerName,List debugList) {
        // 构建一个客户端实例
        try {
            Client client = new Client(endpoint, accessKeyId, accessKeySecret);
            // 列出当前Project下的所有日志库名称
            int offset = 0;
            int size = 100;
            String logStoreSubName = "";
            ListLogStoresRequest req1 = new ListLogStoresRequest(project, offset, size, logStoreSubName);
            ArrayList<String> logStores = client.ListLogStores(req1).GetLogStores();

            // 写入日志
            String topic = "";
            String source = "";
            // 连续发送10个数据包，每个数据包有10条日志
            Vector<LogItem> logGroup1 = new Vector<LogItem>();

            LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
            
            StringBuilder sb = new StringBuilder().append("0");
            
            if (debugList.size()>0) {
            	for (int i = 0; i < debugList.size(); i++) {
            		sb.append("__").append(debugList.get(i));
				}
            }

            logItem.PushBack(loggerName, sb.toString());
            logGroup1.add(logItem);
            PutLogsRequest req2 = new PutLogsRequest(project, logstore, topic, source, logGroup1);
            client.PutLogs(req2);
        } catch (LogException e) {
            e.printStackTrace();
        }
    }
}
