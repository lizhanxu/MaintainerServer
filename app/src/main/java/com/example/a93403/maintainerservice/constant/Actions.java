package com.example.a93403.maintainerservice.constant;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class Actions {

    /**
     * 用户登录请求的action
     */
    public static final String ACTION_REQUEST_LOGIN = "repairman/login.do";

    /**
     * 用户注册请求的action
     */
    public static final String ACTION_REQUEST_REGISTER = "repairman/register.do";

    /**
     * 修改用户资料的action
     */
    public static final String ACTION_MODIFY_PROFILE = "repairman/modify.do";

    /**
     * 获取附近服务点信息的action
     */
    public static final String ACTION_GET_SITE = "site/getList.do";

    /**
     * 获取所有服务点位置信息的action
     */
    public static final String ACTION_GET_SITE_LOCATION = "site/getLocation.do";

    /**
     * 根据名称查找服务点信息的action
     */
    public static final String ACTION_QUERY_SITE = "site/query.do";

    /**
     * 获取所有文章的action
     */
    public static final String ACTION_GET_ARTICLE = "article/getList.do";

    /**
     * 获取目标维修点所有评论的action
     */
    public static final String ACTION_GET_COMMENT = "site/getComment.do";

    /**
     * 获取目标用户所有评论的action
     */
    public static final String ACTION_GET_COMMENT_USER = "customer/getComment.do";

    /**
     * 查询故障码的action
     */
    public static final String ACTION_QUERY_CODE = "code/queryCode.do";

    /**
     * 发送验证码的action
     */
    public static final String ACTION_SEND_VERFI_CODE = "repairman/sendEmail.do";

    /**
     * 忘记密码的action
     */
    public static final String ACTION_FORGET_PASSWORD = "repairman/forgetPsw.do";

    /**
     * 接受订单的action
     */
    public static final String ACTION_ACCEPT_ORDER = "order/accept.do";

    /**
     * 结束订单的action
     */
    public static final String ACTION_FINISH_ORDER = "order/finish.do";

    /**
     * 查询订单的action
     */
    public static final String ACTION_QUERY_ORDER = "order/queryFinish.do";

    /**
     * 查询订单统计的action
     */
    public static final String ACTION_QUERY_ORDER_Statistics = "order/statistics.do";

}
