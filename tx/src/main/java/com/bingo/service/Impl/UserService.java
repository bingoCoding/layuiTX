package com.bingo.service.Impl;

import com.bingo.common.Contant;
import com.bingo.dao.UserMapper;
import com.bingo.domain.*;
import com.bingo.service.IUserService;
import com.bingo.utils.IPUtil;
import com.bingo.utils.SecurityUtil;
import com.bingo.utils.UUIDUtil;
import com.bingo.vo.AddInfo;
import com.bingo.vo.FriendList;
import com.bingo.vo.GroupList;
import com.bingo.vo.GroupMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

@Service
public class UserService implements IUserService{
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    //电子邮件相关服务
//    @Resource
//    private MailService mailService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private MailService mailService;

    /**
     * @description 退出群
     */
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"}, allEntries = true)
    public Boolean leaveOutGroup(Integer gid ,Integer uid ){
        return userMapper.leaveOutGroup(new GroupMember(gid, uid)) == 1;
    }

    /**
     * @description 添加群成员
     * @param gid 群编号
     * @param uid 用户编号
     * @param messageBoxId 消息盒子Id
     */
    @Transactional
    public Boolean addGroupMember(Integer gid, Integer uid, Integer messageBoxId) {
        if (gid == null || uid == null ) {
            return false;
        } else {
            return userMapper.addGroupMember(new GroupMember(gid, uid)) == 1&&updateAddMessage(messageBoxId, 1);
        }
    }

    /**
     * @description 删除好友
     * @param friendId 好友Id
     * @param uId 个人Id
     * @return Boolean
     */
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"}, allEntries = true)
    public Boolean removeFriend(Integer friendId,Integer uId ) {
        if (friendId == null || uId == null)
            return false;
        else
            return userMapper.removeFriend(friendId, uId) == 1;
    }

    /**
     * @description 更新用户头像
     * @param userId
     * @param avatar
     * @return
     */
    @CacheEvict(value = {"findUserById"}, allEntries = true)
    @Transactional
    public Boolean updateAvatar(Integer userId,String avatar) {
        if (userId == null | avatar == null)
            return false;
        else
            return userMapper.updateAvatar(userId, avatar) == 1;
    }

    /**
     * @description 移动好友分组
     * @param groupId 新的分组id
     * @param uId 被移动的好友id
     * @param mId 我的id
     * @return
     */
    //清除缓存
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"}, allEntries = true)
    @Transactional
    public Boolean changeGroup(Integer groupId,Integer uId,Integer mId ) {
        if (groupId == null || uId == null || mId == null)
            return false;
        else
            return userMapper.changeGroup(groupId, uId, mId) == 1;
    }

    /**
     * @description 添加好友操作
     * @param mid 我的id
     * @param mgid 我设定的分组
     * @param tid 对方的id
     * @param tgid 对方设定的分组
     * @param messageBoxId 消息盒子的消息id
     */
    @Transactional
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"}, allEntries = true)
    public Boolean addFriend(Integer mid,Integer mgid,Integer tid,Integer tgid,Integer messageBoxId){
        AddFriends add = new AddFriends(mid, mgid, tid, tgid);
        if (userMapper.addFriend(add) != 0) {
            return updateAddMessage(messageBoxId, 1);
        }
        return false;
    }

    /**
     * @description 创建好友分组列表
     * @param uid
     * @param groupName
     */
    public Boolean createFriendGroup(String groupName,Integer uid) {
        if (uid == null || groupName == null || "".equals(uid) || "".equals(groupName))
            return false;
        else
            return userMapper.createFriendGroup(new FriendGroup(uid, groupName)) == 1;
    }

    /**
     * @description 统计消息
     * @param uid
     */
    public Integer countUnHandMessage(Integer uid, Integer agree){
        return userMapper.countUnHandMessage(uid, agree);
    }

