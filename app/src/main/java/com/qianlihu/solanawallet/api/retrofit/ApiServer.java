package com.qianlihu.solanawallet.api.retrofit;

import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.mvp.create_room.bean.CreateRoomBean;
import com.qianlihu.solanawallet.mvp.dapp.bean.DappBean;
import com.qianlihu.solanawallet.mvp.join_room.JoinRoomBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.DisbandRoom;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.ExistRoomBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.ExistRoomBean2;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.MeetingRoomUserBean;
import com.qianlihu.solanawallet.mvp.member_manager.KickoutRoom;
import com.qianlihu.solanawallet.mvp.mine.bean.BannerBean;
import com.qianlihu.solanawallet.wallet_bean.ArticlesListBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * author : Duan
 * date   : 2022/11/417:07
 * desc   :
 * version: 1.0.0
 */
public interface ApiServer {

    /**
     * 获取dapp列表
     *
     * @param language
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @GET(value = Constant.WALLET_INFO)
    Observable<ApiResponse<DappBean>> getDapp(@Query("language") String language,
                                              @Query("id") int id,
                                              @Query("page") int page,
                                              @Query("limit") int pageSize);

    /**
     * 获取我的页面广告banner图
     *
     * @return
     */
    @GET(value = Constant.BANNER_IMAGE)
    Observable<ApiResponse<List<BannerBean>>> bannerImge(@Query("language") String language);

    /**
     * 获取我的页面新闻列表
     *
     * @param language
     * @param page
     * @param pageSize
     * @return
     */
    @GET(value = Constant.ARTICLES_LIST)
    Observable<ApiResponse<ArticlesListBean>> getNewsList(@Query("language") String language,
                                                          @Query("page") int page,
                                                          @Query("limit") int pageSize);

    /**
     * 创建房间
     *
     * @param language     语言类型
     * @param address      钱包地址
     * @param room_id      房间号
     * @param room_name    房间名称
     * @param room_man_num 房间人数
     * @param user_name    房主名称
     * @param user_img     房主头像
     * @return
     */
    @POST(value = Constant.CREAT_ROOM)
    Observable<ApiResponse<CreateRoomBean>> createMeetingRoom(@Query("language") String language,
                                                              @Query("address") String address,
                                                              @Query("room_id") String room_id,
                                                              @Query("room_name") String room_name,
                                                              @Query("room_man_num") int room_man_num,
                                                              @Query("user_name") String user_name,
                                                              @Query("user_img") String user_img);

    /**
     * 加入房间
     *
     * @param language  语言类型
     * @param address   钱包地址
     * @param room_p_id 房间号
     * @param user_name 成员名称
     * @param user_img  成员头像
     * @return
     */
    @POST(value = Constant.JOIN_ROOM)
    Observable<ApiResponse<JoinRoomBean>> joinMeetingRoom(@Query("language") String language,
                                                          @Query("address") String address,
                                                          @Query("room_p_id") String room_p_id,
                                                          @Query("live_status") int live_status,
                                                          @Query("taboo_status") int taboo_status,
                                                          @Query("user_name") String user_name,
                                                          @Query("user_img") String user_img);

    /**
     * 房间用户列表
     *
     * @param language 语言类型
     * @param room_id  房间号
     * @return
     */
    @POST(value = Constant.USER_LIST)
    Observable<ApiResponse<MeetingRoomUserBean>> userListRoom(@Query("language") String language,
                                                              @Query("room_id") String room_id);

    /**
     * 退出房间
     * @param address 地址
     * @param type 1改用户转态，2用户退出删除，3删除房间
     * @param room_id 房间号
     * @return
     */
    @POST(value = Constant.EXIT_ROOM)
    Observable<ApiResponse<ExistRoomBean>> userExitRoom(@Query("address") String address,
                                                        @Query("type") int type,
                                                        @Query("room_id") String room_id);

    /**
     * 解散房间
     * @param room 房间号
     * @return
     */
    @POST(value = Constant.DISBAND_ROOM)
    Observable<ApiResponse<String>> disbandRoom(@Body DisbandRoom room);

    /**
     * 踢出房间
     * @param kickoutRoom 踢出用户
     * @return
     */
    @POST(value = Constant.KICKOUT_ROOM)
    Observable<ApiResponse<ExistRoomBean2>> kickoutRoom(@Body KickoutRoom kickoutRoom);
}
