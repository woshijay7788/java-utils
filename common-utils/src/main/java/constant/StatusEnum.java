package constant;

/**
 * @description 返回状态类
 * @author chibei
 */
public enum StatusEnum {

    SUCCESS(200, "请求处理成功"),
    UNAUTHORIZED(401, "用户认证失败"),
    FORBIDDEN(403, "权限不足"),
    SERVICE_ERROR(500, "服务器错误"),
    CAPTCHA_INVALID(405, "验证码校验失败"),

    //业务异常
    API_INTERNAL_ERROR(1001, "接口内部异常"),
    INVAILD_PARAM(1002, "无效的参数"),
    PARAM_HAS_EMPTY(1003, "参数存在空值"),
    INVAILD_BUSSINESS_TYPE(1004, "无效的认证类型"),
    PARAM_BIND_EXCEPTION(1005, "不支持的请求参数"),
    HTTP_METHORD_NOT_SUPPORT(1006, "不支持的请求类型"),
    DATA_NOT_EXIST(1007, "数据不存在!"),
    RPC_ERROR(1008, "远程调用失败"),
    DB_INSERT_EXCEPTION(1009, "数据插入出错!"),
    DB_SELECT_EXCEPTION(1010, "数据查询异常!"),
    DB_UPDATE_EXCEPTION(1011, "数据修改异常!"),
    INVAILD_ENUM_VALUE(1014,"无效的枚举值"),

    ;


    private Integer code;
    private String message;

    StatusEnum(Integer status, String message) {
        this.code = status;
        this.message = message;
    }

    public static StatusEnum getStatusEnumByStatus(Integer status) {

        if (status == null) {
            return null;
        }

        for (StatusEnum statusEnum : values()) {
            if (statusEnum.code().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