    /**
     * @description 查询添加好友、群组信息
     * @param uid
     * @return List[AddInfo]
     */
    public List<AddInfo> findAddInfo(Integer uid) {
        List<AddInfo> list = userMapper.findAddInfo(uid);
        list.stream().forEach( info -> {
            if (info.getType() == 0) {
                info.setContent("申请添加你为好友");
            } else {
                GroupList group = userMapper.findGroupById(info.getFrom_group());
                info.setContent("申请加入 '" + group.getGroupname() + "' 群聊中!");
            }
            info.setHref(null);
            info.setUser(findUserById(info.getFrom()));
            LOGGER.info(info.toString());
        });
        return list;
    }

    /**
     * @description 更新好友、群组信息请求
     * @return
     */
    @Transactional
    public Boolean updateAddMessage(Integer messageBoxId, Integer agree){
        AddMessage addMessage = new AddMessage();
        addMessage.setAgree(agree);
        addMessage.setId(messageBoxId);
        return userMapper.updateAddMessage(addMessage) == 1;
    }


    /**
     * @description 添加好友、群组信息请求
     * @param addMessage
     * @return
     */
    public int saveAddMessage(AddMessage addMessage){
        return userMapper.saveAddMessage(addMessage);
    }

    /**
     * @description 根据群名模糊统计
     * @param groupName
     * @return
     */
    public int countGroup(String groupName){
        return userMapper.countGroup(groupName);
    }

    /**
     * @description 根据群名模糊查询群
     * @param groupName
     * @return
     */
    public List<GroupList> findGroup(String groupName){
        return userMapper.findGroup(groupName);
    }

    /**
     * @description 根据用户名和性别统计用户
     * @param username
     * @param sex
     */
    public int countUsers(String username,Integer sex){
        return userMapper.countUser(username, sex);
    }

    /**
     * @description 根据用户名和性别查询用户
     * @param username
     * @param sex
     */
    public List<User> findUsers(String username,Integer sex){
        return userMapper.findUsers(username, sex);
    }


    /**
     * @description 统计查询消息
     * @param uid 消息所属用户
     * @param mid 来自哪个用户
     * @param Type 消息类型，可能来自friend或者group
     */
    public int countHistoryMessage(Integer uid,Integer mid,String Type ) {
        if ("friend".equals(Type)){
            return userMapper.countHistoryMessage(uid, mid, Type);
        }else if ("group".equals(Type)){
             return userMapper.countHistoryMessage(null, mid, Type);
        }
        return -1;
    }

    /**
     * @description 查询历史消息
     * @param
     */
    public List<ChatHistory> findHistoryMessage(User user,Integer mid,String Type){
        List<ChatHistory> list = new ArrayList<>();
        //单人聊天记录
        if ("friend".equals(Type)) {
            //查找聊天记录
            List<Receive> historys = userMapper.findHistoryMessage(user.getId(), mid, Type);
            User toUser = findUserById(mid);
            historys.forEach(history -> {
                ChatHistory chatHistory = null;
                if(history.getId() == mid){
                    chatHistory = new ChatHistory(history.getId(), toUser.getUsername(),toUser.getAvatar(),history.getContent(),history.getTimestamp());
                } else {
                    chatHistory = new ChatHistory(history.getId(), user.getUsername(),user.getAvatar(),history.getContent(),history.getTimestamp());
                }
                list.add(chatHistory);
            });
        }
        //群聊天记录
        if ("group".equals(Type)) {
            //查找聊天记录
            List<Receive> historys = userMapper.findHistoryMessage(null, mid, Type);
            historys.forEach (history -> {
                ChatHistory chatHistory = null;
                User u = findUserById(history.getFromid());
                if (history.getFromid().equals(user.getId())) {
                    chatHistory = new ChatHistory(user.getId(), user.getUsername(),user.getAvatar(),history.getContent(),history.getTimestamp());
                } else {
                    chatHistory = new ChatHistory(history.getId(), u.getUsername(),u.getAvatar(),history.getContent(),history.getTimestamp());
                }
                list.add(chatHistory);
            }) ;
        }
        return list;
    }

    /**
     * @description 查询离线消息
     * @param uid
     * @param status 历史消息还是离线消息 0代表离线 1表示已读
     */
    public List<Receive> findOffLineMessage(Integer uid ,Integer status){
        return userMapper.findOffLineMessage(uid, status);
    }


