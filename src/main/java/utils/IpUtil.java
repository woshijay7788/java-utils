package utils;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description ip工具
 * @author chibei
 */
@Slf4j
public class IpUtil {

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     */
    public final static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        
        log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip = {}", ip);

        if (StringUtils.isBlank(ip)|| UNKNOWN.equalsIgnoreCase(ip)) {
            if (StringUtils.isBlank(ip)|| UNKNOWN.equalsIgnoreCase(ip)) {

                ip = request.getHeader("Proxy-Client-IP");
                if (StringUtils.isNotBlank(ip)) {
                    log.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip = {}", ip);
                }
            }
            if (StringUtils.isBlank(ip)|| UNKNOWN.equalsIgnoreCase(ip)) {

                ip = request.getHeader("WL-Proxy-Client-IP");
                if (StringUtils.isNotBlank(ip)) {
                    log.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip = {}", ip);
                }
            }
            if (StringUtils.isBlank(ip)|| UNKNOWN.equalsIgnoreCase(ip)) {

                ip = request.getHeader("HTTP_CLIENT_IP");
                if (StringUtils.isNotBlank(ip)) {
                    log.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip = {}", ip);
                }
            }
            if (StringUtils.isBlank(ip)|| UNKNOWN.equalsIgnoreCase(ip)) {

                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (StringUtils.isNotBlank(ip)) {
                    log.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip = {}", ip);
                }
            }
            if (StringUtils.isBlank(ip)|| UNKNOWN.equalsIgnoreCase(ip)) {

                ip = request.getRemoteAddr();
                if (StringUtils.isNotBlank(ip)) {
                    log.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip = {}", ip);
                }
            }
        } else if (ip.length() > MAX_IP_LENGTH) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!(UNKNOWN.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    private static final String UNKNOWN = "unknown";

    private static final int MAX_IP_LENGTH = 15;
}
