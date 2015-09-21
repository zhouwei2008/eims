package eims

class WoxpSmsService {

    private Integer x_gate_id=300;
    private Integer x_eid=11318;
    private String x_uid="rongpay";//登录用户名
//  private String x_pwd_md5="afb492f92da5a4cd6ff188e9d05bec18";
    private String x_pwd_md5="e0cea4bb45e36c115356e46910c354af";
//  private String password="123qwe!@#QWE";

    public String SendSms(String mobile,String content) throws UnsupportedEncodingException{
        Integer x_ac=12;//发送信息
        HttpURLConnection httpconn = null;
        String result="-20";
        String memo = content.trim();
        StringBuilder sb = new StringBuilder();
        sb.append("http://gateway.woxp.cn:6630/utf8/web_api/?x_eid=");
        sb.append(x_eid);
        sb.append("&x_uid=").append(x_uid);
        sb.append("&x_pwd_md5=").append(x_pwd_md5);
        sb.append("&x_ac=").append(x_ac);
        sb.append("&x_gate_id=").append(x_gate_id);
        sb.append("&x_target_no=").append(mobile);
        sb.append("&x_memo=").append(URLEncoder.encode(memo, "utf-8"));
        try {
            URL url = new URL(sb.toString());
            httpconn = (HttpURLConnection) url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
            result = rd.readLine();
            rd.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(httpconn!=null){
                httpconn.disconnect();
                httpconn=null;
            }
        }
        return result;
    }
}