    /**
     * @description 保存用户聊天记录
     * @param receive 聊天记录信息
     * @return Int
     */
    public int saveMessage(Receive receive){
        return userMapper.saveMessage(receive);
    }

    /**
     * @description 用户更新签名
     * @param user
     * @return Boolean
     */
    public Boolean updateSing(User user) {
        if (user == null || user.getSign() == null || user.getId() == null) {
            return false;
        } else {
            return userMapper.updateSign(user.getSign(), user.getId()) == 1;
        }
    }

    /**
     * @description 激活码激活用户
     * @param activeCode
     * @return Int
     */
    public int activeUser(String activeCode){
        if (activeCode == null || "".equals(activeCode)) {
            return 0;
        }
        return userMapper.activeUser(activeCode);
    }

    /**
     * @description 判断邮件是否存在
     * @param email
     * @return
     */
    public Boolean existEmail(String email) {
        if (email == null || "".equals(email))
            return false;
        else
            return userMapper.matchUser(email) != null;
    }

    /**
     * @description 用户邮件和密码是否匹配
     * @param user
     * @return User
     */
    public User matchUser(User user) {
        if (user == null || user.getEmail() == null) {
            return null;
        }
        User u = userMapper.matchUser(user.getEmail());
        //密码不匹配
        if(u == null || !SecurityUtil.matchs(user.getPassword(), u.getPassword())){
            return null;
        }
        return u;
    }

    /**
     * @description 根据群组ID查询群里用户的信息
     * @param gid
     * @return List[User]
     */
    @Cacheable(value = "findUserByGroupId", keyGenerator = "wiselyKeyGenerator")
    public List<User> findUserByGroupId(int gid){
        return userMapper.findUserByGroupId(gid);
    }

    /**
     * @description 根据ID查询用户好友分组列表信息
     * @param uid 用户ID
     * @return List[FriendList]
     */
    @Cacheable(value = "findFriendGroupsById", keyGenerator = "wiselyKeyGenerator")
    public List<FriendList> findFriendGroupsById(int uid) {
        List<FriendList> friends = userMapper.findFriendGroupsById(uid);
        //封装分组列表下的好友信息
        friends.forEach(friend -> {
            friend.setList(userMapper.findUsersByFriendGroupIds(friend.getId())) ;
        });
        return friends;
    }

    /**
     * @description 根据ID查询用户信息
     * @param id
     * @return User
     */
    @Cacheable(value ="findUserById", keyGenerator = "wiselyKeyGenerator")
    public User findUserById(Integer id) {
        if (id != null)
            return userMapper.findUserById(id);
        else
            return null;
    }

    /**
     * @description 根据ID查询用户群组信息
     * @param id
     * @return List[Group]
     */
    @Cacheable(value = "findGroupsById", keyGenerator = "wiselyKeyGenerator")
    public List<GroupList> findGroupsById(int id){
        return userMapper.findGroupsById(id);
    }
    /**
     * @description 保存用户信息
     * @param user
     * @return Int
     */
    //清除缓存
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"}, allEntries = true)
    @Transactional
    public Boolean saveUser(User user, HttpServletRequest request) {
        if (user == null || user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            return false;
        } else {
            //激活码
            String activeCode = UUIDUtil.getUUID64String();
            user.setActive(activeCode);
            user.setCreateDate(new Date());
            //加密密码
            user.setPassword(SecurityUtil.encrypt(user.getPassword()));
            userMapper.saveUser(user);
            LOGGER.info("userid = " + user.getId());
            //创建默认的好友分组
            createFriendGroup(Contant.DEFAULT_GROUP_NAME, user.getId());
            //发送激活电子邮件
            try {
                mailService.sendHtmlMail(user.getEmail(), Contant.SUBJECT,
                        user.getUsername() +",请确定这是你本人注册的账号   " + ", " + IPUtil.getServerIpAdder(request) + "/user/active/" + activeCode);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
