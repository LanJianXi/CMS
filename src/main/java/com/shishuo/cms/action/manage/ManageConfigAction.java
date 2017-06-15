/*
 *	Copyright © 2013 Changsha Shishuo Network Technology Co., Ltd. All rights reserved.
 *	长沙市师说网络科技有限公司 版权所有
 *	http://www.shishuo.com
 */

package com.shishuo.cms.action.manage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shishuo.cms.entity.Config;
import com.shishuo.cms.entity.Friendlylink;
import com.shishuo.cms.service.ConfigService;
import com.shishuo.cms.service.FriendlylinkService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shishuo.cms.constant.SystemConstant;
import com.shishuo.cms.entity.vo.JsonVo;
import com.shishuo.cms.util.SSUtils;

/**
 * 网站配置action
 *
 * @author Herbert
 */
@Controller
@RequestMapping("/manage/config")
public class ManageConfigAction extends ManageBaseAction {

    @Autowired
    private FriendlylinkService friendlylinkService;

    @RequestMapping(value = "/basic.htm", method = RequestMethod.GET)
    public String basic(ModelMap modelMap) {
        List<Config> configs = configService.findAll();
        modelMap.put("configs", configs);
        modelMap.put("list",friendlylinkService.getAllList());
        return "manage/config/basic";
    }

    /**
     * 修改网站配置
     *
     * @author Administrator
     */
    @ResponseBody
    @RequestMapping(value = "/basic.json", method = RequestMethod.POST)
    public JsonVo<String> basicSubmit(@RequestParam Map<String, String> map) {
        JsonVo<String> json = new JsonVo<String>();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (StringUtils.isBlank(entry.getValue())) {
                    json.getErrors().put(entry.getKey(), "参数值不能为空");
                }
            }
            // 检测校验结果
            validate(json);
           configService.updagteConfig(map);
            json.setResult(true);
        } catch (Exception e) {
            json.setResult(false);
            json.setMsg(e.getMessage());
        }
        return json;

    }


    @ResponseBody
    @RequestMapping(value = "/addNew.json", method = RequestMethod.POST)
    public JsonVo<String> addNewFriendlylink(Friendlylink friendlylink) {
        JsonVo<String> json = new JsonVo<String>();
        try {
            if (friendlylink.getName().equals("")) {
                json.getErrors().put("name", "名称不能为空");
            }
            if (StringUtils.isBlank(friendlylink.getUrl())) {
                json.getErrors().put("password", "链接地址不能为空");
            }
            // 检测校验结果
            validate(json);
            friendlylinkService.addFriendlylink(friendlylink);
            json.setResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setResult(false);
            json.setMsg(e.getMessage());
        }
        return json;
    }

    @ResponseBody
    @RequestMapping(value = "/delete.json", method = RequestMethod.POST)
    public JsonVo<String> delete(@RequestParam(value = "id") Integer id) {
        JsonVo<String> json = new JsonVo<String>();
        try {
            friendlylinkService.deleteFriendlylink(id);
            json.setResult(true);
        } catch (Exception e) {
            json.setResult(false);
            json.setMsg(e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/update.htm", method = RequestMethod.GET)
    public String update(
            @RequestParam(value = "id") Integer id,
            ModelMap modelMap) throws Exception {
        modelMap.put("friendlylink", friendlylinkService.getFriendlylinkById(id));
        return "manage/config/updatefriendLink";
    }


    @ResponseBody
    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    public JsonVo<Friendlylink> update(Friendlylink friendlylink){
        JsonVo<Friendlylink> json = new JsonVo<Friendlylink>();
        try {
            friendlylinkService.updateFriendlylinkByFriendlylinkId(friendlylink);
            json.setResult(true);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            json.setResult(false);
            return json;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sort.json", method = RequestMethod.POST)
    public JsonVo<String> sort(
            @RequestParam(value = "sortJson") String sortJson) {
        JsonVo<String> json = new JsonVo<String>();
        JSONArray array = JSONArray.fromObject(sortJson);
        for (int i = 0; i < array.size(); i++) {
            JSONObject friendlylink = array.getJSONObject(i);
            String id = friendlylink.get("id").toString();
            String sort = friendlylink.get("sort").toString();
            friendlylinkService.modifySortById(Integer.parseInt(id),
                    Integer.parseInt(sort));
        }
        json.setResult(true);
        return json;
    }

}
