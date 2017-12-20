package com.bingo.service;

import com.bingo.common.Contant;
import com.bingo.domain.*;
import com.bingo.utils.IPUtil;
import com.bingo.utils.SecurityUtil;
import com.bingo.utils.UUIDUtil;
import com.bingo.vo.AddInfo;
import com.bingo.vo.FriendList;
import com.bingo.vo.GroupList;
import com.bingo.vo.GroupMember;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface IUserService {
    public Boolean leaveOutGroup(Integer gid ,Integer uid );
    /**
     * @description 添加群成员
     * @param gid 群编号
     * @param uid 用户编号
     * @param messageBoxId 消息盒子Id
     */
    public Boolean addGroupMember(Integer gid, Integer uid, Integer messageBoxId) ;

    /**
     * @description 删除好友
     * @param friendId 好友Id
     * @param uId 个人Id
     * @return Boolean
     */
    public Boolean removeFriend(Integer friendId,Integer uId ) ;

    /**
     * @description 更新用户头像
     * @param userId
     * @param avatar
     * @return
     */
    public Boolean updateAvatar(Integer userId,String avatar) ;
    /**
     * @description 移动好友分组
     * @param groupId 新的分组id
     * @param uId 被移动的好友id
     * @param mId 我的id
     * @return
     */
    //清除缓存
    public Boolean changeGroup(Integer groupId,Integer uId,Integer mId ) ;
    /**
     * @description 添加好友操作
     * @param mid 我的id
     * @param mgid 我设定的分组
     * @param tid 对方的id
     * @param tgid 对方设定的分组
     * @param messageBoxId 消息盒子的消息id
     */
    public Boolean addFriend(Integer mid,Integer mgid,Integer tid,Integer tgid,Integer messageBoxId);
    /**
     * @description 创建好友分组列表
     * @param uid
     * @param groupName
     */
    public Boolean createFriendGroup(String groupName,Integer uid) ;
    /**
     * @description 统计消息
     * @param uid
     */
    public Integer countUnHandMessage(Integer uid, Integer agree);
    /**
     * @description 查询添加好友、群组信息
     * @param uid
     * @return List[AddInfo]
     */
    public List findAddInfo(Integer uid) ;
    /**
     * @description 更新好友、群组信息请求
     * @return
     */
    public Boolean updateAddMessage(Integer messageBoxId, Integer agree);


    /**
     * @description 添加好友、群组信息请求
     * @param addMessage
     * @return
     */
    public int saveAddMessage(AddMessage addMessage);
    /**
     * @description 根据群名模糊统计
     * @param groupName
     * @return
     */
    public int countGroup(String groupName);
    /**
     * @description 根据群名模糊查询群
     * @param groupName
     * @return
     */
    public List<GroupList> findGroup(String groupName);
    /**
     * @description 根据用户名和性别统计用户
     * @param username
     * @param sex
     */
    public int countUsers(String username,Integer sex);
    /**
     * @description 根据用户名和性别查询用户
     * @param username
     * @param sex
     */
    public List<User> findUsers(String username, Integer sex);

    /**
     * @description 统计查询消息
     * @param uid 消息所属用户
     * @param mid 来自哪个用户
     * @param Type 消息类型，可能来自friend或者group
     */
    public int countHistoryMessage(Integer uid,Integer mid,String Type ) ;

    /**
     * @description 查询历史消息
     * @param
     */
    public List<ChatHistory> findHistoryMessage(User user, Integer mid, String Type);
    /**
     * @description 查询离线消息
     * @param uid
     * @param status 历史消息还是离线消息 0代表离线 1表示已读
     */
    public List<Receive> findOffLineMessage(Integer uid ,Integer status);


    /**
     * @description 保存用户聊天记录
     * @param receive 聊天记录信息
     * @return Int
     */
    public int saveMessage(Receive receive);
    /**
     * @description 用户更新签名
     * @param user
     * @return Boolean
     */
    public Boolean updateSing(User user) ;

    /**
     * @description 激活码激活用户
     * @param activeCode
     * @return Int
     */
    public int activeUser(String activeCode);

    /**
     * @description 判断邮件是否存在
     * @param email
     * @return
     */
    public Boolean existEmail(String email) ;
    /**
     * @description 用户邮件和密码是否匹配
     * @param user
     * @return User
     */
    User matchUser(User user) ;
    /**
     * @description 根据群组ID查询群里用户的信息
     * @param gid
     * @return List[User]
     */
    public List<User> findUserByGroupId(int gid);

    /**
     * @description 根据ID查询用户好友分组列表信息
     * @param uid 用户ID
     * @return List[FriendList]
     */
    public List<FriendList> findFriendGroupsById(int uid);
    /**
     * @description 根据ID查询用户信息
     * @param id
     * @return User
     */
    public User findUserById(Integer id) ;
    /**
     * @description 根据ID查询用户群组信息
     * @param id
     * @return List[Group]
     */
    public List<GroupList> findGroupsById(int id);
    /**
     * @description 保存用户信息
     * @param user
     * @return Int
     */
    //清除缓存
    public Boolean saveUser(User user, HttpServletRequest request) ;
}
