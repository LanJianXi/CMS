/*
 *	Copyright © 2013 Changsha Shishuo Network Technology Co., Ltd. All rights reserved.
 *	长沙市师说网络科技有限公司 版权所有
 *	http://www.shishuo.com
 */

package com.shishuo.cms.action.manage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.shishuo.cms.entity.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shishuo.cms.entity.Admin;

/**
 * @author lqq
 * @author 进入网站后台首页
 * 
 */

@Controller
@RequestMapping("/manage")
public class ManageAction extends ManageBaseAction {

	@RequestMapping(value = "/index.htm", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap modelMap)
			{
		modelMap.put("articleCount", 0);
		modelMap.put("downloadCount", 0);
		modelMap.put("userCount", 0);


		return "manage/index";
	}

}
