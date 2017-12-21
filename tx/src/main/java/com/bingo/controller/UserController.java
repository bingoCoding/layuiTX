package com.bingo.controller;

import com.bingo.common.Contant;
import com.bingo.domain.ChatHistory;
import com.bingo.domain.Receive;
import com.bingo.domain.User;
import com.bingo.service.IUserService;
import com.bingo.utils.FileUtil;
import com.bingo.vo.*;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(value = "用户相关操作")
@RequestMapping("/user")
public class UserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final Gson gson = new Gson();

    @Resource
    private IUserService userService;
    /**
     * @description 退出群
     * @param groupId 群编号
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/leaveOutGroup", method = RequestMethod.POST)
    String leaveOutGroup(@RequestParam("groupId")Integer groupId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Boolean result = userService.leaveOutGroup(groupId, user.getId());
        return gson.toJson(new ResultSet(result));
    }

    /**
     * @description 删除好友
     * @param friendId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/removeFriend", method = RequestMethod.POST)
    String removeFriend(@RequestParam("friendId")Integer friendId,HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Boolean result = userService.removeFriend(friendId, user.getId());
        return gson.toJson(new ResultSet(result));
    }

    /**
     * @description 移动好友分组
     * @param groupId 新的分组id
     * @param userId 被移动的好友id
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeGroup", method = RequestMethod.POST)
    String changeGroup(@RequestParam("groupId")Integer groupId, @RequestParam("userId")Integer userId
            ,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Boolean result = userService.changeGroup(groupId, userId, user.getId());
        if (result)
            return gson.toJson(new ResultSet(result));
        else
            return gson.toJson(new ResultSet(Contant.ERROR, Contant.ERROR_MESSAGE));
    }

    /**
     * @description 拒绝添加好友
     * @param request
     * @param messageBoxId 消息盒子的消息id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refuseFriend", method = RequestMethod.POST)
    String refuseFriend(@RequestParam("messageBoxId")Integer messageBoxId,HttpServletRequest request) {
        Boolean result = userService.updateAddMessage(messageBoxId, 2);
        return gson.toJson(new ResultSet(result));
    }

    /**
     * @description 添加好友
     * @param uid 对方用户ID
     * @param fromGroup 对方设定的好友分组
     * @param group 我设定的好友分组
     * @param messageBoxId 消息盒子的消息id
     * @return String
     */
    @ResponseBody
    @RequestMapping(value = "/agreeFriend", method = RequestMethod.POST)
    String agreeFriend(@RequestParam("uid")Integer uid ,@RequestParam("from_group")Integer fromGroup,
                    @RequestParam("group")Integer group , @RequestParam("messageBoxId")Integer messageBoxId,
                    HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Boolean result = userService.addFriend(user.getId(), group, uid, fromGroup, messageBoxId);
        return gson.toJson(new ResultSet(result));
    }

    /**
     * @description 查询消息盒子信息
     * @param uid
     * @param page
     */
    @ResponseBody
    @RequestMapping(value = "/findAddInfo", method = RequestMethod.GET)
    String findAddInfo(@RequestParam("uid")Integer uid, @RequestParam("page")int page) {
        PageHelper.startPage(page, Contant.ADD_MESSAGE_PAGE);
        List<AddInfo > list = userService.findAddInfo(uid);
        int count = userService.countUnHandMessage(uid, null);
        int pages =  (count < Contant.ADD_MESSAGE_PAGE)? 1 : (count / Contant.ADD_MESSAGE_PAGE + 1);
        return gson.toJson(new ResultPageSet(list, pages)).replaceAll("Type", "type");
    }

    /**
     * @description 分页查找好友
     * @param page 第几页
     * @param name 好友名字
     * @param sex 性别
     */
    @ResponseBody
    @RequestMapping(value = "/findUsers", method = RequestMethod.GET)
    String findUsers(@RequestParam(value = "page",defaultValue = "1")int page,
                  @RequestParam(value = "name", required = false)String name ,
                  @RequestParam(value = "sex", required = false)Integer sex){
        int count = userService.countUsers(name, sex);
        int pages = (count < Contant.USER_PAGE)? 1: (count / Contant.USER_PAGE + 1);
        PageHelper.startPage(page, Contant.USER_PAGE);
        List<User> users = userService.findUsers(name, sex);
        ResultPageSet result = new ResultPageSet(users);
        result.setPages(pages);
        return gson.toJson(result);
    }

    /**
     * @description 分页查找群组
     * @param page 第几页
     * @param name 群名称
     */
    @ResponseBody
    @RequestMapping(value = "/findGroups", method = RequestMethod.GET)
    String findGroups(@RequestParam(value = "page",defaultValue = "1")int page,
                   @RequestParam(value = "name", required = false)String name){
        int count = userService.countGroup(name);
        int pages = (count < Contant.USER_PAGE)? 1 :(count / Contant.USER_PAGE + 1);
        PageHelper.startPage(page, Contant.USER_PAGE);
        List<GroupList> groups = userService.findGroup(name);
        ResultPageSet result = new ResultPageSet(groups);
        result.setPages(pages);
        return gson.toJson(result);
    }

    /**
     * @description 获取聊天记录
     * @param id 与谁的聊天记录id
     * @param Type 类型，可能是friend或者是group
     */
    @ResponseBody
    @RequestMapping(value = "/chatLog", method = RequestMethod.POST)
    String chatLog(@RequestParam("id")Integer id , @RequestParam("Type")String Type,
                @RequestParam("page")int page,HttpServletRequest request,Model model){
        User user = (User) request.getSession().getAttribute("user");
        PageHelper.startPage(page, Contant.SYSTEM_PAGE);
        //查找聊天记录
        List<ChatHistory> historys = userService.findHistoryMessage(user, id, Type);
        return gson.toJson(new ResultSet(historys));
    }

    /**
     * @description 弹出聊天记录页面
     * @param id 与谁的聊天记录id
     * @param Type 类型，可能是friend或者是group
     */
    @RequestMapping(value = "/chatLogIndex", method = RequestMethod.GET)
    String chatLogIndex(@RequestParam("id")Integer id, @RequestParam("Type")String Type,
                        Model model,HttpServletRequest request) {
        model.addAttribute("id", id);
        model.addAttribute("Type", Type);
        User user = (User) request.getSession().getAttribute("user");
        int pages = userService.countHistoryMessage(user.getId(), id, Type);
        pages =  (pages < Contant.SYSTEM_PAGE)? pages : (pages / Contant.SYSTEM_PAGE + 1);
        model.addAttribute("pages", pages);
        return "chatLog";
    }

    /**
     * @description 获取离线消息
     */
    @ResponseBody
    @RequestMapping(value = "/getOffLineMessage", method = RequestMethod.POST)
    String getOffLineMessage(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        LOGGER.info("查询 uid = " + user.getId() + " 的离线消息");
        List<Receive> receives = userService.findOffLineMessage(user.getId(), 0);
        receives.forEach(receive -> {
                User user1 = userService.findUserById(receive.getId());
                receive.setUsername(user1.getUsername());
                receive.setAvatar(user1.getAvatar());
        } );
        return gson.toJson(new ResultSet(receives)).replaceAll("Type", "type");
    }


    /**
     * @description 更新签名
     * @param sign
     *
     */
    @ResponseBody
    @RequestMapping(value = "/updateSign", method = RequestMethod.POST)
    String updateSign(HttpServletRequest request, @RequestParam("sign")String sign) {
        User user = (User) request.getSession().getAttribute("user");
        user.setSign(sign);
        if(userService.updateSing(user)) {
            return gson.toJson(new ResultSet());
        } else {
            return gson.toJson(new ResultSet(Contant.ERROR, Contant.ERROR_MESSAGE));
        }
    }

    /**
     * @description 激活
     * @param activeCode
     *
     */
    @RequestMapping(value = "/active/{activeCode}", method = RequestMethod.GET)
    String activeUser(@PathVariable("activeCode")String activeCode) {
        if(userService.activeUser(activeCode) == 1) {
            return "redirect:/#tologin?status=1";
        }
        return "redirect:/#toregister?status=0";
    }

    /**
     * @description 注册
     * @param user
     *
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    String register(@RequestBody User user, HttpServletRequest request) {
        if(userService.saveUser(user, request)) {
            return gson.toJson(new ResultSet(Contant.SUCCESS, Contant.REGISTER_SUCCESS));
        } else {
            return gson.toJson(new ResultSet(Contant.ERROR, Contant.REGISTER_FAIL));
        }
    }

    /**
     * @description 登陆
     * @param user
     *
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    String login(@RequestBody User user,HttpServletRequest request)  {
        User u = userService.matchUser(user);
        //未激活
        if (u != null && "nonactivated".equals(u.getStatus())) {
            return gson.toJson(new ResultSet(Contant.ERROR, Contant.NONACTIVED));
        } else if(u != null && !"nonactivated".equals(u.getStatus())) {
            LOGGER.info(user + "成功登陆服务器");
            request.getSession().setAttribute("user", u);
            return gson.toJson(new ResultSet<User>(u));
        } else {
            ResultSet result = new ResultSet<User>(Contant.ERROR, Contant.LOGGIN_FAIL);
            return gson.toJson(result);
        }
    }

    /**
     * @description  初始化主界面数据
     * @param userId
     *
     */
    @ResponseBody
    @ApiOperation("初始化聊天界面数据，分组列表好友信息、群列表")
    @RequestMapping(value = "/init/{userId}", method = RequestMethod.POST)
    String init(@PathVariable("userId")int userId) {
        FriendAndGroupInfo data = new FriendAndGroupInfo();
        //用户信息
        User user = userService.findUserById(userId);
        user.setStatus("online");
        data.setMine(user) ;
        //用户群组列表
        data.setGroup(userService.findGroupsById(userId));
        //用户好友列表
        data.setFriend(userService.findFriendGroupsById(userId));
        return gson.toJson(new ResultSet<FriendAndGroupInfo>(data));
    }

    /**
     * @description 获取群成员
     * @param id
     *
     */
    @ResponseBody
    @RequestMapping(value = "/getMembers", method = RequestMethod.GET)
    String getMembers(@RequestParam("id")int id) {
        List<User> users = userService.findUserByGroupId(id);
        FriendList friends = new FriendList();
        friends.setList(users);
        return gson.toJson(new ResultSet<FriendList>(friends));
    }

    /**
     * @description 客户端上传图片
     * @param file
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
    String uploadImage(@RequestParam("file")MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return gson.toJson(new ResultSet(Contant.ERROR, Contant.UPLOAD_FAIL));
        }
        String path = request.getServletContext().getRealPath("/");
        String src = FileUtil.upload(Contant.IMAGE_PATH, path, file);
        Map<String, String> result = new HashMap<String, String>();
        //图片的相对路径地址
        result.put("src", src);
        LOGGER.info("图片" + file.getOriginalFilename() + "上传成功");
        return gson.toJson(new ResultSet(result));
    }

    /**
     * @description 客户端上传文件
     * @param file
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    String uploadFile(@RequestParam("file")MultipartFile file,HttpServletRequest request) {
        if (file.isEmpty()) {
            return gson.toJson(new ResultSet(Contant.ERROR, Contant.UPLOAD_FAIL));
        }
        String path = request.getServletContext().getRealPath("/");
        String src = FileUtil.upload(Contant.FILE_PATH, path, file);
        Map<String, String> result = new HashMap<String, String>();
        //文件的相对路径地址
        result.put("src", src);
        result.put("name", file.getOriginalFilename());
        LOGGER.info("文件" + file.getOriginalFilename() + "上传成功");
        return gson.toJson(new ResultSet(result));
    }

    /**
     * @description用户更新头像
     */
    @ResponseBody
    @RequestMapping(value = "/updateAvatar", method = RequestMethod.POST)
    String updateAvatar(@RequestParam("avatar")MultipartFile avatar,HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String path = request.getServletContext().getRealPath(Contant.AVATAR_PATH);
        String src = FileUtil.upload(path, avatar);
        userService.updateAvatar(user.getId(), src);
        Map<String,String> result = new HashMap<String,String>();
        result.put("src", src);
        return gson.toJson(new ResultSet(result));
    }

    /**
     * @description 跳转主页
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    String index(Model model,HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        LOGGER.info("用户" + user + "登陆服务器");
        return "index";
    }

    /**
     * @description 根据id查找用户信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUser", method = {RequestMethod.POST, RequestMethod.GET})
    String findUserById(@RequestParam("id")Integer id){
        return gson.toJson(new ResultSet(userService.findUserById(id)));
    }

    /**
     * @description 判断邮件是否存在
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/existEmail", method = RequestMethod.POST)
    String existEmail(@RequestParam("email")String email){
        return gson.toJson(new ResultSet(userService.existEmail(email)));
    }
}
