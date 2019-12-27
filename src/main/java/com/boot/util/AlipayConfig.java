package com.boot.util;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号,开发时使用沙箱提供的APPID，生产环境改成自己的APPID
    public static String APP_ID = "2016101600702968";

    // 商户私钥，您的PKCS8格式RSA2私钥
    //public static String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCu2UMZX9IEXn+H2ptW9scAHoG+J0SSbmTz2EZb+44j1cM0WqGgPat7Edi3HVp0y4yj5qCpZxfKy1Dm2jwUAyWbBM/PyrxqeA0K9/kUDYIpF6PgT41EdLkwL2o4gg/bM+0CCdNbkghRxHbV7VC+z71ebEMiKDRo4Zg0s9LJZzt5jywkdAsAXeSIj49nXDAURduhik2K18xHKmPeXyVculQg1IQ/fBTIZWZXw1ViFa3q2NOg/LBkiguduIJHX9yhoTe69RjiiHYg9gDgl6XvDyWFV1jmIc/5pht3AOOJU57RAS+qTbF6Y+D5EzpSr4WSKwwqYQbKcofOrEaMl97HmOzBAgMBAAECggEARV/Y2rGFL8yaxzl6lwe1L5vrGJqV/4+jqIiwagCmhhtjp1sqc9zkNCGBni4cXOkCdWmlZ3GLJCCFigMfAUW6XwaKucST+56EdkyrXER713gUwoRt5bJ2Y+L8P02DoxK4QC3L2bJYcLEGAZ12gy8D0PodrOjM2qxf6tFKmjTGiVUWEbW+oi9fwZmnT1bcdW3ESaxUf5GoNE/yoiNBnFSq6qOTQ3Vwegxi7LDFxkPXI4vOQvw5z2/q8MQ8NRfs7IhNMKdJjkLtkwwoh36nPUnlLhOdyMf6HQ20RCw3WIY7Cyfz3AOpo1/KFZTHiJkxdLbBYmQPFM1SN1aA4jEzTh8CvQKBgQD57mJRgRiZnIOIg+kM2SNN5dhQ+377qIZ6/Qf/xdUlgCoM9S+b/0nL4LM2H2gfn0OZr0Zv09hz1surPg1hUmcoUO3cZ/9HrRAfQtumAWCtfXhREaqxvieYSjt6HxsihCgJEHQMuWbOPOLuQsiKcGdo+xTW0ytUMKi1JEjGPhdAwwKBgQCzGCbtaR6LEov7xgV0zdBNIryqrg/WBmAvtFcFdZi+NE6Ivr+eqhZnzUdeJmqstmlodsW7B9xJeQ8mbulgR8ggZbOcUIM3jL1Vd0ShqbhUNwBWcEFV9vmDTdh8f0N8l3u9uOp43UntwOpld+hg4Y2MRiI5JxdnZUKafNC4ZboEKwKBgQDIVObj87l3L3hTDYDZNpdQ0kIwr1Yae/vHS1iFENsHoxKRrlpKDTfmvqaHZGc+qZcy8cZgzoq6V1qLWUK6VqWvMCdousdpeXPpytpq1sHabi7ptGKA9C2iqSXBfntukEXS9ig/JsEb4Lv5RPif1vdcs50BkOQzKImiIIJgvNZApwKBgGbMrDuGJUQKx1MjnSoooTJFiCooc2qUik2XpIO7tosnFxUi+HaohufaSubeAklVAzg1RNZQcr+xv2J+M3NSgKsn9Wr6Q/d0z5DpPvnUo7ujPoxfLwGbHCmkW2lK23/+q8aBCAWMb80K+QB5TWee0FL+RtKrf6GX3B01G9FcguO1AoGBALim7j3BR1FHcJQujV5ZHGaOH+J7ZWXMcN4LSxwKwiu6DeG790VPu0con0EtkhsmTuZeDhER8SS0KMzdhaGN2utcDxV+D9xFt40jCEmQRY7QFUL3z7cHmohEAtK3zb4FQKH4OYGMQV4xiqFoBevN5rLLFhSl/+3Mlu6ZKNWxGLTu";
    public static String APP_PRIVATE_KEY ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQmmp3g9vTLn1KiMSoZ/HDYNSoC/ZsHlAVEkl8NU/wSbJt8YiHT7bjjLk/G5HdRleVtX30+sRR3LkwMLDzl0dvi6WcHmFCa6hhSQj7PFONFAIDAICN/FTNbypeCX5PI5Xs5+FWpWIpJQ8Ja9i46b7tEQ2gXUcq8xqArXplmyPlHzcYTTt1Z7GkvPLfS5kf68jqhG+ZgLrTp+o/SJoBab56L+Xp43obcabvrJdrpB8G4O9MGj1Az5xiiHYIpoL2BwSqcMXTeaOCnaAvakz4jgBL4ekV0J+6jXkuDC89iw1eySVmSUIKu7nmFJBsOeanRJFTEAIH9z2G9uEgbSxb+XG3AgMBAAECggEAZphIZZd2RfU8C7/R17I4A+kVuE0Uf01agDjDTo78sn13TS4LMgXKutPDMxU6foZD6gIk5cVtuK8j7R7+zTVOB8RKseOUGe4MP0n0R1xb40KI0txuWU7oVAFFTAbsSPX0j2tNo44fgRW7/3E3CKs/PS/WPSsxLTXiXd23c5OI+iv+FcyUVfuGz1283w2/XX5fODgKOSN15cHsN9dJmW0R3Icn/i6P48TlJIYwRzwOXXs4K3knX3EvR2ATRCe8EoSvXnLPrVyhukXjlLUkw0oO8fiDKK93UonAN3GWvrY6zW9v0ANvAqizrFzcg5q5m6SmOmwDafR7+FVAzEvAajKRcQKBgQDu8H6XhdNsayKVH9AYwbtbeXz3Kh2AuGG/QVu0r+/IsbNUGOGXT5jWDjFanTo+2FQaHTAOQlBNxfbYl5WG11ZrGyGUJ4dtz/HAablRIJDjpstTQVyP5/F1lY0zT2c5k237QGdl2UVA8S9JfHzDRuPtpJtDjgexA41vuUatemhm3QKBgQCa7ZN/kt2LtsaNjergr/Lv2B7P0yEIZSkQvhAfr1JHkNSL4pE1KI6g142OSBMkeduyczXLAkg2xOrhJf9Pgapj37OJ5IUmCPUcTwT1UprvPALV/ymXxGvKrt81zbewUWqeFpSwFOxbWkMTsAsGYcN0b/oc6KSmo3KkYywbgU0PowKBgGbtNCsDW/LdJ7WDUboIYjPiXoF+I91aD8k6V027aBhU7AHzAeKTSCwVSw9UoU2HFJt3LUdHK56FA3ODwMaoJjLXKhzO9ljIdcREBq86zw8xv25qvnvDF1Fmjb+nmbjEmGDd+mx2Lo3uF0eZl4jrFa499SAho5CODjQtZJPAIg8FAoGALSDbACQ1qIbbG0yliEF4UGjxwLPNQXbGGXxjkfctzm7fmseqaIOh2DQxpapU+SEN39MzbikCBAIkMOUyk2Q/z8vPrnEEX+noDBDwbs1bGCiw4sjqwqySgX2iwzdjNFbi6Z3GPew6VPnb5BY5f+hmWLptbpJNs8SUllHU/UYSvmUCgYEAla58dQKhGIIsud87a3wMYcu/yeqR6KHKPbtVbGfP930oFpuu+N7SIGMsDWsMJi/v+Az3C0ZpfngZoWx9jEVjI7E/JLuPRXsM43eoTFEXw5Rt6Y+fQ5t5CPeTa9VqJdyKnWm5CqUbTmmpUt0dPXJNeGcS/sHT7HzKokei9wVrT3k=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkJpqd4Pb0y59SojEqGfxw2DUqAv2bB5QFRJJfDVP8EmybfGIh0+244y5PxuR3UZXlbV99PrEUdy5MDCw85dHb4ulnB5hQmuoYUkI+zxTjRQCAwCAjfxUzW8qXgl+TyOV7OfhVqViKSUPCWvYuOm+7RENoF1HKvMagK16ZZsj5R83GE07dWexpLzy30uZH+vI6oRvmYC606fqP0iaAWm+ei/l6eN6G3Gm76yXa6QfBuDvTBo9QM+cYoh2CKaC9gcEqnDF03mjgp2gL2pM+I4AS+HpFdCfuo15LgwvPYsNXsklZklCCru55hSQbDnmp0SRUxACB/c9hvbhIG0sW/lxtwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8089/alipay.trade.page.pay-JAVA-UTF-8/notify_url.html";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(其实就是支付成功后返回的页面)
    public static String return_url = "http://localhost:8089/return_url.html";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String CHARSET = "utf-8";

    // 支付宝网关，这是沙箱的网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 日之路径头
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
