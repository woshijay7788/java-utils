package response;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import constant.StatusEnum;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;


/**
 * @Description response实体类
 * @author chibei
 */
@Setter
@Getter
public class ResponseModel<T> implements Serializable {

    private static final long serialVersionUID = 4351320716313492131L;
    private Boolean success;
    private Integer errorCode;
    private String errorMsg;
    private Pagination pagination;
    private T result;

    public static ResponseModel success() {

        ResponseModel responseModel = new ResponseModel();
        responseModel.setSuccess(Boolean.TRUE);

        return responseModel;
    }

    public static <T> ResponseModel<T> success(T result) {

        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setSuccess(Boolean.TRUE);
        responseModel.setResult(result);

        return responseModel;
    }

    public static <T> ResponseModel<T> success(T result, Pagination pagination) {

        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setSuccess(Boolean.TRUE);
        responseModel.setResult(result);
        responseModel.setPagination(pagination);

        return responseModel;
    }

    public static <T> ResponseModel<T> success(T result, Page page) {

        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setSuccess(Boolean.TRUE);
        responseModel.setResult(result);

        Pagination pagination = new Pagination();
        pagination.setCurrent(page.getPageNum());
        pagination.setPageSize(page.getPageSize());
        pagination.setTotal(page.getTotal());
        responseModel.setPagination(pagination);

        return responseModel;
    }

    public static <T> ResponseModel<T> error(StatusEnum statusEnum) {
        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setSuccess(Boolean.FALSE);
        responseModel.setErrorCode(statusEnum.code());
        responseModel.setErrorMsg(statusEnum.message());
        return responseModel;
    }

    public static <T> ResponseModel<T> error(StatusEnum statusEnum, String errorMsg) {

        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setSuccess(Boolean.FALSE);
        responseModel.setErrorCode(statusEnum.code());
        responseModel.setErrorMsg(errorMsg);

        return responseModel;
    }


    public static <T> ResponseModel<T> valueOf(Boolean result) {

        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setSuccess(result);
        return responseModel;
    }

    public static <T> ResponseModel<T> error(Integer errorCode, String errorMsg) {

        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setSuccess(Boolean.FALSE);
        responseModel.setErrorCode(errorCode);
        responseModel.setErrorMsg(errorMsg);
        return responseModel;
    }

    public static <T> ResponseModel<T> success(ResponsePage page) {
        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setSuccess(Boolean.TRUE);

        if (page == null) {
            responseModel.setResult(null);
        } else {
            responseModel.setResult((T) page.getList());
            responseModel.setPagination(new Pagination(page.getCurrent(), page.getPageSize(), page.getTotal()));
        }

        return responseModel;

    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
