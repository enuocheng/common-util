package com.example.util.commonutil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * HTTP工具类
 *
 * @author
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 定义编码格式 UTF-8
     */
    public static final String URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";

    /**
     * 定义编码格式 GBK
     */
    public static final String URL_PARAM_DECODECHARSET_GBK = "GBK";

    private static final String URL_PARAM_CONNECT_FLAG = "&";

    private static final String EMPTY = "";

    /**
     * 问号
     */
    public static final String QUESTION_MARK_SYMBOL = "?";

    /**
     * 逗号
     */
    public static final String COMMA_SYMBOL = ",";

    /**
     * 点
     */
    public static final String POINT_SYMBOL = ".";

    /**
     * 与
     */
    public static final String AND_SYMBOL = "&";

    /**
     * 横线
     */
    public static final String LINE_SYMBOL = "-";

    /**
     * 等号
     */
    public static final String EQUAL_SYMBOL = "=";

    /**
     * 注释标识
     */
    public static final String IGNORE_SYMBOL = "#";

    /**
     * 空格
     */
    public static final String SPACE_SYMBOL = " ";

    /**
     * 所有
     */
    public static final String ALL_SYMBOL = "*";

    /**
     * 下划线
     */
    public static final String UNDERLINE_SYMBOL = "_";

    /**
     * 引号
     */
    public static final String QUOTES_SYMBOL = "\"";

    /**
     * 斜杆
     */
    public static final String DIAGONAL_SYMBOL = "/";

    /**
     * 冒号
     */
    public static final String COLON_SYMBOL = ":";

    private static MultiThreadedHttpConnectionManager connectionManager = null;

    private static int connectionTimeOut = 25000;

    private static int socketTimeOut = 25000;

    private static int maxConnectionPerHost = 20;

    private static int maxTotalConnections = 20;

    private static HttpClient client;

    static {
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
    }

    public static String sendURLPost(String url) {
        return sendURLPost(url, null);
    }


    /**
     * POST方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据 map 转 jsonString
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String sendURLPost(String url, String params) {
        return sendURLPost(url, params, null);
    }

    public static String absoluteURLPost(String url, Map<String, Object> params, String enc) {
        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            if (enc == null || ("").equals(enc)) {
                //默认
                enc = URL_PARAM_DECODECHARSET_UTF8;
            }
            postMethod.setRequestHeader("Content-Type", "application/json;charset=" + enc);
            //将表单的值放入postMethod中
            if (params != null) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    Object value = params.get(key);
                    if (value != null) {
                        postMethod.addParameter(key, value.toString());
                    }
                }
            }
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
            } else {
                logger.info("url:{} 响应状态码:{}", url, postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", e);
        } catch (IOException e) {
            logger.error("发生网络异常:{}", e);
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return response;
    }

    /**
     * POST方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String sendURLPost(String url, String params, String enc) {
        logger.info(url);
        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            if (enc == null || ("").equals(enc)) {
                //默认
                enc = URL_PARAM_DECODECHARSET_UTF8;
            }
            postMethod.setRequestHeader("Content-Type", "application/json;charset=" + enc);
//            postMethod.setRequestHeader("Auth-token", "ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNtZEpaQ0k2TVN3aVpYaHdJam94TlRjek5qTXhPREV5TENKMWRXbGtJam9pTWpobU16RTJNell0T1RVNVlpMDBNelJtTFdGaU5HRXRNVFpoWW1SaVpqVXpNR1EzSWl3aVpYaHdhWEpsY3lJNk1UVTNNell6TVRneE1qZ3hOaXdpYjNKblEyOWtaU0k2SWpKQ01UZ3hRVVZEUkRFeFF6aEdSak14TXpGRFFUaEVRMFV6TVVRMU1UUkRJaXdpWVhCd1MyVjVJam9pVG5wWk1rMTZUWHBOYlZsMFdtMUZlazFUTURCTmJWVjNURmRHYUUxVVVYUk5WRXBvVFhwRk5WbHFRVE5OUkVwdEluMC5lWFl6RS1lb203Y2dhWXZDSnZ1RlprYmRhWllOa0xfdVlDc1ExdU1DZHBJ");
            //将表单的值放入postMethod中
            RequestEntity requestEntity = new ByteArrayRequestEntity(params.getBytes("UTF-8"));
            postMethod.setRequestEntity(requestEntity);
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                response = stringBuffer.toString();
            } else {
                logger.info("url:{} 响应状态码:{}", url, postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", e);
        } catch (IOException e) {
            logger.error("发生网络异常:{}", e);
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return response;
    }

    public static String sendURLPost(String url, String params, Map<String, String> head, String enc) {
        logger.info(url);
        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            if (enc == null || ("").equals(enc)) {
                //默认
                enc = URL_PARAM_DECODECHARSET_UTF8;
            }
            postMethod.setRequestHeader("Content-Type", "application/json;charset=" + enc);
            if (null != head && !head.isEmpty()) {
                Iterator<Map.Entry<String, String>> iterator = head.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    postMethod.setRequestHeader(next.getKey(), next.getValue());
                }
            }
//            postMethod.setRequestHeader("Auth-token", "ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNtZEpaQ0k2TVN3aVpYaHdJam94TlRjek5qTXhPREV5TENKMWRXbGtJam9pTWpobU16RTJNell0T1RVNVlpMDBNelJtTFdGaU5HRXRNVFpoWW1SaVpqVXpNR1EzSWl3aVpYaHdhWEpsY3lJNk1UVTNNell6TVRneE1qZ3hOaXdpYjNKblEyOWtaU0k2SWpKQ01UZ3hRVVZEUkRFeFF6aEdSak14TXpGRFFUaEVRMFV6TVVRMU1UUkRJaXdpWVhCd1MyVjVJam9pVG5wWk1rMTZUWHBOYlZsMFdtMUZlazFUTURCTmJWVjNURmRHYUUxVVVYUk5WRXBvVFhwRk5WbHFRVE5OUkVwdEluMC5lWFl6RS1lb203Y2dhWXZDSnZ1RlprYmRhWllOa0xfdVlDc1ExdU1DZHBJ");
            //将表单的值放入postMethod中
            RequestEntity requestEntity = new ByteArrayRequestEntity(params.getBytes("UTF-8"));
            postMethod.setRequestEntity(requestEntity);
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                response = stringBuffer.toString();
            } else {
                logger.info("url:{} 响应状态码:{}", url, postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", e);
        } catch (IOException e) {
            logger.error("发生网络异常:{}", e);
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return response;
    }

    /**
     * GET方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String sendURLGet(String url, Map<String, Object> params, String enc) {
        url = "" + url;
        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(EMPTY);

        if (enc == null || ("").equals(enc)) {
            //默认
            enc = URL_PARAM_DECODECHARSET_UTF8;
        }

        if (strtTotalURL.indexOf(QUESTION_MARK_SYMBOL) == -1) {
            strtTotalURL.append(url).append(QUESTION_MARK_SYMBOL).append(getUrl(params, enc));
        } else {
            strtTotalURL.append(url).append(AND_SYMBOL).append(getUrl(params, enc));
        }
        logger.debug("GET请求URL = \n" + strtTotalURL.toString());

        try {
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader("Content-Type", "application/json;charset=" + enc);
            //执行getMethod
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                InputStream inputStream = getMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                response = stringBuffer.toString();
            } else {
                logger.debug("响应状态码 = " + getMethod.getStatusCode());
            }
        } catch (HttpException e) {
            logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", e);
        } catch (IOException e) {
            logger.error("发生网络异常:{}", e);
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    /**
     * GET方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String sendURLGet(String url, Map<String, String> headers, Map<String, Object> params, String enc) {
        url = "" + url;
        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(EMPTY);

        if (enc == null || ("").equals(enc)) {
            //默认
            enc = URL_PARAM_DECODECHARSET_UTF8;
        }

        if (strtTotalURL.indexOf(QUESTION_MARK_SYMBOL) == -1) {
            strtTotalURL.append(url).append(QUESTION_MARK_SYMBOL).append(getUrl(params, enc));
        } else {
            strtTotalURL.append(url).append(AND_SYMBOL).append(getUrl(params, enc));
        }
        logger.debug("GET请求URL = \n" + strtTotalURL.toString());

        try {
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader("Content-Type", "application/json;charset=" + enc);
            for (Map.Entry<String, String> e : headers.entrySet()) {
                getMethod.setRequestHeader(e.getKey(), e.getValue());
            }
            //执行getMethod
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = getMethod.getResponseBodyAsString();
            } else {
                logger.info("url:{} 响应状态码:{}", url, getMethod.getStatusCode());
            }
        } catch (HttpException e) {
            logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", e);
        } catch (IOException e) {
            logger.error("发生网络异常:{}", e);
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    /**
     * 据Map生成URL字符串
     *
     * @param map      Map
     * @param valueEnc URL编码
     * @return URL
     */
    private static String getUrl(Map<String, Object> map, String valueEnc) {

        if (("").equals(valueEnc) || valueEnc == null) {
            //默认
            valueEnc = URL_PARAM_DECODECHARSET_UTF8;
        }

        if (null == map || map.keySet().size() == 0) {
            return (EMPTY);
        }
        StringBuffer url = new StringBuffer();
        Set<String> keys = map.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = it.next();
            if (map.containsKey(key)) {
                Object val = map.get(key);
                Object str = val != null ? val : EMPTY;
                try {
                    str = URLEncoder.encode(str.toString(), valueEnc);
                } catch (UnsupportedEncodingException e) {
                    logger.error("error:{}", e);
                }
                url.append(key).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
            }
        }
        String strURL = url.toString();
        if (URL_PARAM_CONNECT_FLAG.equals(EMPTY + strURL.charAt(strURL.length() - 1))) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }

        return (strURL);
    }

    public static String getCilentIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP-CLIENT-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP-X_FORWARDED-FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    if (inet != null) {
                        ipAddress = inet.getHostAddress();
                    }
                } catch (UnknownHostException e) {
                    logger.error("error:{}", e);
                }

            }
        }
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}